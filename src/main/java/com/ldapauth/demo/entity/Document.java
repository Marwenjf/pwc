package com.ldapauth.demo.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "document_id")
    private Long documentId;
    @Column(name = "document_name")
    private String documentName;
    @Column(name = "document_description")
    private String documentDescription;
    @Column(name = "created")
    @CreationTimestamp
    private Date created;
    @Column(name = "updated")
    @UpdateTimestamp
    private Date updated;
    @OneToOne()
    @JoinTable(name="groups_documents",
            joinColumns = {@JoinColumn(name="document_id", referencedColumnName="document_id")},
            inverseJoinColumns = {@JoinColumn(name="group_id", referencedColumnName="group_id")}
    )
    private Groupe groupe;
    @OneToOne()
    @JoinTable(name="users_groups_documents",
            joinColumns = {@JoinColumn(name="document_id", referencedColumnName="document_id")},
            inverseJoinColumns = {@JoinColumn(name="username", referencedColumnName="username")}
    )
    private User documentCreator;

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
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

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public User getDocumentCreator() {
        return documentCreator;
    }

    public void setDocumentCreator(User documentCreator) {
        this.documentCreator = documentCreator;
    }
}
