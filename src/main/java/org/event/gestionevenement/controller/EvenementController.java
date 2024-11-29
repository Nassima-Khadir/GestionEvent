package org.event.gestionevenement.controller;

import org.event.gestionevenement.Repository.EvenementRepository;
import org.event.gestionevenement.Repository.PaiementRepository;
import org.event.gestionevenement.entities.*;
import org.event.gestionevenement.service.EvaluationService;
import org.event.gestionevenement.service.EvenementServiceImpl;
import org.event.gestionevenement.service.ParticipationService;
import org.event.gestionevenement.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class EvenementController {

    @Autowired
    private EvenementServiceImpl evenementService;
    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;
    @Autowired
    private ParticipationService participationService;

    @Autowired
    private EvenementRepository eventRep;

    @Autowired
    PaiementRepository paiementRepository;

    // Afficher form event
    @GetMapping("/ajoutevent")
    public String afficherFormulaireAjout(Model model) {
        model.addAttribute("evenement", new Evenement());
        return "ajoutevent";
    }

    // submit event
    @PostMapping("/addevent")
    public String ajouterEvenement( @ModelAttribute Evenement evenement) {
        // Récupérer l'utilisateur connecté à partir de Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // Récupère le nom d'utilisateur (user connecté)

        // Trouver l'utilisateur connecté
        Utilisateur user = userDetailServiceImpl.findByUsername(username);  // Assurez-vous d'avoir ce service

        // Associer l'événement à l'utilisateur connecté
        evenement.setUser(user);  // Assurez-vous que l'événement a un attribut "user" pour lier l'utilisateur

        // Ajouter l'événement à la base de données
        evenementService.addEvenement(evenement);

        return "redirect:/";  // Rediriger après l'ajout
    }

    // Afficher form edit event
    @GetMapping("/showeditevent/{id}")
    public String afficherFormulaireModification(@PathVariable int id, Model model) {
        Evenement evenement = evenementService.getEvenement(id);
        model.addAttribute("evenement", evenement);
        return "editevent";
    }



    // submit modification event
    @PostMapping("/editevent/{id}")
    public String modifierEvenement(@ModelAttribute("evenement") Evenement evenement, RedirectAttributes redirectAttributes) {
        try {
            evenementService.updateEvenement(evenement);
            redirectAttributes.addFlashAttribute("success", "L'événement a été modifié avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue lors de la modification de l'événement.");
        }
        return "redirect:/";
    }


    // Supprimer un événement
 /*   @GetMapping("/deleteevent/{id}")
    public String supprimerEvenement(@PathVariable int id) {
        evenementService.deleteEvenement(id);
        return "redirect:/";
    }*/

    @GetMapping("/deleteevent/{id}")
    public String deleteEvent(@PathVariable int id, RedirectAttributes redirectAttributes) {
        List<Utilisateur> participation= participationService.getParticipantsByEventId(id);
        Evenement evenement =evenementService.getEvenement(id);
        if (evenement != null && !participation.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vous ne pouvez pas supprimer un événement avec des participants.");
            return "redirect:/";
        }
        evenementService.deleteEvenement(id);
        redirectAttributes.addFlashAttribute("success", "L'événement a été supprimé avec succès.");
        return "redirect:/";
    }

    @GetMapping("/participants/{id}")
    public String afficherParticipants(@PathVariable int id, Model model) {
        Optional<Evenement> evenementOpt = eventRep.findById(id);
        if (evenementOpt.isEmpty()) {
            return "redirect:/admin?error=eventNotFound";
        }
        Evenement evenement = evenementOpt.get();
        List<Utilisateur> participants = participationService.getParticipantsByEventId(id);
        model.addAttribute("participants", participants);
        model.addAttribute("id", id); // pour afficher l'ID de l'événement si nécessaire
        model.addAttribute("eventCapacity", evenement.getCapacite());
        model.addAttribute("evenementtitle", evenement.getTitre());
        return "participants"; // Vue pour afficher les participants
    }

    @GetMapping("/waitinglist/{id}")
    public String showWaitingList(@PathVariable int id, Model model) {
        Optional<Evenement> evenementOpt = eventRep.findById(id);
        if (evenementOpt.isEmpty()) {
            return "redirect:/admin?error=eventNotFound";
        }
        Evenement evenement = evenementOpt.get();

        // Trier la liste d'attente par ordre croissant de date d'enregistrement
        List<Utilisateur> sortedWaitingList = evenement.getWaitingList().stream()
                .sorted(Comparator.comparing(Utilisateur::getRegistrationDate))
                .collect(Collectors.toList());

        model.addAttribute("waitingList", sortedWaitingList);
        model.addAttribute("eventTitle", evenement.getTitre());
        return "waitingList"; // La vue HTML qui affichera la liste d'attente
    }

    // Afficher la liste de payement
    @GetMapping("/showpayement")
    public String afficherPayement(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // récupère le nom d'utilisateur (user connecté)
        // Trouver l'utilisateur connecté
        Utilisateur user = userDetailServiceImpl.findByUsername(username); // Trouver l'utilisateur par son nom
        model.addAttribute("username", username);
        List<Paiement> paye=paiementRepository.findAll();
        model.addAttribute("paye", paye);
        return "listepayement";
    }
    @Autowired
    EvaluationService evaluationService;
    @GetMapping("/evaluations/{id}")
    public String showEvaluations(@PathVariable int id, Model model) {
        // Récupérer les évaluations associées à l'événement avec l'ID donné
        List<Evaluation> evaluations = evaluationService.findByEventId(id);
        Evenement evenement= evenementService.getEvenement(id);
        // Ajouter les données au modèle
        model.addAttribute("evaluations", evaluations);
        model.addAttribute("eventId", evenement.getTitre());

        return "evaluations"; // Vue correspondante à afficher
    }

}