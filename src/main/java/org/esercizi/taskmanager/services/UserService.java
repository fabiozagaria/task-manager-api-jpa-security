package org.esercizi.taskmanager.services;

import org.esercizi.taskmanager.dto.UserRegistrationRequest;
import org.esercizi.taskmanager.dto.UserResponse;
import org.esercizi.taskmanager.models.User;
import org.esercizi.taskmanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(UserRegistrationRequest user) {
        User userEntity = new User(null, user.username(), user.password(), "USER");
        String rawPassword = userEntity.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        userEntity.setPassword(encodedPassword);

        User userSaved = userRepository.save(userEntity);

        return new UserResponse(userSaved.getId(), userSaved.getUsername(), userSaved.getRole());
    }
}
