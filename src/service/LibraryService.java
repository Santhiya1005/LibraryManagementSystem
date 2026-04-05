package service;

import dao.BookDAO;
import util.DBConnection;
import dao.StudentDAO;
import java.sql.*;
import java.util.Scanner;

public class LibraryService {

    Scanner sc = new Scanner(System.in);
    BookDAO bookDAO = new BookDAO();
    StudentDAO studentDAO = new StudentDAO();
    // add book
    public void addBook() {
        try {
            System.out.print("Enter book title: ");
            String title = sc.nextLine();

            System.out.print("Enter author: ");
            String author = sc.nextLine();

            bookDAO.addBook(title, author);

            System.out.println("Book added successfully");

        } catch (Exception e) {
            System.out.println("Error adding book");
        }
    }

    // view books
    public void viewBooks() {
        try {
            ResultSet rs = bookDAO.viewBooks();

            while (rs.next()) {
                System.out.println(
                        rs.getInt("book_id") + " - " +
                                rs.getString("title") + " - " +
                                rs.getString("author") + " - " +
                                rs.getBoolean("available")
                );
            }

        } catch (Exception e) {
            System.out.println("Error fetching books");
        }
    }

    // issue book
    public void issueBook() {
        try {
            System.out.print("Enter book id: ");
            int bookId = Integer.parseInt(sc.nextLine());

            System.out.print("Enter student id: ");
            int studentId = Integer.parseInt(sc.nextLine());

            Connection conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            try {
                String checkSql = "SELECT available FROM books WHERE book_id=?";
                PreparedStatement checkPs = conn.prepareStatement(checkSql);
                checkPs.setInt(1, bookId);
                ResultSet rs = checkPs.executeQuery();

                if (rs.next() && rs.getBoolean("available")) {

                    // FIXED SQL (PostgreSQL compatible)
                    String issueSql = "INSERT INTO issued_books(book_id, student_id, issue_date, due_date) VALUES (?, ?, CURRENT_DATE, CURRENT_DATE + INTERVAL '7 days')";
                    PreparedStatement issuePs = conn.prepareStatement(issueSql);
                    issuePs.setInt(1, bookId);
                    issuePs.setInt(2, studentId);
                    issuePs.executeUpdate();

                    String updateSql = "UPDATE books SET available=false WHERE book_id=?";
                    PreparedStatement updatePs = conn.prepareStatement(updateSql);
                    updatePs.setInt(1, bookId);
                    updatePs.executeUpdate();

                    conn.commit();
                    System.out.println("Book issued");

                } else {
                    System.out.println("Book not available");
                    conn.rollback();
                }

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }

        } catch (Exception e) {
            System.out.println("Error issuing book");
        }
    }

    // return book
    public void returnBook() {
        try {
            System.out.print("Enter book id: ");
            int bookId = Integer.parseInt(sc.nextLine());

            System.out.print("Enter student id: ");
            int studentId = Integer.parseInt(sc.nextLine());

            Connection conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            try {
                String fetchSql = "SELECT due_date FROM issued_books WHERE book_id=? AND student_id=? AND return_date IS NULL";
                PreparedStatement fetchPs = conn.prepareStatement(fetchSql);
                fetchPs.setInt(1, bookId);
                fetchPs.setInt(2, studentId);

                ResultSet rs = fetchPs.executeQuery();

                if (rs.next()) {

                    Date dueDate = rs.getDate("due_date");
                    Date today = new Date(System.currentTimeMillis());

                    long diff = today.getTime() - dueDate.getTime();
                    long daysLate = diff / (1000 * 60 * 60 * 24);

                    if (daysLate > 0) {
                        int fine = (int) daysLate * 10;
                        System.out.println("Late return. Fine: " + fine);
                    } else {
                        System.out.println("Returned on time");
                    }

                    String updateReturn = "UPDATE issued_books SET return_date=CURRENT_DATE WHERE book_id=? AND student_id=?";
                    PreparedStatement returnPs = conn.prepareStatement(updateReturn);
                    returnPs.setInt(1, bookId);
                    returnPs.setInt(2, studentId);
                    returnPs.executeUpdate();

                    String updateBook = "UPDATE books SET available=true WHERE book_id=?";
                    PreparedStatement bookPs = conn.prepareStatement(updateBook);
                    bookPs.setInt(1, bookId);
                    bookPs.executeUpdate();

                    conn.commit();
                    System.out.println("Book returned");

                } else {
                    System.out.println("No record found");
                    conn.rollback();
                }

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }

        } catch (Exception e) {
            System.out.println("Error returning book");
        }
    }

    // update book
    public void updateBooks() {
        try {
            System.out.print("Enter book id: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("Enter new title: ");
            String title = sc.nextLine();

            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE books SET title=? WHERE book_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, title);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Book updated");
            } else {
                System.out.println("Book not found");
            }

        } catch (Exception e) {
            System.out.println("Error updating book");
        }
    }

    // delete book
    public void deleteBooks() {
        try {
            System.out.print("Enter book id: ");
            int id = Integer.parseInt(sc.nextLine());

            Connection conn = DBConnection.getConnection();
            String sql = "DELETE FROM books WHERE book_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Book deleted");
            } else {
                System.out.println("Book not found");
            }

        } catch (Exception e) {
            System.out.println("Error deleting book");
        }
    }

    // add student
    public void addStudent() {
        try {
            System.out.print("Enter student name: ");
            String name = sc.nextLine();

            System.out.print("Enter email: ");
            String email = sc.nextLine();

            System.out.print("Enter phone: ");
            String phone = sc.nextLine();

            studentDAO.addStudent(name, email, phone);

            System.out.println("Student added");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // view students
    public void viewStudents() {
        try {
            ResultSet rs = studentDAO.viewStudents();

            while (rs.next()) {
                System.out.println(
                        rs.getInt("student_id") + " - " +
                                rs.getString("name") + " - " +
                                rs.getString("email") + " - " +
                                rs.getString("phone")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}