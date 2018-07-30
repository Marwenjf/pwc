package com.ldapauth.demo.repository;

import com.ldapauth.demo.entity.Groupe;
import com.ldapauth.demo.entity.Invitation;
import com.ldapauth.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation,Long>{
    List<Invitation> findByGroupe(Groupe groupe);
    List<Invitation> findBySender(User sender);
    List<Invitation> findByReceiver(User receiver);
    List<Invitation> findByGroupeAndSender(Groupe groupe,User sender);
    List<Invitation> findByGroupeAndReceiver(Groupe groupe,User receiver);
    List<Invitation> findByReceiverAndStatus(User receiver,String status);
    @Query("select invitation from Invitation invitation where invitation.groupe = :groupe and invitation.status = :status")
    List<Invitation> findByGroupeAndStatus(@Param("groupe") Groupe groupe,@Param("status") String status);
    List<Invitation> findByGroupeAndStatusAndAndSender(Groupe groupe,String status,User sender);
    Invitation findByGroupeAndStatusAndReceiver(Groupe groupe,String status,User receiver);

}
