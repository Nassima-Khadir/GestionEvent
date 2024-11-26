package org.event.gestionevenement.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.event.gestionevenement.Repository.EvenementRepository;
import org.event.gestionevenement.Repository.PaiementRepository;
import org.event.gestionevenement.Repository.ParticipationRepository;
import org.event.gestionevenement.Repository.UtilisateurRepository;
import org.event.gestionevenement.entities.Evenement;
import org.event.gestionevenement.entities.Paiement;
import org.event.gestionevenement.entities.Participation;
import org.event.gestionevenement.entities.Utilisateur;
import org.event.gestionevenement.service.EmailService;
import org.event.gestionevenement.service.IPaypalServiceImpl;
import org.event.gestionevenement.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@AllArgsConstructor
@Slf4j
@RequestMapping("/user")
public class PaymentController {
    private final IPaypalServiceImpl paypalService;
    @Autowired
    UserDetailServiceImpl userDetailService;
    @Autowired
    private EvenementRepository evenementRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ParticipationRepository participationRepository;
    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;
    @Autowired
    private PaiementRepository paiementRepository;

    @GetMapping("/payment/{id}")
    public  String homepayement(@PathVariable int id, Model model){

        Optional<Evenement> evenementOpt = evenementRepository.findById(id);

        model.addAttribute("evenementOpt",evenementOpt.get());
        return "homepayement";
    }

    @GetMapping("/payment/create")
    public RedirectView  createPayment(
            @RequestParam("method") String method,
            @RequestParam("amount")  String amount,
            @RequestParam("currency") String currency,
            @RequestParam("eventId") int eventId,Model model
    ){
        try {
            String cancelUrl="http://localhost:8089/user/payment/cancel";
            String successUrl="http://localhost:8089/user/payment/success/" + eventId;
            Payment payment = paypalService.createPayment(Double.valueOf(amount),currency,
                    method,"sale"
                    ,cancelUrl,successUrl);
            Optional<Evenement> evenementOpt = evenementRepository.findById(eventId);
            Evenement evenement = evenementOpt.get();
            model.addAttribute("evenement", evenement);
//            Payment payment = paypalService.createPayment(10.0,"USD",
//                    "paypal","sale","bbbbbb"
//                    ,cancelUrl,successUrl);

            for (Links links: payment.getLinks()){
                if (links.getRel().equals("approval_url")){
                    return  new RedirectView(links.getHref());

                }
            }

        }catch (PayPalRESTException p){
            log.error("Error occurred ",p);
        }
        return  new RedirectView("/payment/error");
    }

    @GetMapping("payment/success/{id}")
    public String participer(Model model, @PathVariable("id") int eventId,
                             @RequestParam("paymentId") String paymentId,
                             @RequestParam("PayerID") String payerId) {
        try {
            // Récupérer l'utilisateur connecté
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Utilisateur user = userDetailServiceImpl.findByUsername(username);

            // Vérifier si l'événement existe
            Optional<Evenement> evenementOpt = evenementRepository.findById(eventId);

            if (evenementOpt.isEmpty()) {
                return "redirect:/?error=eventNotFound";
            }
            Evenement evenement = evenementOpt.get();
            model.addAttribute("evenement", evenement);

            // Vérifier si l'utilisateur a déjà participé
            if (participationRepository.existsByUserAndEvenement(user, evenement)) {
                return "redirect:/?error=alreadyParticipated";
            }

            // Vérifier la capacité
            if (evenement.getCapacite() <= 0) {
                return "redirect:/?error=capacityReached";
            }

            // Vérifier le paiement
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (!payment.getState().equals("approved")) {
                return "redirect:/?error=paymentFailed";
            }
            // Enregistrer le paiement
            Paiement paiement = new Paiement();
            paiement.setAmount(evenement.getPrix()); // prix de l'entité Evenement a un champ prix
            paiement.setMethode("PayPal");
            paiement.setStatut("Approuvé");
            paiement.setDatePaiement(LocalDateTime.now());
            paiement.setParticipant(user);
            paiement.setEvenement(evenement);
            paiementRepository.save(paiement);
            // Ajouter la participation
            Participation participation = new Participation();
            participation.setUser(user);
            participation.setEvenement(evenement);
            participationRepository.save(participation);

            // Réduire la capacité
            evenement.setCapacite(evenement.getCapacite() - 1);
            evenementRepository.save(evenement);

            // Envoi d'email de confirmation
            String subject = "Confirmation de participation";
            String body = "Bonjour " + user.getUsername() + ",\n\n" +
                    "Votre participation à l'événement '" + evenement.getTitre() + "' a été confirmée.";
            emailService.sendEmail(user.getEmail(), subject, body);

            // Ajouter un message de succès
            model.addAttribute("message", "Votre participation à l'événement '" + evenement.getTitre() + "' a été ajoutée avec succès !");
            return "paymentSuccess"; // Page de succès avec message
        } catch (Exception e) {
            e.printStackTrace(); // Log de l'erreur pour le débogage
            return "redirect:/?error=internalError";
        }
    }


    @GetMapping("/payment/cancel")
    public String paymentCancel(){
        return "redirect:/";
    }

    @GetMapping("/payment/error")
    public String paymentError(){
        return "paymentError";
    }

}