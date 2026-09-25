package repository;

import model.RoomDomain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRoomRepository {

    RoomDomain create(RoomDomain room);

    RoomDomain findById(UUID id);

    Optional<RoomDomain> findByRoomNumber(String roomNumber);

    List<RoomDomain> findAll();

    void update(RoomDomain room);

    void delete(UUID id);
}