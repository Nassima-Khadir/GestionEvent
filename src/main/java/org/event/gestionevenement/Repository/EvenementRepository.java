package org.event.gestionevenement.Repository;

import org.event.gestionevenement.entities.Evenement;
import org.event.gestionevenement.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvenementRepository extends JpaRepository<Evenement, Integer> {
    List<Evenement> findEvenementByTitre(String nom);
    List<Evenement> findByUser(Utilisateur user);

}
