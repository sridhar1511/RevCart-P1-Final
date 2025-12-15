package com.revcart.mongo;

import com.revcart.document.DeliveryLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeliveryLogRepository extends MongoRepository<DeliveryLog, String> {
    List<DeliveryLog> findByOrderIdOrderByTimestampDesc(Long orderId);
    List<DeliveryLog> findByDeliveryAgentIdOrderByTimestampDesc(Long deliveryAgentId);
    List<DeliveryLog> findByDeliveryAgentIdAndTimestampBetween(Long agentId, LocalDateTime start, LocalDateTime end);
    
    default List<DeliveryLog> findByDeliveryAgentIdAndDate(Long agentId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return findByDeliveryAgentIdAndTimestampBetween(agentId, startOfDay, endOfDay);
    }
}
