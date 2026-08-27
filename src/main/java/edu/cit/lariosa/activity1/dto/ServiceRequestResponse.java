package edu.cit.lariosa.activity1.dto;

import edu.cit.lariosa.activity1.model.ServiceRequest;
import java.time.LocalDateTime;

// Response shape for all /api/requests endpoints.
public class ServiceRequestResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private LocalDateTime dateCreated;
    private String createdBy;

    public ServiceRequestResponse(ServiceRequest sr) {
        this.id = sr.getId();
        this.title = sr.getTitle();
        this.description = sr.getDescription();
        this.category = sr.getCategory();
        this.dateCreated = sr.getDateCreated();
        this.createdBy = sr.getCreatedBy().getUsername();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public String getCreatedBy() {
        return createdBy;
    }
}
