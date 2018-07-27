package com.ldapauth.demo.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "invitation")
public class Invitation implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "invitation_id")
    private Long idInvitation;
    @Column(name = "invitation_status")
    private String status;
    @Column(name = "created")
    @CreationTimestamp
    private Date created;
    @Column(name = "updated")
    @UpdateTimestamp
    private Date updated;
    @OneToOne(cascade=CascadeType.ALL)
    @JoinTable(name="receiver_invitations",
            joinColumns = {@JoinColumn(name="invitation_id", referencedColumnName="invitation_id")},
            inverseJoinColumns = {@JoinColumn(name="receiver", referencedColumnName="username")}
    )
    private User receiver;
    @OneToOne(cascade=CascadeType.ALL)
    @JoinTable(name="sender_invitations",
            joinColumns = {@JoinColumn(name="invitation_id", referencedColumnName="invitation_id")},
            inverseJoinColumns = {@JoinColumn(name="sender", referencedColumnName="username")}
    )
    private User sender;
    @OneToOne(cascade=CascadeType.ALL)
    @JoinTable(name="groupe_invitations",
            joinColumns = {@JoinColumn(name="invitation_id", referencedColumnName="invitation_id")},
            inverseJoinColumns = {@JoinColumn(name="group_id", referencedColumnName="group_id")}
    )
    private Groupe groupe;

    public Long getIdInvitation() {
        return idInvitation;
    }

    public void setIdInvitation(Long idInvitation) {
        this.idInvitation = idInvitation;
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

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
