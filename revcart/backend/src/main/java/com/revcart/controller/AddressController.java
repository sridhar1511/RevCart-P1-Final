package com.revcart.controller;

import com.revcart.entity.Address;
import com.revcart.entity.User;
import com.revcart.repository.AddressRepository;
import com.revcart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "*")
public class AddressController {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Address>> getUserAddresses(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.badRequest().build();
        
        List<Address> addresses = addressRepository.findByUserOrderByIsDefaultDescIdAsc(user);
        return ResponseEntity.ok(addresses);
    }

    @PostMapping
    public ResponseEntity<Address> saveAddress(@RequestBody Address address, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.badRequest().build();
        
        address.setUser(user);
        
        // If this is the first address, make it default
        List<Address> existingAddresses = addressRepository.findByUserOrderByIsDefaultDescIdAsc(user);
        if (existingAddresses.isEmpty()) {
            address.setIsDefault(true);
        }
        
        Address saved = addressRepository.save(address);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, @RequestBody Address address, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.badRequest().build();
        
        Address existing = addressRepository.findById(id).orElse(null);
        if (existing == null || !existing.getUser().getId().equals(user.getId())) {
            return ResponseEntity.notFound().build();
        }
        
        existing.setFullName(address.getFullName());
        existing.setPhone(address.getPhone());
        existing.setLine1(address.getLine1());
        existing.setLine2(address.getLine2());
        existing.setCity(address.getCity());
        existing.setState(address.getState());
        existing.setPincode(address.getPincode());
        
        Address updated = addressRepository.save(existing);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/default")
    public ResponseEntity<Void> setDefaultAddress(@PathVariable Long id, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.badRequest().build();
        
        Address address = addressRepository.findById(id).orElse(null);
        if (address == null || !address.getUser().getId().equals(user.getId())) {
            return ResponseEntity.notFound().build();
        }
        
        // Clear all default addresses for user
        addressRepository.clearDefaultAddresses(user);
        
        // Set this address as default
        address.setIsDefault(true);
        addressRepository.save(address);
        
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.badRequest().build();
        
        Address address = addressRepository.findById(id).orElse(null);
        if (address == null || !address.getUser().getId().equals(user.getId())) {
            return ResponseEntity.notFound().build();
        }
        
        addressRepository.delete(address);
        return ResponseEntity.ok().build();
    }
}