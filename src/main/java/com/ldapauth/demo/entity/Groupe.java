package com.ldapauth.demo.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="groups_documents",
            joinColumns = {@JoinColumn(name="group_id", referencedColumnName="group_id")},
            inverseJoinColumns = {@JoinColumn(name="document_id", referencedColumnName="document_id")}
    )
    private Set<GroupDocument> groupGroupDocuments;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="group_user_comments",
            joinColumns = {@JoinColumn(name="group_id", referencedColumnName="group_id")},
            inverseJoinColumns = {@JoinColumn(name="group_comment_id", referencedColumnName="group_comment_id")}
    )
    private Set<GroupComment> groupComments;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="groupe_invitations",
            joinColumns = {@JoinColumn(name="group_id", referencedColumnName="group_id")},
            inverseJoinColumns = {@JoinColumn(name="invitation_id", referencedColumnName="invitation_id")}
    )
    private Set<Invitation> invitations;

    public Set<Invitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(Set<Invitation> invitations) {
        this.invitations = invitations;
    }

    public Set<GroupComment> getGroupComments() {
        return groupComments;
    }

    public void setGroupComments(Set<GroupComment> groupComments) {
        this.groupComments = groupComments;
    }

    public Set<GroupDocument> getGroupGroupDocuments() {
        return groupGroupDocuments;
    }

    public void setGroupGroupDocuments(Set<GroupDocument> groupGroupDocuments) {
        this.groupGroupDocuments = groupGroupDocuments;
    }

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
