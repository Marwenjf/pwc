package com.ldapauth.demo.repository;

import com.ldapauth.demo.entity.Comment;
import com.ldapauth.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long>{
    @Query("select c from Comment c where c.receiver = :receiver")
    List<Comment> findByReceiver(@Param("receiver")User receiver);
}
