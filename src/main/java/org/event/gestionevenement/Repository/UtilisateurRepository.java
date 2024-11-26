package org.event.gestionevenement.Repository;

import org.event.gestionevenement.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur,String> {
    Utilisateur findByUsername(String username);

}
