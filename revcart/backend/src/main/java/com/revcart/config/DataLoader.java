package com.revcart.config;

import com.revcart.entity.Product;
import com.revcart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            loadProducts();
        }
    }

    private void loadProducts() {
        String[][] products = {
            {"Apple", "fruits", "50", "kg", "https://via.placeholder.com/200?text=Apple", "Fresh red apples"},
            {"Banana", "fruits", "30", "kg", "https://via.placeholder.com/200?text=Banana", "Yellow bananas"},
            {"Orange", "fruits", "40", "kg", "https://via.placeholder.com/200?text=Orange", "Fresh oranges"},
            {"Mango", "fruits", "60", "kg", "https://via.placeholder.com/200?text=Mango", "Sweet mangoes"},
            {"Grapes", "fruits", "80", "kg", "https://via.placeholder.com/200?text=Grapes", "Green grapes"},
            {"Carrot", "vegetables", "25", "kg", "https://via.placeholder.com/200?text=Carrot", "Fresh carrots"},
            {"Tomato", "vegetables", "20", "kg", "https://via.placeholder.com/200?text=Tomato", "Ripe tomatoes"},
            {"Onion", "vegetables", "15", "kg", "https://via.placeholder.com/200?text=Onion", "White onions"},
            {"Potato", "vegetables", "18", "kg", "https://via.placeholder.com/200?text=Potato", "Fresh potatoes"},
            {"Broccoli", "vegetables", "35", "kg", "https://via.placeholder.com/200?text=Broccoli", "Green broccoli"},
            {"Milk", "dairy", "45", "liter", "https://via.placeholder.com/200?text=Milk", "Fresh milk"},
            {"Cheese", "dairy", "120", "kg", "https://via.placeholder.com/200?text=Cheese", "Cheddar cheese"},
            {"Yogurt", "dairy", "60", "kg", "https://via.placeholder.com/200?text=Yogurt", "Plain yogurt"},
            {"Butter", "dairy", "150", "kg", "https://via.placeholder.com/200?text=Butter", "Salted butter"},
            {"Bread", "bakery", "40", "piece", "https://via.placeholder.com/200?text=Bread", "Whole wheat bread"},
            {"Cake", "bakery", "200", "piece", "https://via.placeholder.com/200?text=Cake", "Chocolate cake"},
            {"Cookies", "bakery", "80", "pack", "https://via.placeholder.com/200?text=Cookies", "Chocolate cookies"},
            {"Donut", "bakery", "50", "piece", "https://via.placeholder.com/200?text=Donut", "Glazed donuts"},
            {"Laptop", "electronics", "50000", "piece", "https://via.placeholder.com/200?text=Laptop", "Dell laptop"},
            {"Phone", "electronics", "30000", "piece", "https://via.placeholder.com/200?text=Phone", "iPhone 13"},
            {"Headphones", "electronics", "5000", "piece", "https://via.placeholder.com/200?text=Headphones", "Wireless headphones"},
            {"Charger", "electronics", "1500", "piece", "https://via.placeholder.com/200?text=Charger", "USB charger"},
            {"Football", "sports", "800", "piece", "https://via.placeholder.com/200?text=Football", "Professional football"},
            {"Basketball", "sports", "1200", "piece", "https://via.placeholder.com/200?text=Basketball", "Basketball"},
            {"Tennis Racket", "sports", "3000", "piece", "https://via.placeholder.com/200?text=Racket", "Tennis racket"},
            {"Doll", "kids", "500", "piece", "https://via.placeholder.com/200?text=Doll", "Baby doll"},
            {"Toy Car", "kids", "400", "piece", "https://via.placeholder.com/200?text=Car", "Remote control car"},
            {"Puzzle", "kids", "300", "piece", "https://via.placeholder.com/200?text=Puzzle", "1000 piece puzzle"},
            {"Lipstick", "beauty", "300", "piece", "https://via.placeholder.com/200?text=Lipstick", "Red lipstick"},
            {"Face Cream", "beauty", "500", "piece", "https://via.placeholder.com/200?text=Cream", "Moisturizing cream"},
            {"Shampoo", "beauty", "250", "bottle", "https://via.placeholder.com/200?text=Shampoo", "Hair shampoo"},
            {"Shirt", "mens-clothing", "800", "piece", "https://via.placeholder.com/200?text=Shirt", "Cotton shirt"},
            {"Jeans", "mens-clothing", "1500", "piece", "https://via.placeholder.com/200?text=Jeans", "Blue jeans"},
            {"Jacket", "mens-clothing", "2500", "piece", "https://via.placeholder.com/200?text=Jacket", "Winter jacket"},
            {"Dress", "womens-clothing", "1200", "piece", "https://via.placeholder.com/200?text=Dress", "Summer dress"},
            {"Saree", "womens-clothing", "2000", "piece", "https://via.placeholder.com/200?text=Saree", "Cotton saree"},
            {"Top", "womens-clothing", "600", "piece", "https://via.placeholder.com/200?text=Top", "Casual top"},
            {"Kids Shirt", "kids-clothing", "400", "piece", "https://via.placeholder.com/200?text=KShirt", "Kids t-shirt"},
            {"Kids Pants", "kids-clothing", "600", "piece", "https://via.placeholder.com/200?text=KPants", "Kids pants"}
        };

        for (String[] p : products) {
            Product product = new Product(
                p[0],
                p[1],
                new BigDecimal(p[2]),
                p[3],
                p[4],
                p[5]
            );
            productRepository.save(product);
        }
    }
}
