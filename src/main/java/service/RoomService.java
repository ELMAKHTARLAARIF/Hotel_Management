package service;

import model.RoomDomain;
import model.UserDomain;
import model.enums.RoomStatus;
import model.enums.RoomType;
import model.enums.UserRole;
import repository.IUserRepository;
import repository.IRoomRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class RoomService {

    private final IRoomRepository roomRepository;
    private final IUserRepository userRepository;

    public RoomService(
            IRoomRepository roomRepository,
            IUserRepository userRepository
    ) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public RoomDomain createRoom(
            UUID adminId,
            UUID userId,
            String roomNumber,
            RoomType roomType,
            BigDecimal pricePerNight,
            int capacity
    ) {

        checkAdmin(adminId);
        validateUser(userId);
        validateRoomNumber(roomNumber);
        validateRoomType(roomType);
        validatePrice(pricePerNight);
        validateCapacity(capacity);

        RoomDomain existingRoom =
                roomRepository.findByRoomNumber(
                        roomNumber
                );

        if (existingRoom != null) {

            throw new IllegalArgumentException(
                    "Room number already exists."
            );
        }

        RoomDomain room =
                new RoomDomain(
                        userId,
                        roomNumber.trim(),
                        roomType,
                        pricePerNight,
                        RoomStatus.AVAILABLE,
                        capacity
                );

        return roomRepository.create(room);
    }

    public RoomDomain getRoomByRoomNumber(
            UUID adminId,
            String roomNumber
    ) {

        checkAdmin(adminId);

        return roomRepository.findByRoomNumber(roomNumber);
    }

    public List<RoomDomain> getAllRooms(
            UUID adminId
    ) {

        checkAdmin(adminId);

        return roomRepository.findAll();
    }

    public void updateRoom(
            UUID adminId,
            UUID roomId,
            String roomNumber,
            RoomType roomType,
            BigDecimal pricePerNight,
            RoomStatus roomStatus,
            int capacity
    ) {

        checkAdmin(adminId);

        validateRoomNumber(roomNumber);
        validateRoomType(roomType);
        validatePrice(pricePerNight);
        validateRoomStatus(roomStatus);
        validateCapacity(capacity);

        RoomDomain existingRoom =
                roomRepository.findById(roomId);

        if (existingRoom == null) {

            throw new IllegalArgumentException(
                    "Room not found."
            );
        }

        RoomDomain roomWithSameNumber =
                roomRepository.findByRoomNumber(
                        roomNumber
                );

        if (
                roomWithSameNumber != null &&
                        !roomWithSameNumber
                                .getId()
                                .equals(roomId)
        ) {

            throw new IllegalArgumentException(
                    "Room number already exists."
            );
        }

        existingRoom.setRoomNumber(
                roomNumber.trim()
        );

        existingRoom.setRoomType(
                roomType
        );

        existingRoom.setPricePerNight(
                pricePerNight
        );

        existingRoom.setRoomStatus(
                roomStatus
        );

        existingRoom.setCapacity(
                capacity
        );

        roomRepository.update(
                existingRoom
        );
    }

    public void deleteRoom(
            UUID adminId,
            UUID roomId
    ) {

        checkAdmin(adminId);

        RoomDomain room =
                roomRepository.findById(roomId);

        if (room == null) {

            throw new IllegalArgumentException(
                    "Room not found."
            );
        }

        roomRepository.delete(roomId);
    }

    private void checkAdmin(UUID userId) {

        UserDomain user =
                userRepository.findById(userId);

        if (user == null) {

            throw new IllegalArgumentException(
                    "User not found."
            );
        }

        if (user.getRole() != UserRole.ADMIN) {

            throw new IllegalStateException(
                    "Only administrators can manage rooms."
            );
        }
    }

    private void validateUser(UUID userId) {

        if (userId == null) {

            throw new IllegalArgumentException(
                    "User ID cannot be null."
            );
        }

        UserDomain user =
                userRepository.findById(userId);

        if (user == null) {

            throw new IllegalArgumentException(
                    "Assigned user does not exist."
            );
        }
    }

    private void validateRoomNumber(
            String roomNumber
    ) {

        if (
                roomNumber == null ||
                        roomNumber.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "Room number cannot be empty."
            );
        }
    }

    private void validateRoomType(
            RoomType roomType
    ) {

        if (roomType == null) {

            throw new IllegalArgumentException(
                    "Room type is required."
            );
        }
    }

    private void validatePrice(
            BigDecimal price
    ) {

        if (price == null) {

            throw new IllegalArgumentException(
                    "Price is required."
            );
        }

        if (
                price.compareTo(
                        BigDecimal.ZERO
                ) <= 0
        ) {

            throw new IllegalArgumentException(
                    "Price must be greater than zero."
            );
        }
    }

    private void validateRoomStatus(
            RoomStatus roomStatus
    ) {

        if (roomStatus == null) {

            throw new IllegalArgumentException(
                    "Room status is required."
            );
        }
    }

    private void validateCapacity(
            int capacity
    ) {

        if (capacity <= 0) {

            throw new IllegalArgumentException(
                    "Capacity must be greater than zero."
            );
        }
    }
}