package org.event.gestionevenement.service;

import lombok.AllArgsConstructor;
import org.event.gestionevenement.Repository.RoleRepository;
import org.event.gestionevenement.Repository.UtilisateurRepository;
import org.event.gestionevenement.entities.Role;
import org.event.gestionevenement.entities.Utilisateur;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Service
@Transactional
@AllArgsConstructor
public class CompteServiceImpl implements CompteService {

        private UtilisateurRepository utilisateurRepository;
        private RoleRepository roleRepository;
        private PasswordEncoder passwordEncoder;
        @Override
        public void ajouterUtilisateur(Utilisateur utilisateur) {
            // Encoder le mot de passe avant de sauvegarder
            utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
            utilisateurRepository.save(utilisateur);
        }
        @Override
        public Utilisateur addNewUser(String username, String motpasse, String email, String confirmpassword) {
            Utilisateur user = utilisateurRepository.findByUsername(username);
            if(user !=null) throw new RuntimeException("this user already exist");
            if (!motpasse.equals(confirmpassword)) throw new RuntimeException("Password not match");
            user=Utilisateur.builder()
                    .id(UUID.randomUUID().toString())
                    .username(username)
                    .password(passwordEncoder.encode(motpasse))
                    .email(email)
                    .build();
            return utilisateurRepository.save(user);
        }



    @Override
        public Role addNewRole(String roleName) {
            Role role =roleRepository.findById(roleName).orElse(null);
            if(role!=null) throw new RuntimeException("role already exist");
            role= Role.builder()
                    .role(roleName)
                    .build();
            return roleRepository.save(role);
        }

        @Override
        public void addRoleToUser(String username, String roleName) {
            Role appRole = roleRepository.findById(roleName).get();
            Utilisateur appUser = utilisateurRepository.findByUsername(username);
            appUser.getRoles().add(appRole);
//     appUserRepository.save(appUser);

        }

        @Override
        public void removeRoleFromUser(String username, String roleName) {
            Role role = roleRepository.findById(username).get();
            Utilisateur user = utilisateurRepository.findByUsername(username);
            user.getRoles().remove(role);
        }

        @Override
        public Utilisateur loadUserByUsername(String username) {
            return utilisateurRepository.findByUsername(username);
        }
    }
