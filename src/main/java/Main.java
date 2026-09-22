import Controller.AuthController;
import Controller.RoomController;
import config.DatabaseMigration;
import db.DatabaseConnection;
import model.UserDomain;
import model.enums.UserRole;
import repository.IRoomRepository;
import repository.IUserRepository;
import repository.jdbc.RoomRepositoryJdbc;
import repository.jdbc.UserRepositoryJdbc;
import service.AuthService;
import service.RoomService;

import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // ==============================
        // Database migration
        // ==============================

        DatabaseMigration.migrate();

        System.out.println("Database migration completed!");

        // ==============================
        // Database connection
        // ==============================

        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // ==============================
        // User repository
        // ==============================

        IUserRepository userRepo = new UserRepositoryJdbc(databaseConnection);

        // ==============================
        // Auth service
        // ==============================

        AuthService authService = new AuthService(userRepo);

        // ==============================
        // Auth controller
        // ==============================

        AuthController authController = new AuthController(authService);


        IRoomRepository roomRepo = new RoomRepositoryJdbc(databaseConnection);

        RoomService roomService = new RoomService(roomRepo, userRepo);
        RoomController roomController = new RoomController(roomService);

        // ==============================
        // Application state
        // ==============================


        boolean running = true;
        boolean isLoggedIn = false;

        UserDomain loggedInUser = null;
        UUID currentUserId = null;

        // ==============================
        // Main application loop
        // ==============================

        while (running) {

            // ==============================
            // NOT LOGGED IN
            // ==============================

            if (!isLoggedIn) {

                System.out.println("========================");

                System.out.println("HOTEL BOOKING");

                System.out.println("========================");

                System.out.println("1. Register");

                System.out.println("2. Login");

                System.out.println("0. Exit");

                System.out.print("Choice: ");

                int choice = readSafeInt(scanner);

                switch (choice) {

                    case 1:

                        authController.register(scanner);

                        break;

                    case 2:

                        loggedInUser = authController.login(scanner);

                        if (loggedInUser != null) {

                            isLoggedIn = true;

                            currentUserId = loggedInUser.getId();

                            System.out.println("\nLogged in successfully!\n");
                        }

                        break;

                    case 0:

                        running = false;

                        System.out.println("Goodbye!");

                        break;

                    default:

                        System.out.println("\nInvalid choice. " + "Please enter a valid number.\n");
                }

            }

            // ==============================
            // CLIENT MENU
            // ==============================

            else if (loggedInUser.getRole().equals(UserRole.CLIENT)) {

                System.out.println("================================");

                System.out.println("CLIENT MENU");

                System.out.println("================================");
                System.out.println("5. Make Reservation");

                System.out.println("6. Cancel Reservation");

                System.out.println("7. Update profile");

                System.out.println("9. Logout");

                System.out.println("0. Exit");

                System.out.print("Choice: ");

                int choice = readSafeInt(scanner);

                switch (choice) {

                    case 7:

                        authController.updateProfile(scanner, currentUserId);

                        break;

                    case 9:

                        isLoggedIn = false;
                        loggedInUser = null;
                        currentUserId = null;

                        System.out.println("\nLogged out successfully.\n");

                        break;

                    case 0:

                        running = false;

                        System.out.println("Goodbye!");

                        break;

                    default:

                        System.out.println("\nInvalid choice.\n");
                }

            }

            // ==============================
            // ADMIN MENU
            // ==============================

            else if (loggedInUser.getRole().equals(UserRole.ADMIN)) {

                System.out.println("================================");

                System.out.println("ADMIN MENU");

                System.out.println("================================");

                System.out.println("1. Create Room");

                System.out.println("2. View Rooms");

                System.out.println("3. Update Room");

                System.out.println("4. Delete Room");

                System.out.println("5. Accept Reservation");

                System.out.println("6. Cancel Reservation");

                System.out.println("7. Update Profile");

                System.out.println("9. Logout");

                System.out.println("0. Exit");

                System.out.print("Choice: ");

                int choice = readSafeInt(scanner);

                switch (choice) {

                    case 1:
                        roomController.createRoom(scanner, loggedInUser.getId());

                        break;

                    case 2:

                        roomController.listRooms(loggedInUser.getId());

                        break;

                    case 3:
                        roomController.listRooms(loggedInUser.getId());
                        roomController.updateRoom(scanner, loggedInUser.getId());
                        break;

                    case 4:

                        roomController.deleteRoom(scanner, loggedInUser.getId());

                        break;

                    case 5:

                        System.out.println("\n[Accept Reservation - not connected yet]\n");

                        break;

                    case 6:

                        System.out.println("\n[Cancel Reservation - not connected yet]\n");

                        break;

                    case 7:

                        authController.updateProfile(scanner, currentUserId);

                        break;

                    case 9:

                        isLoggedIn = false;
                        loggedInUser = null;
                        currentUserId = null;

                        System.out.println("\nLogged out successfully.\n");

                        break;

                    case 0:

                        running = false;

                        System.out.println("Goodbye!");

                        break;

                    default:

                        System.out.println("\nInvalid choice.\n");
                }
            }
        }

        scanner.close();
    }

    private static int readSafeInt(Scanner scanner) {

        try {

            return Integer.parseInt(scanner.nextLine().trim());

        } catch (NumberFormatException e) {

            return -1;
        }
    }
}