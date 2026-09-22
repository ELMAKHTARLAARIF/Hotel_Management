package Controller;

import model.RoomDomain;
import model.enums.RoomStatus;
import model.enums.RoomType;
import service.RoomService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    public void createRoom(Scanner scanner, UUID adminId) {

        try {

            System.out.println("\n------ CREATE ROOM ------");

            System.out.print("Room Number: ");

            String roomNumber = scanner.nextLine();

            System.out.println("Room Type:");

            System.out.println("1. SINGLE");

            System.out.println("2. DOUBLE");

            System.out.println("3. SUITE");

            System.out.print("Choice: ");

            int typeChoice = Integer.parseInt(scanner.nextLine());

            RoomType roomType = parseRoomType(typeChoice);

            System.out.print("Price Per Night: ");

            BigDecimal price = new BigDecimal(scanner.nextLine());

            System.out.print("Capacity: ");

            int capacity = Integer.parseInt(scanner.nextLine());

            RoomDomain room = roomService.createRoom(adminId, adminId, roomNumber, roomType, price, capacity);

            System.out.println("\nRoom created successfully!");

            System.out.println("Room ID: " + room.getId());

        } catch (IllegalArgumentException | IllegalStateException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listRooms(UUID adminId) {

        try {

            System.out.println("\n------ ALL ROOMS ------");

            List<RoomDomain> rooms = roomService.getAllRooms(adminId);

            if (rooms.isEmpty()) {

                System.out.println("No rooms found.");

                return;
            }

            for (RoomDomain room : rooms) {

                System.out.println("----------------------------");

                System.out.println("Room Number: " + room.getRoomNumber() + "  |  " + "Room Type: " + room.getRoomType() + "  |  " + "Price Per Night: " + room.getPricePerNight() + "  |  " + "Room Status: " + room.getRoomStatus() + "  |  " + "Capacity: " + room.getCapacity());

                System.out.println();

                System.out.println();

                System.out.println();

                System.out.println();
            }

            System.out.println("----------------------------");

        } catch (IllegalArgumentException | IllegalStateException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateRoom(Scanner scanner, UUID adminId) {

        try {

            System.out.println("\n------ UPDATE ROOM ------");

            System.out.print("Room Number: ");

            String roomNumber = scanner.nextLine().trim();

            RoomDomain room = roomService.getRoomByRoomNumber(adminId, roomNumber);

            if (room == null) {

                System.out.println("Room not found.");

                return;
            }

            System.out.println("Current Room Number: " + room.getRoomNumber());

            System.out.print("New Room Number " + "(blank = keep current): ");

            String newRoomNumber = scanner.nextLine().trim();

            if (newRoomNumber.isBlank()) {

                newRoomNumber = room.getRoomNumber();
            }

            System.out.println("Current Room Type: " + room.getRoomType());

            System.out.print("New Room Type " + "(SINGLE/DOUBLE/SUITE, blank = keep current): ");

            String roomTypeInput = scanner.nextLine().trim();

            RoomType roomType;

            if (roomTypeInput.isBlank()) {

                roomType = room.getRoomType();

            } else {

                roomType = RoomType.valueOf(roomTypeInput.toUpperCase());
            }

            System.out.println("Current Price: " + room.getPricePerNight());

            System.out.print("New Price " + "(blank = keep current): ");

            String priceInput = scanner.nextLine().trim();

            BigDecimal price;

            if (priceInput.isBlank()) {

                price = room.getPricePerNight();

            } else {

                price = new BigDecimal(priceInput);
            }

            System.out.println("Current Status: " + room.getRoomStatus());

            System.out.print("New Status " + "(AVAILABLE/OCCUPIED/MAINTENANCE, blank = keep current): ");

            String statusInput = scanner.nextLine().trim();

            RoomStatus roomStatus;

            if (statusInput.isBlank()) {

                roomStatus = room.getRoomStatus();

            } else {

                roomStatus = RoomStatus.valueOf(statusInput.toUpperCase());
            }

            System.out.println("Current Capacity: " + room.getCapacity());

            System.out.print("New Capacity " + "(blank = keep current): ");

            String capacityInput = scanner.nextLine().trim();

            int capacity;

            if (capacityInput.isBlank()) {

                capacity = room.getCapacity();

            } else {

                capacity = Integer.parseInt(capacityInput);
            }

            roomService.updateRoom(adminId, room.getId(), newRoomNumber, roomType, price, roomStatus, capacity);

            System.out.println("\nRoom updated successfully!");

        } catch (IllegalArgumentException | IllegalStateException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteRoom(Scanner scanner, UUID adminId) {

        try {

            System.out.println("\n------ DELETE ROOM ------");

            System.out.print("Room ID: ");

            UUID roomId = UUID.fromString(scanner.nextLine().trim());

            roomService.deleteRoom(adminId, roomId);

            System.out.println("\nRoom deleted successfully!");

        } catch (IllegalArgumentException | IllegalStateException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    private RoomType parseRoomType(int choice) {

        switch (choice) {

            case 1:
                return RoomType.SINGLE;

            case 2:
                return RoomType.DOUBLE;

            case 3:
                return RoomType.SUITE;

            default:
                throw new IllegalArgumentException("Invalid room type.");
        }
    }
}