package com.ldapauth.demo.repository;

import com.ldapauth.demo.entity.GroupDocument;
import com.ldapauth.demo.entity.Groupe;
import com.ldapauth.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupDocumentRepository extends JpaRepository<GroupDocument,Long>{
List<GroupDocument> findByDocumentName(String documentName);
Page<GroupDocument> findByDocumentName(String documentName, Pageable pageable);
@Query("Select d from GroupDocument d where d.documentName like :name")
Page<GroupDocument> findBySearch(@Param("name") String name, Pageable pageable);
Page<GroupDocument> findByDocumentCreator(User user,Pageable pageable);
List<GroupDocument> findByDocumentCreator(User user);
Page<GroupDocument> findByGroupe(Groupe groupe,Pageable pageable);
List<GroupDocument> findByGroupe(Groupe groupe);
Page<GroupDocument> findByDocumentCreatorAndGroupe(User user,Groupe groupe,Pageable pageable);
@Query("Select d from GroupDocument d where d.documentName like :name and d.documentCreator = :documentCreator and d.groupe = :groupe")
Page<GroupDocument> searchByDocumentCreatorAndGroupeAndSearch(@Param("name") String name,@Param("documentCreator") User documentCreator,@Param("groupe") Groupe groupe,Pageable pageable);
@Query("Select d from GroupDocument d where d.documentName like :name and d.documentCreator = :documentCreator and d.groupe = :groupe")
List<GroupDocument> searchByDocumentCreatorAndGroupeAndSearch(@Param("name") String name,@Param("documentCreator") User documentCreator,@Param("groupe") Groupe groupe);
@Query("Select d from GroupDocument d where d.documentName like :name and d.groupe = :groupe")
Page<GroupDocument> searchByGroupeAndSearch(@Param("name") String name,@Param("groupe") Groupe groupe,Pageable pageable);
@Query("Select d from GroupDocument d where d.documentName like :name and d.groupe = :groupe")
List<GroupDocument> searchByGroupeAndSearch(@Param("name") String name,@Param("groupe") Groupe groupe);
}
