package org.event.gestionevenement.service;

import org.event.gestionevenement.Repository.EvenementRepository;
import org.event.gestionevenement.Repository.ParticipationRepository;
import org.event.gestionevenement.entities.Evenement;
import org.event.gestionevenement.entities.Participation;
import org.event.gestionevenement.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParticipationService {
    @Autowired
    private ParticipationRepository participationRepository;
    @Autowired
    private EvenementRepository evenementRepository;

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
