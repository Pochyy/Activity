package edu.cit.lariosa.activity1.repository;

import edu.cit.lariosa.activity1.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

    // Only ever fetch requests scoped to a specific owner.
    List<ServiceRequest> findByCreatedBy_IdOrderByDateCreatedDesc(Long userId);

    // Fetching by (id AND ownerId) together is what prevents a user from
    // reading/editing/deleting another user's request by guessing an ID.
    Optional<ServiceRequest> findByIdAndCreatedBy_Id(Long id, Long userId);
}
