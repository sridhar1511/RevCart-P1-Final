package com.revcart.mongo;

import com.revcart.document.DeliveryAnalytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryAnalyticsRepository extends MongoRepository<DeliveryAnalytics, String> {
    Optional<DeliveryAnalytics> findByAgentIdAndDate(Long agentId, LocalDate date);
    List<DeliveryAnalytics> findByAgentId(Long agentId);
    List<DeliveryAnalytics> findByDate(LocalDate date);
    
    @Aggregation(pipeline = {
        "{ $match: { date: { $gte: ?0, $lte: ?1 } } }",
        "{ $group: { _id: '$agentId', totalDeliveries: { $sum: '$totalDeliveries' }, successfulDeliveries: { $sum: '$successfulDeliveries' }, avgRating: { $avg: '$averageRating' } } }",
        "{ $sort: { totalDeliveries: -1 } }"
    })
    List<Object> getAgentPerformance(LocalDate startDate, LocalDate endDate);
    
    @Aggregation(pipeline = {
        "{ $match: { date: ?0 } }",
        "{ $sort: { successfulDeliveries: -1 } }",
        "{ $limit: 10 }"
    })
    List<DeliveryAnalytics> getTopAgentsByDate(LocalDate date);
    
    @Aggregation(pipeline = {
        "{ $group: { _id: '$date', totalDeliveries: { $sum: '$totalDeliveries' }, successfulDeliveries: { $sum: '$successfulDeliveries' } } }",
        "{ $sort: { _id: -1 } }",
        "{ $limit: 30 }"
    })
    List<Object> getDailyDeliveryStats();
}
