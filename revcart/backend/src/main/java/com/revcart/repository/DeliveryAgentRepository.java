package com.revcart.repository;

import com.revcart.entity.DeliveryAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, Long> {
    Optional<DeliveryAgent> findByEmail(String email);
    boolean existsByEmail(String email);
    List<DeliveryAgent> findByStatus(DeliveryAgent.Status status);
}