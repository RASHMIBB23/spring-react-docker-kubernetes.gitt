package com.example.demo.data;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "users_roles")
public class Users_Roles {

    @EmbeddedId
    private UserRoleId id;

    public UserRoleId getId() { return id; }
    public void setId(UserRoleId id) { this.id = id; }

    // Convenience methods
    public Long getUserId() { return id != null ? id.getUserId() : null; }
    public void setUserId(Long user_id) {
        if (id == null) {
            id = new UserRoleId();
        }
        id.setUserId(user_id);
    }

    public Long getRoleId() { return id != null ? id.getRoleId() : null; }
    public void setRoleId(Long role_id) {
        if (id == null) {
            id = new UserRoleId();
        }
        id.setRoleId(role_id);
    }
}
