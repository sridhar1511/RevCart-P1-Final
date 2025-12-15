package com.revcart.config;

import com.revcart.repository.CartItemRepository;
import com.revcart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataCleanup implements CommandLineRunner {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try {
            cartItemRepository.deleteAll();
            cartRepository.deleteAll();
            System.out.println("Old cart data cleaned up successfully");
        } catch (Exception e) {
            System.err.println("Error cleaning up cart data: " + e.getMessage());
        }
    }
}
