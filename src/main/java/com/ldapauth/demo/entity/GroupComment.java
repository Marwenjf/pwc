package com.ldapauth.demo.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "groupComments")
public class GroupComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "group_comment_id")
    private Long idGroupComment;
    @Column(name = "created")
    @CreationTimestamp
    private Date created;
    @Column(name = "updated")
    @UpdateTimestamp
    private Date updated;
    @Column(name = "content")
    private String content;
    @OneToOne(cascade=CascadeType.ALL)
    @JoinTable(name="user_group_comments",
            joinColumns = {@JoinColumn(name="group_comment_id", referencedColumnName="group_comment_id")},
            inverseJoinColumns = {@JoinColumn(name="username", referencedColumnName="username")}
    )
    private User sender;
    @OneToOne(cascade=CascadeType.ALL)
    @JoinTable(name="group_user_comments",
            joinColumns = {@JoinColumn(name="group_comment_id", referencedColumnName="group_comment_id")},
            inverseJoinColumns = {@JoinColumn(name="group_id", referencedColumnName="group_id")}
    )
    private Groupe groupe;

    public Long getIdGroupComment() {
        return idGroupComment;
    }

    public void setIdGroupComment(Long idGroupComment) {
        this.idGroupComment = idGroupComment;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
