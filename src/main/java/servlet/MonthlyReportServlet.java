package servlet;

import dao.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/reports/monthly")
public class MonthlyReportServlet extends HttpServlet {

    public static class DailyTotal {
        public LocalDate day;
        public int bills;
        public BigDecimal revenue;
    }

    public static class TopItem {
        public int productNo;
        public String name;
        public long qty;              // total units sold in the month
        public BigDecimal estRevenue; // OPTIONAL: qty * current price (see note)
    }

    private Connection getConn() throws SQLException {
        // Use your existing DB helper so it works with your env
        return DBConnection.getConnection();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Accept ?month=yyyy-MM (e.g. 2025-08); default = current month
        String monthParam = req.getParameter("month");
        YearMonth ym = (monthParam == null || monthParam.isBlank())
                ? YearMonth.now() : YearMonth.parse(monthParam);

        LocalDate start = ym.atDay(1);
        LocalDate endExclusive = ym.plusMonths(1).atDay(1);

        try (Connection c = getConn()) {
            // --- Aggregates from bills ---
            BigDecimal total = BigDecimal.ZERO;
            int count = 0;
            BigDecimal avg = BigDecimal.ZERO;

            String aggSql = "SELECT COUNT(*) AS cnt, COALESCE(SUM(total_amount),0) AS sum, " +
                    "COALESCE(AVG(total_amount),0) AS avg " +
                    "FROM bills WHERE bill_date >= ? AND bill_date < ?";
            try (PreparedStatement ps = c.prepareStatement(aggSql)) {
                ps.setTimestamp(1, Timestamp.valueOf(start.atStartOfDay()));
                ps.setTimestamp(2, Timestamp.valueOf(endExclusive.atStartOfDay()));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        count = rs.getInt("cnt");
                        total = rs.getBigDecimal("sum");
                        avg = rs.getBigDecimal("avg");
                    }
                }
            }

            // --- Daily totals from bills ---
            List<DailyTotal> days = new ArrayList<>();
            String dailySql = "SELECT DATE(bill_date) AS d, COUNT(*) bills, COALESCE(SUM(total_amount),0) revenue " +
                    "FROM bills WHERE bill_date >= ? AND bill_date < ? " +
                    "GROUP BY DATE(bill_date) ORDER BY d";
            try (PreparedStatement ps = c.prepareStatement(dailySql)) {
                ps.setTimestamp(1, Timestamp.valueOf(start.atStartOfDay()));
                ps.setTimestamp(2, Timestamp.valueOf(endExclusive.atStartOfDay()));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        DailyTotal dt = new DailyTotal();
                        dt.day = rs.getDate("d").toLocalDate();
                        dt.bills = rs.getInt("bills");
                        dt.revenue = rs.getBigDecimal("revenue");
                        days.add(dt);
                    }
                }
            }

            // --- Top 5 items by quantity (joins bill_items + bills + products) ---
            // NOTE: Your bill_items table does not store unit_price at sale time.
            // So: we rank by qty (accurate), and we can show an *estimated* revenue using current price.
            List<TopItem> topItems = new ArrayList<>();
            String topSql =
                    "SELECT bi.productNo, p.name, SUM(bi.quantity) AS qty, " +
                            "       SUM(bi.quantity * p.price) AS estRevenue " +   // estimated, since price may have changed
                            "FROM bill_items bi " +
                            "JOIN bills b ON bi.bill_id = b.bill_id " +
                            "JOIN products p ON bi.productNo = p.productNo " +
                            "WHERE b.bill_date >= ? AND b.bill_date < ? " +
                            "GROUP BY bi.productNo, p.name " +
                            "ORDER BY qty DESC " +
                            "LIMIT 5";
            try (PreparedStatement ps = c.prepareStatement(topSql)) {
                ps.setTimestamp(1, Timestamp.valueOf(start.atStartOfDay()));
                ps.setTimestamp(2, Timestamp.valueOf(endExclusive.atStartOfDay()));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        TopItem t = new TopItem();
                        t.productNo  = rs.getInt("productNo");
                        t.name       = rs.getString("name");
                        t.qty        = rs.getLong("qty");
                        t.estRevenue = rs.getBigDecimal("estRevenue");
                        topItems.add(t);
                    }
                }
            }

            req.setAttribute("month", ym.toString()); // yyyy-MM
            req.setAttribute("total", total);
            req.setAttribute("count", count);
            req.setAttribute("avg", avg);
            req.setAttribute("daily", days);
            req.setAttribute("topItems", topItems);
            req.getRequestDispatcher("/monthly_report.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Monthly report failed: " + e.getMessage());
            req.getRequestDispatcher("/monthly_report.jsp").forward(req, resp);
        }
    }
}
