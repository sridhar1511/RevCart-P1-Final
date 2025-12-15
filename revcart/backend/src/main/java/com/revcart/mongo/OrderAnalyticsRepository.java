package com.revcart.mongo;

import com.revcart.document.OrderAnalytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderAnalyticsRepository extends MongoRepository<OrderAnalytics, String> {
    Optional<OrderAnalytics> findByProductId(Long productId);
    
    @Aggregation(pipeline = {
        "{ $sort: { purchaseCount: -1 } }",
        "{ $limit: 10 }"
    })
    List<OrderAnalytics> findTopPurchasedItems();
    
    @Aggregation(pipeline = {
        "{ $match: { category: ?0 } }",
        "{ $sort: { purchaseCount: -1 } }",
        "{ $limit: 5 }"
    })
    List<OrderAnalytics> findTopItemsByCategory(String category);
    
    @Aggregation(pipeline = {
        "{ $group: { _id: '$category', totalRevenue: { $sum: '$totalRevenue' }, count: { $sum: 1 } } }",
        "{ $sort: { totalRevenue: -1 } }"
    })
    List<Object> findTopCategoriesByRevenue();
}
