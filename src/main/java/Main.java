import Controller.AuthController;
import config.DatabaseMigration;
import db.DatabaseConnection;
import model.UserDomain;
import repository.IUserRepository;
import repository.jdbc.UserRepositoryJdbc;
import service.AuthService;

import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);


        DatabaseMigration.migrate();

        System.out.println("Database migration completed!");


        DatabaseConnection databaseConnection =
                DatabaseConnection.getInstance();


        IUserRepository userRepo =
                new UserRepositoryJdbc(databaseConnection);


        AuthService authService =
                new AuthService(userRepo);


        AuthController authController =
                new AuthController(authService);

        // ==============================
        // Application state
        // ==============================

        boolean running = true;
        boolean isLoggedIn = false;

        UserDomain loggedInUser = null;
        UUID currentUserId = null;

        // ==============================
        // Main menu
        // ==============================

        while (running) {

            if (!isLoggedIn) {

                System.out.println("========================");
                System.out.println("HOTEL BOOKING");
                System.out.println("========================");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("0. Exit");
                System.out.print("Choice: ");

                int choice =
                        readSafeInt(scanner);

                switch (choice) {

                    case 1:

                        authController.register(scanner);

                        break;

                    case 2:

                        loggedInUser =
                                authController.login(scanner);

                        if (loggedInUser != null) {

                            isLoggedIn = true;

                            currentUserId =
                                    loggedInUser.getId();

                            System.out.println(
                                    "\nLogged in successfully!\n"
                            );
                        }

                        break;

                    case 0:

                        running = false;

                        System.out.println(
                                "Goodbye!"
                        );

                        break;

                    default:

                        System.out.println(
                                "\nInvalid choice. " +
                                        "Please enter a valid number.\n"
                        );
                }

            } else {

                System.out.println(
                        "================================"
                );

                System.out.println("MAIN MENU");

                System.out.println(
                        "================================"
                );

                System.out.println(
                        "7. Update profile"
                );

                System.out.println(
                        "9. Logout"
                );

                System.out.println(
                        "0. Exit"
                );

                System.out.print("Choice: ");

                int choice =
                        readSafeInt(scanner);

                switch (choice) {

                    case 7:

                        authController.updateProfile(
                                scanner,
                                currentUserId
                        );

                        break;

                    case 9:

                        isLoggedIn = false;
                        loggedInUser = null;
                        currentUserId = null;

                        System.out.println(
                                "\nLogged out successfully.\n"
                        );

                        break;

                    case 0:

                        running = false;

                        System.out.println(
                                "Goodbye!"
                        );

                        break;

                    default:

                        System.out.println(
                                "\nInvalid choice.\n"
                        );
                }
            }
        }

        scanner.close();
    }

    private static int readSafeInt(
            Scanner scanner
    ) {

        try {

            return Integer.parseInt(
                    scanner.nextLine().trim()
            );

        } catch (NumberFormatException e) {

            return -1;
        }
    }
}