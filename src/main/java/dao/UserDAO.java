// src/main/java/dao/UserDAOimpl.java
package dao;

import model.User;

public interface UserDAO {
    User findByUsername(String username) throws Exception;
    boolean usernameExists(String username) throws Exception;
    void create(User user) throws Exception;
    boolean validateLogin(String username, String rawPassword) throws Exception; // if you store salted hashes, adapt
}
