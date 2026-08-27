package edu.cit.lariosa.activity1.service;

import edu.cit.lariosa.activity1.dto.ServiceRequestDto;
import edu.cit.lariosa.activity1.dto.ServiceRequestResponse;
import edu.cit.lariosa.activity1.model.ServiceRequest;
import edu.cit.lariosa.activity1.model.User;
import edu.cit.lariosa.activity1.repository.ServiceRequestRepository;
import edu.cit.lariosa.activity1.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServiceRequestService {

    private final ServiceRequestRepository serviceRequestRepository;
    private final UserRepository userRepository;

    public ServiceRequestService(ServiceRequestRepository serviceRequestRepository,
                                  UserRepository userRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.userRepository = userRepository;
    }

    // Resolves the authenticated username (from SecurityContext / JWT) to a User row.
    private User resolveCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    public ServiceRequestResponse create(String username, ServiceRequestDto dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required");
        }

        User currentUser = resolveCurrentUser(username);

        ServiceRequest sr = new ServiceRequest();
        sr.setTitle(dto.getTitle());
        sr.setDescription(dto.getDescription());
        sr.setCategory(dto.getCategory());
        sr.setCreatedBy(currentUser); // ownership set server-side only

        return new ServiceRequestResponse(serviceRequestRepository.save(sr));
    }

    public List<ServiceRequestResponse> listMine(String username) {
        User currentUser = resolveCurrentUser(username);

        return serviceRequestRepository.findByCreatedBy_IdOrderByDateCreatedDesc(currentUser.getId())
                .stream()
                .map(ServiceRequestResponse::new)
                .toList();
    }

    public ServiceRequestResponse getOwned(String username, Long id) {
        User currentUser = resolveCurrentUser(username);
        ServiceRequest sr = findOwnedOrThrow(id, currentUser.getId());
        return new ServiceRequestResponse(sr);
    }

    public ServiceRequestResponse update(String username, Long id, ServiceRequestDto dto) {
        User currentUser = resolveCurrentUser(username);
        ServiceRequest sr = findOwnedOrThrow(id, currentUser.getId());

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            sr.setTitle(dto.getTitle());
        }
        sr.setDescription(dto.getDescription());
        sr.setCategory(dto.getCategory());

        return new ServiceRequestResponse(serviceRequestRepository.save(sr));
    }

    public void delete(String username, Long id) {
        User currentUser = resolveCurrentUser(username);
        ServiceRequest sr = findOwnedOrThrow(id, currentUser.getId());
        serviceRequestRepository.delete(sr);
    }

    // Central ownership check. Returns 404 (not 403) when the request exists
    // but belongs to someone else, so callers can't distinguish "not found"
    // from "not yours" - avoids leaking which IDs exist.
    private ServiceRequest findOwnedOrThrow(Long id, Long userId) {
        return serviceRequestRepository.findByIdAndCreatedBy_Id(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service request not found"));
    }
}
