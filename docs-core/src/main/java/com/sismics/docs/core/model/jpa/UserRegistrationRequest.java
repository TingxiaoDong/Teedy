package com.sismics.docs.core.model.jpa;

import jakarta.persistence.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "USER_REGISTRATION_REQUEST")
public class UserRegistrationRequest {
    @Id
    @Column(name = "ID", length = 36)
    @JsonProperty("id")
    private String id;

    @Column(name = "USERNAME", length = 255, nullable = false, unique = true)
    @JsonProperty("username")
    private String username;

    @Column(name = "EMAIL", length = 255, nullable = false, unique = true)
    @JsonProperty("email")
    private String email;

    @Column(name = "PASSWORD", length = 255, nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "STATUS", length = 20, nullable = false)
    @JsonProperty("status")
    private String status; // e.g., pending, accepted, rejected

    @Column(name = "CREATE_TIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty("createTime")
    private Date createTime;

    @Column(name = "REQUEST_MESSAGE", columnDefinition = "TEXT")
    @JsonProperty("requestMessage")
    private String requestMessage;

    @Column(name = "ADMIN_REMARK", columnDefinition = "TEXT")
    @JsonProperty("adminRemark")
    private String adminRemark;

    public UserRegistrationRequest() {
    }

    public UserRegistrationRequest(String id, String username, String email, String password, String status, Date createTime) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
        this.createTime = createTime;
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getAdminRemark() {
        return adminRemark;
    }

    public void setAdminRemark(String adminRemark) {
        this.adminRemark = adminRemark;
    }
} 