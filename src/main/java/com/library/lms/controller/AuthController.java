package com.library.lms.controller;

import com.library.lms.model.User;
import com.library.lms.repository.UserRepository;
import com.library.lms.request.LoginRequest;
import com.library.lms.request.SignupRequest;
import com.library.lms.model.ERole;
import com.library.lms.model.Role;
import com.library.lms.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void insertDefaultRoles() {
        for (ERole roleName : ERole.values()) {
            if (!roleRepository.findByName(roleName).isPresent()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
                System.out.println("Inserted role: " + roleName);
            }
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        // user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setPassword(signupRequest.getPassword());
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setPhoneNumber(signupRequest.getPhoneNumber());
        user.setAddress(signupRequest.getAddress());

        Set<Role> roles = new HashSet<>();

        if (signupRequest.getRoles() == null || signupRequest.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByName(ERole.ROLE_MEMBER)
                    .orElseThrow(() -> new RuntimeException("Error: Default role not found."));
            roles.add(defaultRole);
        } else {
            for (String roleStr : signupRequest.getRoles()) {
                ERole roleEnum = ERole.valueOf("ROLE_" + roleStr.toUpperCase());
                Role role = roleRepository.findByName(roleEnum)
                        .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                roles.add(role);
            }
        }

        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully with roles: " + roles);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: User not found.");
        }

        User user = userOptional.get();

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Incorrect password.");
        }

        return ResponseEntity.ok("Login successful for user: " + user.getUsername());
    }

    // @PostMapping("/signin")
    // public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
    //     Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());

    //     if (userOpt.isPresent()) {
    //         User user = userOpt.get();
    //         if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
    //             return ResponseEntity.ok("Login successful for user: " + user.getUsername());
    //         } else {
    //             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
    //         }
    //     } else {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    //     }
    // }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
