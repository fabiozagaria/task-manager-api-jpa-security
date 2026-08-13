package org.esercizi.taskmanager.services;

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

    public User createUser(User user) {
        String pswUser = user.getPassword();
        String pswHash = passwordEncoder.encode(pswUser);
        user.setPassword(pswHash);

        return  userRepository.save(user);
    }
}
