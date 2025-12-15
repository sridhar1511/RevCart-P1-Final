package com.revcart.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

@Document(collection = "order_analytics")
public class OrderAnalytics {
    @Id
    private String id;
    private Long productId;
    private String productName;
    private String category;
    private Integer purchaseCount;
    private BigDecimal totalRevenue;
    private Long lastUpdated;

    public OrderAnalytics() {}

    public OrderAnalytics(Long productId, String productName, String category, Integer purchaseCount, BigDecimal totalRevenue) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.purchaseCount = purchaseCount;
        this.totalRevenue = totalRevenue;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getPurchaseCount() { return purchaseCount; }
    public void setPurchaseCount(Integer purchaseCount) { this.purchaseCount = purchaseCount; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    public Long getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Long lastUpdated) { this.lastUpdated = lastUpdated; }
}
