package com.ldapauth.demo.repository;

import com.ldapauth.demo.entity.PersonalDocument;
import com.ldapauth.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface PersonalDocumentRepository extends JpaRepository<PersonalDocument,Long>{
List<PersonalDocument> findByPersonalDocumentName(String personalDocumentName);
Page<PersonalDocument> findByPersonalDocumentName(String personalDocumentName,Pageable pageable);
@Query("Select p from PersonalDocument p where p.personalDocumentName like :name")
Page<PersonalDocument> findBySearch(@Param("name") String name,Pageable pageable);
Page<PersonalDocument> findByPersonalDocumentCreator(User creator, Pageable pageable);
@Query("Select p from PersonalDocument p where p.personalDocumentCreator = :creator and p.personalDocumentName like :name")
Page<PersonalDocument> searchByPersonalDocumentCreatorAndSearch(@Param("name") String name,@Param("creator")User creator, Pageable pageable);
}
