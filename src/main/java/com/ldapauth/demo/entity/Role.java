package com.ldapauth.demo.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="roles")
public class Role {

    @Id
    @Column(name = "role")
    private String role;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="user_roles",
            joinColumns = {@JoinColumn(name="role", referencedColumnName="role")},
            inverseJoinColumns = {@JoinColumn(name="username", referencedColumnName="username")}
    )
    private Set<User> userRoles;


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<User> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<User> userRoles) {
        this.userRoles = userRoles;
    }

}
