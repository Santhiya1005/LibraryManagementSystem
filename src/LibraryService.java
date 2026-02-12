import java.sql.*;
import java.util.Scanner;

public class LibraryService {

    private Scanner sc = new Scanner(System.in);

    // ✅ Add Book (NO ID input)
    public void addBook() {

        System.out.println("Enter book name: ");
        String title = sc.nextLine();

        System.out.println("Enter book author: ");
        String author = sc.nextLine();

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "INSERT INTO books(title, author, available) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, title);
            pst.setString(2, author);
            pst.setBoolean(3, true);

            pst.executeUpdate();

            System.out.println("Book added successfully 🔥");

            conn.close();

        } catch (SQLException e) {
            System.out.println("Error while adding book 😢");
            e.printStackTrace();
        }
    }

    // ✅ View Books
    public void viewBooks() {

        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM books");

            boolean empty = true;

            while (rs.next()) {
                empty = false;

                System.out.println("ID: " + rs.getInt("book_id") +
                        ", Title: " + rs.getString("title") +
                        ", Author: " + rs.getString("author") +
                        ", Available: " + rs.getBoolean("available"));
            }

            if (empty) {
                System.out.println("No books available");
            }

            conn.close();

        } catch (SQLException e) {
            System.out.println("Error while fetching books 😢");
            e.printStackTrace();
        }
    }

    // ✅ Update Book
    public void updateBooks() {

        System.out.println("Enter book ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter new title: ");
        String newTitle = sc.nextLine();

        System.out.println("Enter new author: ");
        String newAuthor = sc.nextLine();

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "UPDATE books SET title = ?, author = ? WHERE book_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, newTitle);
            pst.setString(2, newAuthor);
            pst.setInt(3, id);

            int rows = pst.executeUpdate();

            if (rows > 0) {
                System.out.println("Book updated successfully 🔥");
            } else {
                System.out.println("Book not found 😢");
            }

            conn.close();

        } catch (SQLException e) {
            System.out.println("Error while updating book 😢");
            e.printStackTrace();
        }
    }

    // ✅ Delete Book
    public void deleteBooks() {

        System.out.println("Enter book ID to delete: ");
        int id = sc.nextInt();
        sc.nextLine();

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "DELETE FROM books WHERE book_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setInt(1, id);

            int rows = pst.executeUpdate();

            if (rows > 0) {
                System.out.println("Book deleted successfully 🔥");
            } else {
                System.out.println("Book not found 😢");
            }

            conn.close();

        } catch (SQLException e) {
            System.out.println("Error while deleting book 😢");
            e.printStackTrace();
        }
    }
    public void addStudent() {
        try {
            System.out.println("Enter student name: ");
            String name = sc.nextLine();

            System.out.println("Enter student email: ");
            String email = sc.nextLine();

            System.out.println("Enter student phone: ");
            String phone = sc.nextLine();

            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO students(name, email, phone) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);

            ps.executeUpdate();
            System.out.println("Student added successfully 🔥");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void viewStudents() {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM students";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                        "ID: " + rs.getInt("student_id") +
                                ", Name: " + rs.getString("name") +
                                ", Email: " + rs.getString("email") +
                                ", Phone: " + rs.getString("phone")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void issueBook() {
        try {
            System.out.println("Enter Book ID: ");
            int bookId = Integer.parseInt(sc.nextLine());

            System.out.println("Enter Student ID: ");
            int studentId = Integer.parseInt(sc.nextLine());

            Connection conn = DBConnection.getConnection();

            // Check availability
            String checkSql = "SELECT available FROM books WHERE book_id = ?";
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setInt(1, bookId);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                boolean available = rs.getBoolean("available");

                if (available) {

                    // Insert into issued_books
                    String issueSql = "INSERT INTO issued_books(book_id, student_id, issue_date) VALUES (?, ?, CURRENT_DATE)";
                    PreparedStatement issuePs = conn.prepareStatement(issueSql);
                    issuePs.setInt(1, bookId);
                    issuePs.setInt(2, studentId);
                    issuePs.executeUpdate();

                    // Update book availability
                    String updateSql = "UPDATE books SET available = false WHERE book_id = ?";
                    PreparedStatement updatePs = conn.prepareStatement(updateSql);
                    updatePs.setInt(1, bookId);
                    updatePs.executeUpdate();

                    System.out.println("Book issued successfully 🔥");

                } else {
                    System.out.println("Book already issued ❌");
                }

            } else {
                System.out.println("Book not found ❌");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void returnBook() {
        try {
            System.out.println("Enter Book ID to return: ");
            int bookId = Integer.parseInt(sc.nextLine());

            Connection conn = DBConnection.getConnection();

            // Update return date
            String returnSql = "UPDATE issued_books SET return_date = CURRENT_DATE WHERE book_id = ? AND return_date IS NULL";
            PreparedStatement returnPs = conn.prepareStatement(returnSql);
            returnPs.setInt(1, bookId);
            int rows = returnPs.executeUpdate();

            if (rows > 0) {

                // Update book availability
                String updateSql = "UPDATE books SET available = true WHERE book_id = ?";
                PreparedStatement updatePs = conn.prepareStatement(updateSql);
                updatePs.setInt(1, bookId);
                updatePs.executeUpdate();

                System.out.println("Book returned successfully 🔥");

            } else {
                System.out.println("No active issue found for this book ❌");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Menu
    public void menu() {

        boolean exit = false;

        while (!exit) {

            System.out.println("\n=== Library Menu ===");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Update Book");
            System.out.println("4. Delete Book");
            System.out.println("5. Add Student");
            System.out.println("6. View Students");
            System.out.println("7. Issue Book");
            System.out.println("8. Return Book");
            System.out.println("9. Exit");

            int option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    addBook();
                    break;
                case 2:
                    viewBooks();
                    break;
                case 3:
                    updateBooks();
                    break;
                case 4:
                    deleteBooks();
                    break;
                case 5:
                    addStudent();
                    break;

                case 6:
                    viewStudents();
                    break;
                case 7:
                    issueBook();
                    break;

                case 8:
                    returnBook();
                    break;

                case 9:
                    exit = true;
                    System.out.println("Exiting... 👋");
                    break;

                default:
                    System.out.println("Invalid input 😢");
            }
        }
    }
}
