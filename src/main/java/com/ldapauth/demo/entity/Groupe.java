package com.ldapauth.demo.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "groupe")
public class Groupe implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "group_id")
    private Long id;

    @Column(name = "group_name")
    @NotEmpty(message = "Not null")
    @Size(min = 5,message = "minimum length 5")
    private String groupName;

    @Column(name = "created")
    @CreationTimestamp
    private Date created;

    @Column(name = "updated")
    @UpdateTimestamp
    private Date updated;
    @OneToOne(cascade=CascadeType.ALL)
    @JoinTable(name="creator_groupe",
            joinColumns = {@JoinColumn(name="group_id", referencedColumnName="group_id")},
            inverseJoinColumns = {@JoinColumn(name="username", referencedColumnName="username")}
    )
    private User creator;

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getCreated() {
        return created;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getGroupName() {
        return groupName;
    }

    public Long getId() {
        return id;
    }

    public Date getUpdated() {
        return updated;
    }

}
