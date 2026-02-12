# Library Management System (Java + PostgreSQL)

A console-based Library Management System built using **Java (JDBC)** and **PostgreSQL**.  
This project allows management of books, students, and book issuing/returning functionality.

---

## Features

- ✅ Add Book  
- ✅ View Books  
- ✅ Update Book  
- ✅ Delete Book  
- ✅ Add Student  
- ✅ View Students  
- ✅ Issue Book  
- ✅ Return Book  
- ✅ Book Availability Tracking  

---

## Technologies Used

- Java (JDK 23)
- JDBC
- PostgreSQL
- IntelliJ IDEA
- Git & GitHub

---

## Database Structure

### Books Table
| Column      | Type      |
|------------|-----------|
| book_id    | SERIAL (PK) |
| title      | VARCHAR   |
| author     | VARCHAR   |
| available  | BOOLEAN   |

---

### Students Table
| Column      | Type      |
|------------|-----------|
| student_id | SERIAL (PK) |
| name       | VARCHAR   |
| email      | VARCHAR   |
| phone      | VARCHAR   |

---

### Issued Books Table
| Column      | Type      |
|------------|-----------|
| issue_id   | SERIAL (PK) |
| book_id    | INT (FK) |
| student_id | INT (FK) |
| issue_date | DATE |
| return_date| DATE |

---

## How It Works

### Issue Book Logic
- Checks if the book is available
- Inserts record into `issued_books`
- Updates book availability to `false`

### Return Book Logic
- Updates `return_date`
- Updates book availability to `true`

---

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/YOUR_USERNAME/LibraryManagementSystem.git
   ```

2. Open in IntelliJ IDEA

3. Configure PostgreSQL:
   - Create database
   - Create required tables

4. Update DB credentials inside:
   ```
   DBConnection.java
   ```

5. Run `Main.java`

---

## Learning Outcomes

- JDBC database connectivity
- CRUD operations
- Foreign key relationships
- Backend logic handling
- Git version control
- Real-world database project structure

---

## Future Improvements

- Search functionality
- Fine calculation for late returns
- GUI version (JavaFX / Swing)
- Spring Boot REST API version
- Authentication system

---

## Author

Santhiya  
GitHub: https://github.com/Santhiya1005

---

