package org.event.gestionevenement.Repository;

import org.event.gestionevenement.entities.Evenement;
import org.event.gestionevenement.entities.Participation;
import org.event.gestionevenement.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Integer> {
    boolean existsByUserAndEvenement(Utilisateur user, Evenement evenement);
    List<Participation> findByUser(Utilisateur user);
    List<Participation> findByEvenementId(int evenementId);
}

