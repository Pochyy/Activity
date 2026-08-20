package edu.cit.lariosa.activity1.controller;

import edu.cit.lariosa.activity1.model.User;
import edu.cit.lariosa.activity1.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // POST /api/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Username already exists");
        }

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(savedUser);
    }

    // POST /api/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        Optional<User> existingUser =
                userRepository.findByUsername(user.getUsername());

        if (existingUser.isEmpty()) {
            return ResponseEntity.status(401)
                    .body("Invalid username or password");
        }

        User foundUser = existingUser.get();

        if (!foundUser.getPassword().equals(user.getPassword())) {
            return ResponseEntity.status(401)
                    .body("Invalid username or password");
        }

        return ResponseEntity.ok(foundUser);
    }

    // GET /api/user/{id}
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user.get());
    }
}
