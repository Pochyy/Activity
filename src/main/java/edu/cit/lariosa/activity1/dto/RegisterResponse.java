package edu.cit.lariosa.activity1.dto;

// Returned by /api/register. Never includes the password hash.
public class RegisterResponse {

    private Long id;
    private String username;

    public RegisterResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
