package com.ldapauth.demo.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements Serializable{

    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "biography")
    private String biography;
    @Column(name = "created")
    @CreationTimestamp
    private Date created;

    @Column(name = "updated")
    @UpdateTimestamp
    private Date updated;
    @OneToOne(cascade=CascadeType.ALL)
    @JoinTable(name="user_roles",
            joinColumns = {@JoinColumn(name="username", referencedColumnName="username")},
            inverseJoinColumns = {@JoinColumn(name="role", referencedColumnName="role")}
    )

    private Role role;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="user_comments",
            joinColumns = {@JoinColumn(name="receiver", referencedColumnName="username")},
            inverseJoinColumns = {@JoinColumn(name="comment_id", referencedColumnName="comment_id")}
    )
    private Set<Comment> Comments;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="creator_groupe",
            joinColumns = {@JoinColumn(name="username", referencedColumnName="username")},
            inverseJoinColumns = {@JoinColumn(name="group_id", referencedColumnName="group_id")}
    )
    private Set<Groupe> myGroups;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="user_comment",
            joinColumns = {@JoinColumn(name="sender", referencedColumnName="username")},
            inverseJoinColumns = {@JoinColumn(name="comment_id", referencedColumnName="comment_id")}
    )
    private Set<Comment> myComments;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="user_group_comments",
            joinColumns = {@JoinColumn(name="username", referencedColumnName="username")},
            inverseJoinColumns = {@JoinColumn(name="group_comment_id", referencedColumnName="group_comment_id")}
    )
    private Set<GroupComment> myGroupComments;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="sender_invitations",
            joinColumns = {@JoinColumn(name="sender", referencedColumnName="username")},
            inverseJoinColumns = {@JoinColumn(name="invitation_id", referencedColumnName="invitation_id")}
    )
    private Set<Invitation> sendInvitations;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="receiver_invitations",
            joinColumns = {@JoinColumn(name="receiver", referencedColumnName="username")},
            inverseJoinColumns = {@JoinColumn(name="invitation_id", referencedColumnName="invitation_id")}
    )
    private Set<Invitation> receiverInvitations;

    public Set<Invitation> getSendInvitations() {
        return sendInvitations;
    }

    public void setSendInvitations(Set<Invitation> sendInvitations) {
        this.sendInvitations = sendInvitations;
    }

    public Set<Invitation> getReceiverInvitations() {
        return receiverInvitations;
    }

    public void setReceiverInvitations(Set<Invitation> receiverInvitations) {
        this.receiverInvitations = receiverInvitations;
    }

    public Set<GroupComment> getMyGroupComments() {
        return myGroupComments;
    }

    public void setMyGroupComments(Set<GroupComment> myGroupComments) {
        this.myGroupComments = myGroupComments;
    }

    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="users_personal_documents",
            joinColumns = {@JoinColumn(name="username", referencedColumnName="username")},
            inverseJoinColumns = {@JoinColumn(name="personal_document_id", referencedColumnName="personal_document_id")}
    )
    private Set<PersonalDocument> myPersonalDocuments;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="users_groups_documents",
            joinColumns = {@JoinColumn(name="username", referencedColumnName="username")},
            inverseJoinColumns = {@JoinColumn(name="document_id", referencedColumnName="document_id")}
    )
    private Set<GroupDocument> myGroupGroupDocuments;

    public Set<GroupDocument> getMyGroupGroupDocuments() {
        return myGroupGroupDocuments;
    }

    public void setMyGroupGroupDocuments(Set<GroupDocument> myGroupGroupDocuments) {
        this.myGroupGroupDocuments = myGroupGroupDocuments;
    }

    public Set<PersonalDocument> getMyPersonalDocuments() {
        return myPersonalDocuments;
    }

    public void setMyPersonalDocuments(Set<PersonalDocument> myPersonalDocuments) {
        this.myPersonalDocuments = myPersonalDocuments;
    }

    public void setMyGroups(Set<Groupe> myGroups) {
        this.myGroups = myGroups;
    }

    public Set<Groupe> getMyGroups() {
        return myGroups;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public Date getUpdated() {
        return updated;
    }

    public Date getCreated() {
        return created;
    }

    public String getUsername() {
        return username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Set<Comment> getMyComments() {
        return myComments;
    }

    public void setMyComments(Set<Comment> myComments) {
        this.myComments = myComments;
    }

    public Set<Comment> getComments() {
        return Comments;
    }

    public void setComments(Set<Comment> comments) {
        Comments = comments;
    }
}
