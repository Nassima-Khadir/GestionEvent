package org.event.gestionevenement.service;

import org.event.gestionevenement.Repository.EvenementRepository;
import org.event.gestionevenement.Repository.ParticipationRepository;
import org.event.gestionevenement.Repository.UtilisateurRepository;
import org.event.gestionevenement.entities.Evenement;
import org.event.gestionevenement.entities.Participation;
import org.event.gestionevenement.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EvenementServiceImpl implements EvenementService {
    @Autowired
    private EvenementRepository evenementRepository;
    private UtilisateurRepository utilisateurRepository;
    private ParticipationRepository participationRepository;

    @Override
    public Evenement addEvenement(Evenement evenement) {
        return evenementRepository.save(evenement);
    }

    @Override
    public Evenement updateEvenement(Evenement evenement) {
        Optional<Evenement> existingEvenement = evenementRepository.findById(evenement.getId());
        if (existingEvenement.isPresent()) {
            Evenement evenementToUpdate = existingEvenement.get();
            evenementToUpdate.setTitre(evenement.getTitre());
            evenementToUpdate.setDescription(evenement.getDescription());
            evenementToUpdate.setLieu(evenement.getLieu());
            evenementToUpdate.setDate(evenement.getDate());
            evenementToUpdate.setCapacite(evenement.getCapacite());
            evenementToUpdate.setType(evenement.getType());
            evenementToUpdate.setPrix(evenement.getPrix());
            return evenementRepository.save(evenementToUpdate);
        } else {
            throw new RuntimeException("Événement non trouvé avec l'ID : " + evenement.getId());
        }
    }


    @Override
    public Evenement getEvenement(int id) {
        return evenementRepository.findById(id).get();
    }

    @Override
    public List<Evenement> getAllEvenement() {
        System.out.println(evenementRepository.findAll());
        return evenementRepository.findAll();
    }

    @Override
    public void deleteEvenement(int id) {
        if (evenementRepository.existsById(id)) {
            evenementRepository.deleteById(id);
        }
    }




    @Override
    public List<Evenement> getEvenementByEvenementName(String titre) {
        return evenementRepository.findEvenementByTitre(titre);
    }
    // Récupérer les événements créés par un utilisateur spécifique
    public List<Evenement> findByUser(Utilisateur user) {
        return evenementRepository.findByUser(user);  // Utiliser la méthode de repository pour filtrer par utilisateur
    }

    public List<Utilisateur> getParticipantsByEventId(int eventId) {
        Optional<Evenement> evenement = evenementRepository.findById(eventId);
        if (evenement.isPresent()) {
            List<Participation> participations = participationRepository.findByEvenementId(eventId);
            return participations.stream()
                    .map(Participation::getUser) // Récupérer les utilisateurs depuis les participations
                    .collect(Collectors.toList());
        }
        throw new RuntimeException("Événement introuvable avec l'ID : " + eventId);
    }
}
//    public void removeParticipant(int eventId,String userId) {
//        Optional<Evenement> evenement = evenementRepository.findById(eventId);
//        Optional<Utilisateur> user =utilisateurRepository.findById(userId);
//        if (evenement.isPresent() && user.isPresent()) {
//            evenement.get().getParticipants().remove(user.get());
//            evenementRepository.save(evenement.get());
//        } else {
//            throw new RuntimeException("Événement ou utilisateur introuvable.");
//        }
//    }




