package com.kambovski.ibproject.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    private String embg;  // unique identifier for the user (like an ID)

    private String name;

    @Enumerated(EnumType.STRING)
    private UserType userType;  // Role of the user: ADMIN or CUSTOMER

    // Getters and Setters
    public String getEmbg() {
        return embg;
    }

    public void setEmbg(String embg) {
        this.embg = embg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
