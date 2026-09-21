package repository;

import model.UserDomain;

import java.util.List;
import java.util.UUID;

public interface IUserRepository {

    UserDomain create(UserDomain user);

    UserDomain findById(UUID id);

    UserDomain findByEmail(String email);

    List<UserDomain> findAll();

    void update(UserDomain user);

    void delete(UUID id);
}