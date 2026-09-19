//import Controllers.AuthController;
//import Controllers.ReservationController;
//import Controllers.RoomController;
//import Domains.UserDomain;
//import Repositories.ReservationRepository;
//import Repositories.RoomRepository;
//import Repositories.UserRepository;
//import Services.AuthService;
//import Services.ReservationService;
//import Services.RoomService;

import config.DatabaseMigration;
import db.DatabaseConnection;

import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        DatabaseMigration.migrate();
        DatabaseConnection.getInstance().getConnection();
        // Background scheduler to process expired reservations automatically
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {


            } catch (Exception e) {

            }
        }, 0, 1, TimeUnit.HOURS);

        boolean running = true;
        boolean isLoggedIn = false;
//        UserDomain loggedInUserdb = null;
        UUID currentUserId = null; // Track active user's repository UUID

        while (running) {
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
//                        authController.register(scanner);
                        break;
                    case 2:
//                        loggedInUser = authController.login(scanner);
//                        if (loggedInUser != null) {
//                            isLoggedIn = true;
//                            currentUserId = userRepo.findUserIdByEmail(loggedInUser.getEmail());
//                            System.out.println("\nLogged in successfully!\n");
//                            break;
//                        }
                        break;
                    case 0:
                        running = false;
                        scheduler.shutdown(); // Cleanly shut down background thread on exit
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("\nInvalid choice. Please enter a valid number.\n");
                }
            } else {
                System.out.println("================================");
                System.out.println("MAIN MENU");
                System.out.println("================================");
                System.out.println("1. Search available rooms");
                System.out.println("2. View all rooms");
                System.out.println("3. Create reservation");
                System.out.println("4. My reservations");
                System.out.println("5. Update reservation");
                System.out.println("6. Cancel reservation");
                System.out.println("7. Update profile");
                System.out.println("9. Logout");
                System.out.println("0. Exit");
                System.out.print("Choice: ");

                int choice = readSafeInt(scanner);

                switch (choice) {
                    case 1:
//                        roomController.searchAviableRooms();
                        break;
                    case 2:
//                        roomController.viewAllRooms();
                        break;
                    case 3:
//                        reservationController.makeReservation(scanner, currentUserId);
                        break;
                    case 4:
//                        reservationController.MyReservations(currentUserId);
                        break;
                    case 5:
//                        reservationController.updateReservation(scanner,currentUserId);
                        break;
                    case 6:
//                        reservationController.MyReservations(currentUserId);
//                        reservationController.cancelReservation(scanner);
                        break;
                    case 7:
//                        authController.updateProfile(scanner,currentUserId);
                        break;
                    case 9:
                        isLoggedIn = false;
//                        loggedInUser = null;
                        currentUserId = null;
                        System.out.println("\nLogged out successfully.\n");
                        break;
                    case 0:
                        running = false;
                        scheduler.shutdown(); // Cleanly shut down background thread on exit
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("\n--- Action " + choice + " executed ---\n");
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