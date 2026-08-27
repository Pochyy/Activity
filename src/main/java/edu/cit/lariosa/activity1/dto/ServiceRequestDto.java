package edu.cit.lariosa.activity1.dto;

// Request body for POST /api/requests and PUT /api/requests/{id}.
// Deliberately has NO userId / createdBy field: ownership is never
// accepted from the client, only derived from the JWT on the backend.
public class ServiceRequestDto {

    private String title;
    private String description;
    private String category;

    public ServiceRequestDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
