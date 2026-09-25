package Controller;

import exception.InvalidReservationDateException;
import exception.RoomNotAvailableException;
import exception.RoomNotFoundException;
import model.ReservationDomain;
import model.RoomDomain;
import repository.IRoomRepository;
import service.ReservationService;

import javax.management.relation.RoleInfoNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public void makeReservation(Scanner scanner, UUID currentUserId) {
        System.out.println("------ Make a Reservation --------");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            System.out.print("Check-in Date (YYYY-MM-DD): ");
            LocalDate checkIn = LocalDate.parse(scanner.nextLine().trim(), dateFormatter);

            System.out.print("Check-out Date (YYYY-MM-DD): ");
            LocalDate checkOut = LocalDate.parse(scanner.nextLine().trim(), dateFormatter);

            System.out.print("Number Of Guests: ");
            int numberOfGuests = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Room Number: ");
            String roomNumber = scanner.nextLine().trim();

            reservationService.makeReservation(roomNumber, checkIn, checkOut, numberOfGuests, currentUserId);

        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid number for guests.");
        } catch (InvalidReservationDateException | RoomNotFoundException | RoomNotAvailableException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void accept(Scanner scanner) {

        System.out.println("ENTER RESERVATION CODE: ");

        String targetReservation = scanner.nextLine().trim();

        try {

            reservationService.accept(targetReservation);

            System.out.println("Reservation accepted successfully!");

        } catch (IllegalArgumentException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    public void myReservations(UUID userId) {
        try {
            List<ReservationDomain> reservations =  reservationService.myReservations(userId);
            for (var reservation : reservations){
                System.out.println("Checking date:"+reservation.getCheck_in()+"Check Out date"+reservation.getCheck_out()+"Number Of Guests: "+reservation.getNumberOfGuest()+"Reservation Code: "+reservation.getReservationCode()+"Reservation Status: "+reservation.getStatus()+"Total Price: "+ reservation.getTotal_amount()+"Created At: "+ reservation.getCreated_at());
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not retrieve reservations", e);
        }
    }
    public void cancel(){

    }


}
