package com.template.api.dto.response;

/**
 * What gets sent back to the API consumer.
 * Only expose fields the client needs — never your full internal model.
 */
public class ExampleResponse {

    private Long id;
    private String name;
    private String email;

    public ExampleResponse() {}

    public ExampleResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
