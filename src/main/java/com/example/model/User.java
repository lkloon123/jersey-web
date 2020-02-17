package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.mindrot.jbcrypt.BCrypt;

import java.beans.ConstructorProperties;
import java.security.Principal;


public class User implements Principal {
    public final static String[] ROLE_LIST = new String[]{"user", "manager", "admin"};

    @JsonProperty("id")
    private int id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phone")
    private String phone;
    @JsonIgnore()
    private String password;
    @JsonProperty("department")
    private String department;
    @JsonProperty("role")
    private String role;

    public User() {
    }

    @ConstructorProperties({"id", "email", "phone", "password", "department", "role"})
    public User(int id, String email, String phone, String password, String department, String role) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.department = department;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void hashPassword() {
        this.password = BCrypt.hashpw(this.password, BCrypt.gensalt());
    }

    public boolean comparePassword(String password) {
        if (this.password == null) {
            return false;
        }

        return BCrypt.checkpw(password, this.password);
    }

    @Override
    @JsonIgnore
    public String getName() {
        return "" + this.id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", department='" + department + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
