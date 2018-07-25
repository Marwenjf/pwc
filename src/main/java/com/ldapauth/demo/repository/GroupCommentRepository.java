package com.ldapauth.demo.repository;

import com.ldapauth.demo.entity.GroupComment;
import com.ldapauth.demo.entity.Groupe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupCommentRepository extends JpaRepository<GroupComment,Long>{
List<GroupComment> findByGroupe(Groupe groupe);
}
