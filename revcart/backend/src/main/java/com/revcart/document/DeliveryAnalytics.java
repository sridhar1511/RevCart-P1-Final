package com.revcart.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "delivery_analytics")
public class DeliveryAnalytics {
    @Id
    private String id;
    private Long agentId;
    private String agentName;
    private LocalDate date;
    private Integer totalDeliveries;
    private Integer successfulDeliveries;
    private Integer failedDeliveries;
    private Double averageDeliveryTime;
    private Double totalDistance;
    private Double averageRating;
    private Long lastUpdated;

    public DeliveryAnalytics() {}

    public DeliveryAnalytics(Long agentId, String agentName, LocalDate date) {
        this.agentId = agentId;
        this.agentName = agentName;
        this.date = date;
        this.totalDeliveries = 0;
        this.successfulDeliveries = 0;
        this.failedDeliveries = 0;
        this.averageDeliveryTime = 0.0;
        this.totalDistance = 0.0;
        this.averageRating = 0.0;
        this.lastUpdated = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Long getAgentId() { return agentId; }
    public void setAgentId(Long agentId) { this.agentId = agentId; }
    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Integer getTotalDeliveries() { return totalDeliveries; }
    public void setTotalDeliveries(Integer totalDeliveries) { this.totalDeliveries = totalDeliveries; }
    public Integer getSuccessfulDeliveries() { return successfulDeliveries; }
    public void setSuccessfulDeliveries(Integer successfulDeliveries) { this.successfulDeliveries = successfulDeliveries; }
    public Integer getFailedDeliveries() { return failedDeliveries; }
    public void setFailedDeliveries(Integer failedDeliveries) { this.failedDeliveries = failedDeliveries; }
    public Double getAverageDeliveryTime() { return averageDeliveryTime; }
    public void setAverageDeliveryTime(Double averageDeliveryTime) { this.averageDeliveryTime = averageDeliveryTime; }
    public Double getTotalDistance() { return totalDistance; }
    public void setTotalDistance(Double totalDistance) { this.totalDistance = totalDistance; }
    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
    public Long getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Long lastUpdated) { this.lastUpdated = lastUpdated; }
}
