package com.library.lms.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.library.lms.model.ERole;
import com.library.lms.model.Role;
import com.library.lms.model.User;
import com.library.lms.repository.UserRepository;
import com.library.lms.security.service.UserDetailsImpl;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;
    
    @Test
    void loadUserByUsername_ShouldReturnUserDetailsForMember() {
        // Arrange
        User member = createTestUser("member1", "member@library.com", "password123", 
                                   "John", "Doe", "1234567890", "123 Library St");
        member.setRoles(Set.of(new Role(ERole.ROLE_MEMBER)));
        
        when(userRepository.findByUsername("member1")).thenReturn(Optional.of(member));
        
        // Act
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("member1");
        
        // Assert
        assertNotNull(userDetails);
        assertEquals("member1", userDetails.getUsername());
        assertEquals("member@library.com", userDetails.getEmail());
        assertEquals("password123", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("ROLE_MEMBER", userDetails.getAuthorities().iterator().next().getAuthority());
    }
    
    @Test
    void loadUserByUsername_ShouldWorkForLibrarian() {
        // Arrange
        User librarian = createTestUser("lib1", "librarian@library.com", "libpass", 
                                     "Jane", "Smith", "9876543210", "456 Library Ave");
        librarian.setRoles(Set.of(new Role(ERole.ROLE_LIBRARIAN)));
        
        when(userRepository.findByUsername("lib1")).thenReturn(Optional.of(librarian));
        
        // Act
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("lib1");
        
        // Assert
        assertNotNull(userDetails);
        assertEquals("ROLE_LIBRARIAN", userDetails.getAuthorities().iterator().next().getAuthority());
    }
    
    @Test
    void loadUserByUsername_ShouldWorkForAdminWithMultipleRoles() {
        User admin = createTestUser("admin1", "admin@library.com", "adminpass", 
                                "Admin", "User", "5551234567", "789 Admin Blvd");

        // Assign two distinct roles
        Set<Role> roles = new HashSet<>();
        Role roleAdmin = new Role(ERole.ROLE_ADMIN);
        roleAdmin.setId(1);
        Role roleLibrarian = new Role(ERole.ROLE_LIBRARIAN);
        roleLibrarian.setId(2);
        roles.add(roleAdmin);
        roles.add(roleLibrarian);

        admin.setRoles(roles);

        when(userRepository.findByUsername("admin1")).thenReturn(Optional.of(admin));

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("admin1");

        assertNotNull(userDetails);
        assertEquals(2, userDetails.getAuthorities().size());

        assertTrue(userDetails.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));

        assertTrue(userDetails.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_LIBRARIAN")));
    }

    @Test
    void loadUserByUsername_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        
        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("unknown");
        });
        assertEquals("User Not Found with username: unknown", exception.getMessage());
    }
    
    @Test
    void loadUserByUsername_ShouldIncludeAllUserDetails() {
        // Arrange
        User user = createTestUser("fulluser", "user@library.com", "userpass", 
                                "Alice", "Johnson", "1112223333", "321 User Lane");
        user.setMembershipDate(LocalDate.now().minusMonths(1));
        user.setMembershipExpiryDate(LocalDate.now().plusYears(1));
        user.setRoles(Set.of(new Role(ERole.ROLE_MEMBER)));
        
        when(userRepository.findByUsername("fulluser")).thenReturn(Optional.of(user));
        
        // Act
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("fulluser");
        
        // Assert
        assertNotNull(userDetails);
        assertEquals("Alice", userDetails.getFirstName());
        assertEquals("Johnson", userDetails.getLastName());
        assertEquals("1112223333", userDetails.getPhoneNumber());
        assertEquals("321 User Lane", userDetails.getAddress());
        assertNotNull(userDetails.getMembershipDate());
        assertNotNull(userDetails.getMembershipExpiryDate());
    }
    
    private User createTestUser(String username, String email, String password,
                              String firstName, String lastName,
                              String phoneNumber, String address) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setAddress(address);
        return user;
    }
}