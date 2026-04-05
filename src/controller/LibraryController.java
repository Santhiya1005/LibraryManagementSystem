package controller;

import service.LibraryService;
import java.util.Scanner;

public class LibraryController {

    private Scanner sc = new Scanner(System.in);
    private LibraryService service = new LibraryService();

    public void menu() {

        boolean exit = false;

        while (!exit) {

            System.out.println("\n=== Library Menu ===");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Update Book");
            System.out.println("6. Delete Book");
            System.out.println("7. Add Student");
            System.out.println("8. View Students");
            System.out.println("9. Exit");

            System.out.print("Enter choice: ");
            int option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    service.addBook();
                    break;

                case 2:
                    service.viewBooks();
                    break;

                case 3:
                    service.issueBook();
                    break;

                case 4:
                    service.returnBook();
                    break;

                case 5:
                    service.updateBooks();
                    break;

                case 6:
                    service.deleteBooks();
                    break;

                case 7:
                    service.addStudent();
                    break;

                case 8:
                    service.viewStudents();
                    break;

                case 9:
                    exit = true;
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid input");
            }
        }
    }
}