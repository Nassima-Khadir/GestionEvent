package org.event.gestionevenement.service;

import lombok.AllArgsConstructor;
import org.event.gestionevenement.Repository.UtilisateurRepository;
import org.event.gestionevenement.entities.Evenement;
import org.event.gestionevenement.entities.Utilisateur;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UtilisateurRepository utilisateurRepository;
    private CompteService compteService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur user = compteService.loadUserByUsername(username);
        if (user == null) throw new UsernameNotFoundException(String.format("Utilsateur %s not found", username));
        String[] roles = user.getRoles().stream().map(u -> u.getRole()).toArray(String[]::new);
        UserDetails userDetails = User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(roles).build();
        return userDetails;
    }

    public Utilisateur findByUsername(String username) {
        Optional<Utilisateur> user = Optional.ofNullable(utilisateurRepository.findByUsername(username));
        if (user.isPresent()) {
            return user.get(); // Retourner l'utilisateur s'il est trouvé
        } else {
            throw new RuntimeException("Utilisateur non trouvé avec le nom d'utilisateur : " + username);
        }
    }


}

