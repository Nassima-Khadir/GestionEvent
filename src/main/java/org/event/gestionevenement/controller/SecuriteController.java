package org.event.gestionevenement.controller;

import org.event.gestionevenement.entities.Evenement;
import org.event.gestionevenement.entities.Utilisateur;
import org.event.gestionevenement.service.EvenementServiceImpl;
import org.event.gestionevenement.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class SecuriteController {
    @Autowired
    EvenementServiceImpl evenementService;
    @Autowired
    UserDetailServiceImpl userDetailService;
    @GetMapping("/notAuthenticated")
    public String  notAuthenticated() {
        return "notAuthenticated";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        Model model) {
        // Si un paramètre 'error' est présent dans l'URL, ajouter un message d'erreur au modèle
        if (error != null) {
            model.addAttribute("error", "Nom d'utilisateur ou mot de passe incorrect.");
        }
        return "login";
    }
    @GetMapping("/")
    public String home(Model model){
        // Get the authenticated user’s roles
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // récupère le nom d'utilisateur (user connecté)
        // Trouver l'utilisateur connecté
        Utilisateur user = userDetailService.findByUsername(username); // Trouver l'utilisateur par son nom
        model.addAttribute("username", username);
        // Récupérer les événements créés par cet utilisateur
        List<Evenement> evenements = evenementService.findByUser(user);
        model.addAttribute("evenements", evenements);
        // Check if the user has the role "ADMIN" and redirect to homeAdmin
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "homeAdmin";
        }

        // Check if the user has the role "USER" and redirect to homeUser
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            List<Evenement> listevent = evenementService.getAllEvenement();
            model.addAttribute("listevent", listevent);
            LocalDateTime currentDateTime = LocalDateTime.now();

            // Définir un format personnalisé
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String todayDate = currentDateTime.format(formatter);
            model.addAttribute("todayDate",todayDate);
            return "homeUser";
        }

        // Default redirection if no role is matched
        return "redirect:/login?error"; // or any other page
    }
}