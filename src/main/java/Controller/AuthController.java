package Controller;
import exception.EmailFormatException;
import exception.EmailAlreadyExistsException;
import exception.InvalidCredentialsException;
import model.UserDomain;
import service.AuthService;

import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public void register(Scanner scanner) {

        try {

            System.out.println("\n--- REGISTRATION ---");

            System.out.print("Full Name: ");

            String fullName = scanner.nextLine();

            System.out.print("Email: ");

            String email = scanner.nextLine();

            System.out.print("Password (min 8 chars): ");

            String password = scanner.nextLine();

            UUID userId = authService.registerUser(fullName, email, password);

            System.out.println("\nRegistration successful!");

            System.out.println("Your ID: " + userId);

            System.out.println();

        } catch (
                EmailAlreadyExistsException |
                EmailFormatException |
                IllegalArgumentException e
        ) {
            System.out.println("Error: " + e.getMessage());
            System.out.println();
        }
    }

    public UserDomain login(Scanner scanner) {

        try {

            System.out.println("\n------ LOGIN ------");

            System.out.print("Email: ");

            String email = scanner.nextLine();

            System.out.print("Password: ");

            String password = scanner.nextLine();

            UserDomain user = authService.login(email, password);

            System.out.println("\nLogin successful!");

            System.out.println("Welcome, " + user.getFullName());

            System.out.println("Role: " + user.getRole());

            System.out.println();

            return user;

        } catch (
                InvalidCredentialsException |
                EmailFormatException |
                IllegalArgumentException e
        ) {
            System.out.println("Error: " + e.getMessage());
            System.out.println();
            return null;
        }
    }

    public void updateProfile(Scanner scanner, UUID userId) {

        try {

            Optional<UserDomain> userOptional = authService.getUserById(userId);

            if (userOptional.isEmpty()) {

                System.out.println("Error: User not found.");

                return;
            }

            UserDomain user = userOptional.get();

            System.out.println("\n------ UPDATE PROFILE ------");

            System.out.println("Current Name: " + user.getFullName());

            System.out.print("New Full Name " + "(leave blank to keep current): ");

            String fullName = scanner.nextLine();

            if (fullName.trim().isEmpty()) {
                fullName = user.getFullName();
            }

            System.out.print("New Password " + "(leave blank to keep current): ");

            String password = scanner.nextLine();

            if (password.trim().isEmpty()) {
                password = null;
            }

            authService.updateProfile(userId, fullName, password);

            System.out.println("Profile updated successfully!");

            System.out.println();

        } catch (IllegalArgumentException e) {

            System.out.println("Error: " + e.getMessage());

            System.out.println();
        }
    }
}