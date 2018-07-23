package com.ldapauth.demo.repository;

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
public interface GroupeRepository extends JpaRepository<Groupe,Long>{
    List<Groupe> findByGroupName(String groupName);
    Page<Groupe> findByGroupName(String groupName, Pageable pageable);
    @Query("select g from Groupe g where g.groupName like :name")
    Page<Groupe> findBySearch(@Param("name") String name,Pageable pageable);
    Page<Groupe> findByCreator(User creator,Pageable pageable);
    @Query("select g from Groupe g where g.creator = :creator and g.groupName like :name")
    Page<Groupe> searchByCreatorAndSearch(@Param("name") String name,@Param("creator")User creator, Pageable pageable);


}
