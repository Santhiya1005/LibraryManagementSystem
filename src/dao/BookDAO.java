package dao;

import util.DBConnection;
import java.sql.*;

public class BookDAO {

    public void addBook(String title, String author) throws SQLException {
        Connection conn = DBConnection.getConnection();

        String sql = "INSERT INTO books(title, author, available) VALUES (?, ?, true)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, title);
        ps.setString(2, author);

        ps.executeUpdate();
        conn.close();
    }

    public ResultSet viewBooks() throws SQLException {
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT * FROM books");
    }
}