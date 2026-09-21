package service;

import exception.EmailAlreadyExistsException;
import exception.InvalidCredentialsException;
import model.UserDomain;
import model.enums.UserRole;
import repository.IUserRepository;
import util.PasswordUtil;

import java.util.Optional;
import java.util.UUID;

public class AuthService {

    private final IUserRepository iUserRepository;

    public AuthService(
            IUserRepository iUserRepository
    ) {
        this.iUserRepository =
                iUserRepository;
    }

    public UUID registerUser(
            String fullName,
            String email,
            String password
    ) {

        if (fullName == null ||
                fullName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Full name cannot be empty."
            );
        }

        if (email == null ||
                email.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Email cannot be empty."
            );
        }

        if (password == null ||
                password.length() < 8) {

            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters."
            );
        }

        email = email.trim().toLowerCase();

        UserDomain existingUser =
                iUserRepository.findByEmail(email);

        if (existingUser != null) {

            throw new EmailAlreadyExistsException(
                    "Email already exists."
            );
        }

        String salt =
                PasswordUtil.generateSalt();

        String passwordHash =
                PasswordUtil.hashPassword(
                        password,
                        salt
                );

        UserDomain newUser =
                new UserDomain(
                        fullName.trim(),
                        email,
                        passwordHash,
                        salt,
                        UserRole.CLIENT
                );

        return iUserRepository.create(newUser).getId();
    }

    public UserDomain login(
            String email,
            String password
    ) {

        if (email == null ||
                email.trim().isEmpty()) {

            throw new InvalidCredentialsException();
        }

        if (password == null ||
                password.isEmpty()) {

            throw new InvalidCredentialsException();
        }

        UserDomain user =
                iUserRepository.findByEmail(
                        email.trim().toLowerCase()
                );

        if (user == null) {

            throw new InvalidCredentialsException();
        }

        boolean passwordCorrect =
                PasswordUtil.verifyPassword(
                        password,
                        user.getSalt(),
                        user.getPasswordHash()
                );

        if (!passwordCorrect) {

            throw new InvalidCredentialsException();
        }

        return user;
    }

    public Optional<UserDomain> getUserById(
            UUID userId
    ) {

        return Optional.ofNullable(
                iUserRepository.findById(userId)
        );
    }

    public void updateProfile(
            UUID userId,
            String newFullName,
            String newPassword
    ) {

        UserDomain existingUser =
                iUserRepository.findById(userId);

        if (existingUser == null) {

            throw new IllegalArgumentException(
                    "User not found."
            );
        }

        if (newFullName != null &&
                !newFullName.trim().isEmpty()) {

            existingUser.setFullName(
                    newFullName.trim()
            );
        }

        if (newPassword != null &&
                !newPassword.isEmpty()) {

            if (newPassword.length() < 8) {

                throw new IllegalArgumentException(
                        "Password must contain at least 8 characters."
                );
            }

            String salt =
                    PasswordUtil.generateSalt();

            String passwordHash =
                    PasswordUtil.hashPassword(
                            newPassword,
                            salt
                    );

            existingUser.setSalt(salt);
            existingUser.setPasswordHash(
                    passwordHash
            );
        }

        iUserRepository.update(
                existingUser
        );
    }
}