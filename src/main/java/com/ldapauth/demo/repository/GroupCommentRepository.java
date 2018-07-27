package com.ldapauth.demo.repository;

import com.ldapauth.demo.entity.GroupComment;
import com.ldapauth.demo.entity.Groupe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GroupCommentRepository extends JpaRepository<GroupComment,Long>{
List<GroupComment> findByGroupe(Groupe groupe);
}
