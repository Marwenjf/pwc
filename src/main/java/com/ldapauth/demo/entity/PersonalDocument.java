package com.ldapauth.demo.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name = "personal_document")
public class PersonalDocument implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "personal_document_id")
    private Long personalDocumentId;
    @Column(name = "personal_document_name")
    private String personalDocumentName;
    @Column(name = "personal_document_visibility")
    private String personalDocumentVisibility;
    @Column(name = "personal_document_description")
    private String personalDocumentDescription;
    @Column(name = "created")
    @CreationTimestamp
    private Date created;
    @Column(name = "updated")
    @UpdateTimestamp
    private Date updated;
    @OneToOne(cascade=CascadeType.ALL)
    @JoinTable(name="users_personal_documents",
            joinColumns = {@JoinColumn(name="personal_document_id", referencedColumnName="personal_document_id")},
            inverseJoinColumns = {@JoinColumn(name="username", referencedColumnName="username")}
    )
    private User personalDocumentCreator;
    public Long getPersonalDocumentId() {
        return personalDocumentId;
    }

    public User getPersonalDocumentCreator() {
        return personalDocumentCreator;
    }

    public void setPersonalDocumentCreator(User personalDocumentCreator) {
        this.personalDocumentCreator = personalDocumentCreator;
    }

    public void setPersonalDocumentId(Long personalDocumentId) {
        this.personalDocumentId = personalDocumentId;
    }

    public String getPersonalDocumentName() {
        return personalDocumentName;
    }

    public void setPersonalDocumentName(String personalDocumentName) {
        this.personalDocumentName = personalDocumentName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getPersonalDocumentDescription() {
        return personalDocumentDescription;
    }

    public void setPersonalDocumentDescription(String personalDocumentDescription) {
        this.personalDocumentDescription = personalDocumentDescription;
    }

    public String getPersonalDocumentVisibility() {
        return personalDocumentVisibility;
    }

    public void setPersonalDocumentVisibility(String personalDocumentVisibility) {
        this.personalDocumentVisibility = personalDocumentVisibility;
    }
}
