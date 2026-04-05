package dao;

import util.DBConnection;
import java.sql.*;

public class StudentDAO {

    public void addStudent(String name, String email, String phone) throws SQLException {
        Connection conn = DBConnection.getConnection();

        String sql = "INSERT INTO students(name, email, phone) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, phone);

        ps.executeUpdate();

        conn.close();
    }

    public ResultSet viewStudents() throws SQLException {
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();

        return stmt.executeQuery("SELECT * FROM students");
    }
}