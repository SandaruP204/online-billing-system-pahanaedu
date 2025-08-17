package dao.impl;

import dao.UserDAO;
import dao.DBConnection;
import model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAOimpl implements UserDAO {

    private Connection getConnection() throws Exception {
        return DBConnection.getConnection();
    }

    // SHA-256 (unsalted) → hex string
    private String sha256Hex(String raw) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] out = md.digest(raw.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder(out.length * 2);
        for (byte b : out) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    @Override
    public User findByUsername(String username) throws Exception {
        final String sql = "SELECT id, username, password, role FROM users WHERE username = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setUsername(rs.getString("username"));
                    // (Optional) you can omit storing the hash in the model:
                    u.setPassword(rs.getString("password"));
                    u.setRole(rs.getString("role"));
                    return u;
                }
            }
        }
        return null;
    }

    @Override
    public boolean usernameExists(String username) throws Exception {
        final String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public void create(User user) throws Exception {
        final String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        String hash = sha256Hex(user.getPassword() == null ? "" : user.getPassword());
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, hash);          // store SHA-256 hex
            ps.setString(3, user.getRole());
            ps.executeUpdate();
        }
    }

    @Override
    public boolean validateLogin(String username, String rawPassword) throws Exception {
        final String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Optional: print which DB/schema we’re actually connected to
            try (var dbRs = con.createStatement().executeQuery("SELECT DATABASE()")) {
                if (dbRs.next()) System.out.println("LOGIN DB = " + dbRs.getString(1));
            }

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("LOGIN DEBUG: no row for username=" + username);
                    return false;
                }
                String storedHex = rs.getString("password");
                String attemptHex = sha256Hex(rawPassword == null ? "" : rawPassword).toLowerCase();

                if (storedHex != null) storedHex = storedHex.trim().toLowerCase();

                boolean eq = storedHex != null && attemptHex.equals(storedHex);
                System.out.println("LOGIN DEBUG: user=" + username +
                        " stored.len=" + (storedHex == null ? 0 : storedHex.length()) +
                        " eq=" + eq);
                return eq;
            }
        }
    }
}
