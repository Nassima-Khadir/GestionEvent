package org.event.gestionevenement.service;

import org.event.gestionevenement.entities.Role;
import org.event.gestionevenement.entities.Utilisateur;

public interface CompteService {
        Utilisateur addNewUser(String username, String password, String email, String confirmpassword);
        Role addNewRole(String roleName);
        void addRoleToUser(String username, String roleName);
        void removeRoleFromUser(String username, String roleName);
        Utilisateur loadUserByUsername(String username);
        void ajouterUtilisateur(Utilisateur utilisateur);

    }

