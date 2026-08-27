package edu.cit.lariosa.activity1.controller;

import edu.cit.lariosa.activity1.dto.ServiceRequestDto;
import edu.cit.lariosa.activity1.dto.ServiceRequestResponse;
import edu.cit.lariosa.activity1.service.ServiceRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    public ServiceRequestController(ServiceRequestService serviceRequestService) {
        this.serviceRequestService = serviceRequestService;
    }

    // The username never comes from a request param or body - only from the
    // Authentication object that JwtAuthenticationFilter populated from the
    // validated JWT. This is what makes ownership enforcement real rather
    // than cosmetic.
    private String currentUsername(Authentication authentication) {
        return authentication.getName();
    }

    @PostMapping
    public ResponseEntity<ServiceRequestResponse> create(Authentication authentication,
                                                           @RequestBody ServiceRequestDto dto) {
        return ResponseEntity.ok(serviceRequestService.create(currentUsername(authentication), dto));
    }

    @GetMapping
    public ResponseEntity<List<ServiceRequestResponse>> listMine(Authentication authentication) {
        return ResponseEntity.ok(serviceRequestService.listMine(currentUsername(authentication)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequestResponse> getOne(Authentication authentication,
                                                           @PathVariable Long id) {
        return ResponseEntity.ok(serviceRequestService.getOwned(currentUsername(authentication), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceRequestResponse> update(Authentication authentication,
                                                           @PathVariable Long id,
                                                           @RequestBody ServiceRequestDto dto) {
        return ResponseEntity.ok(serviceRequestService.update(currentUsername(authentication), id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable Long id) {
        serviceRequestService.delete(currentUsername(authentication), id);
        return ResponseEntity.noContent().build();
    }
}
