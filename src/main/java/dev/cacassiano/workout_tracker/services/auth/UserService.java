package dev.cacassiano.workout_tracker.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.cacassiano.workout_tracker.DTOs.auth.UserRequestDTO;
import dev.cacassiano.workout_tracker.entities.User;
import dev.cacassiano.workout_tracker.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public void register(UserRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        user.update(request);
        
        userRepository.save(user);
    }

    public String authenticate(UserRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("invalid credentials");
        }

        return jwtService.generateToken(user.getEmail(), user.getId());
    }

    public User getUserReferenceById(String id){
        return userRepository.getReferenceById(id);
    }
}
