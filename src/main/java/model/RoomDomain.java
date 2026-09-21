package model;

import model.enums.RoomStatus;
import model.enums.RoomType;

import java.math.BigDecimal;
import java.util.UUID;

public class RoomDomain {

    private UUID id;
    private UUID userId;
    private String roomNumber;
    private RoomType roomType;
    private BigDecimal pricePerNight;
    private RoomStatus roomStatus;
    private int capacity;

    public RoomDomain() {
    }

    public RoomDomain(
            UUID userId,
            String roomNumber,
            RoomType roomType,
            BigDecimal pricePerNight,
            RoomStatus roomStatus,
            int capacity
    ) {
        this.userId = userId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.roomStatus = roomStatus;
        this.capacity = capacity;
    }

    public RoomDomain(
            UUID id,
            UUID userId,
            String roomNumber,
            RoomType roomType,
            BigDecimal pricePerNight,
            RoomStatus roomStatus,
            int capacity
    ) {
        this.id = id;
        this.userId = userId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.roomStatus = roomStatus;
        this.capacity = capacity;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "RoomDomain{" +
                "id=" + id +
                ", userId=" + userId +
                ", roomNumber='" + roomNumber + '\'' +
                ", roomType=" + roomType +
                ", pricePerNight=" + pricePerNight +
                ", roomStatus=" + roomStatus +
                ", capacity=" + capacity +
                '}';
    }
}