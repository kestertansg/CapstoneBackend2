package com.library.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class LibraryManagementSystemApplication {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementSystemApplication.class, args);
    }

    @PostConstruct
    public void testPasswordMatch() {
        String raw = "adminpass";
        String encoded = "$2a$10$NwSk6vsmjFpI59ba4.8XVO8VF3NmlxXBEaXgFJTNqJ/sdVCiZixxe";
        System.out.println("Password matches: " + passwordEncoder.matches(raw, encoded));
    }
}
