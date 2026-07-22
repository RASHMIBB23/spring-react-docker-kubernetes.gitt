package com.example.demo.data;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class UserRoleId implements Serializable {

    private Long user_id;
    private Long role_id;

    public UserRoleId() {}

    public UserRoleId(Long user_id, Long role_id) {
        this.user_id = user_id;
        this.role_id = role_id;
    }

    // Getters and setters
    public Long getUserId() { return user_id; }
    public void setUserId(Long user_id) { this.user_id = user_id; }

    public Long getRoleId() { return role_id; }
    public void setRoleId(Long role_id) { this.role_id = role_id; }

    // hashCode and equals methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleId that = (UserRoleId) o;
        return Objects.equals(user_id, that.user_id) &&
               Objects.equals(role_id, that.role_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, role_id);
    }
}
