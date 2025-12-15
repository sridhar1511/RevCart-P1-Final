package com.revcart.controller;

import com.revcart.dto.AddressRequest;
import com.revcart.entity.Address;
import com.revcart.entity.User;
import com.revcart.repository.AddressRepository;
import com.revcart.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        try {
            User user = getUserFromAuthentication(authentication);
            return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "firstName", user.getFirstName() != null ? user.getFirstName() : "",
                "lastName", user.getLastName() != null ? user.getLastName() : "",
                "email", user.getEmail(),
                "phone", user.getPhone() != null ? user.getPhone() : "",
                "address", user.getAddress() != null ? user.getAddress() : "",
                "profilePicture", user.getProfilePicture(),
                "role", user.getRole().toString()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error fetching profile"));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody Map<String, String> updates, 
                                              Authentication authentication) {
        try {
            User user = getUserFromAuthentication(authentication);
            
            if (updates.containsKey("firstName")) {
                user.setFirstName(updates.get("firstName"));
            }
            if (updates.containsKey("lastName")) {
                user.setLastName(updates.get("lastName"));
            }
            if (updates.containsKey("phone")) {
                user.setPhone(updates.get("phone"));
            }
            if (updates.containsKey("profilePicture")) {
                user.setProfilePicture(updates.get("profilePicture"));
            }
            if (updates.containsKey("address")) {
                user.setAddress(updates.get("address"));
            }
            
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "Profile updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error updating profile"));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> passwordData,
                                           Authentication authentication) {
        try {
            User user = getUserFromAuthentication(authentication);
            String currentPassword = passwordData.get("currentPassword");
            String newPassword = passwordData.get("newPassword");
            
            if (!encoder.matches(currentPassword, user.getPassword())) {
                return ResponseEntity.badRequest().body(Map.of("message", "Current password is incorrect"));
            }
            
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
            
            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error changing password"));
        }
    }

    @GetMapping("/addresses")
    public ResponseEntity<?> getUserAddresses(Authentication authentication) {
        try {
            User user = getUserFromAuthentication(authentication);
            List<Address> addresses = addressRepository.findByUserOrderByIsDefaultDescIdAsc(user);
            return ResponseEntity.ok(addresses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error fetching addresses"));
        }
    }

    @PostMapping("/addresses")
    public ResponseEntity<?> addAddress(@Valid @RequestBody AddressRequest addressRequest,
                                       Authentication authentication) {
        try {
            User user = getUserFromAuthentication(authentication);
            
            if (addressRequest.getIsDefault()) {
                addressRepository.findByUserAndIsDefaultTrue(user)
                    .ifPresent(addr -> {
                        addr.setIsDefault(false);
                        addressRepository.save(addr);
                    });
            }
            
            Address address = new Address();
            address.setUser(user);
            address.setFullName(addressRequest.getName());
            address.setLine1(addressRequest.getAddressLine());
            address.setCity(addressRequest.getCity());
            address.setState(addressRequest.getState());
            address.setPincode(addressRequest.getPincode());
            address.setPhone(addressRequest.getPhone());
            address.setIsDefault(addressRequest.getIsDefault());
            
            addressRepository.save(address);
            return ResponseEntity.ok(Map.of("message", "Address added successfully", "address", address));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error adding address"));
        }
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<?> updateAddress(@PathVariable Long addressId,
                                          @Valid @RequestBody AddressRequest addressRequest,
                                          Authentication authentication) {
        try {
            User user = getUserFromAuthentication(authentication);
            Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
            
            if (!address.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied"));
            }
            
            if (addressRequest.getIsDefault()) {
                addressRepository.findByUserAndIsDefaultTrue(user)
                    .ifPresent(addr -> {
                        if (!addr.getId().equals(addressId)) {
                            addr.setIsDefault(false);
                            addressRepository.save(addr);
                        }
                    });
            }
            
            address.setFullName(addressRequest.getName());
            address.setLine1(addressRequest.getAddressLine());
            address.setCity(addressRequest.getCity());
            address.setState(addressRequest.getState());
            address.setPincode(addressRequest.getPincode());
            address.setPhone(addressRequest.getPhone());
            address.setIsDefault(addressRequest.getIsDefault());
            
            addressRepository.save(address);
            return ResponseEntity.ok(Map.of("message", "Address updated successfully", "address", address));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error updating address"));
        }
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long addressId, Authentication authentication) {
        try {
            User user = getUserFromAuthentication(authentication);
            Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
            
            if (!address.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied"));
            }
            
            addressRepository.delete(address);
            return ResponseEntity.ok(Map.of("message", "Address deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error deleting address"));
        }
    }

    private User getUserFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
