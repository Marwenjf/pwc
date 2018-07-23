package com.ldapauth.demo.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    private Long idComment;
    @Column(name = "created")
    @CreationTimestamp
    private Date created;
    @Column(name = "updated")
    @UpdateTimestamp
    private Date updated;
    @Column(name = "content")
    private String content;
    @OneToOne(cascade=CascadeType.ALL)
    @JoinTable(name="user_comments",
            joinColumns = {@JoinColumn(name="comment_id", referencedColumnName="comment_id")},
            inverseJoinColumns = {@JoinColumn(name="receiver", referencedColumnName="username")}
    )
    private User receiver;
    @OneToOne(cascade=CascadeType.ALL)
    @JoinTable(name="user_comment",
            joinColumns = {@JoinColumn(name="comment_id", referencedColumnName="comment_id")},
            inverseJoinColumns = {@JoinColumn(name="sender", referencedColumnName="username")}
    )
    private User sender;

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Long getIdComment() {
        return idComment;
    }

    public void setIdComment(Long idComment) {
        this.idComment = idComment;
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

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User user) {
        this.receiver = user;
    }
}
