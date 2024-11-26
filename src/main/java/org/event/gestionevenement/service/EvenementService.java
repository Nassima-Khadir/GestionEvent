package org.event.gestionevenement.service;

import org.event.gestionevenement.entities.Evenement;
import org.event.gestionevenement.entities.Utilisateur;

import java.util.List;

public interface EvenementService {
    public Evenement addEvenement(Evenement evenement);
    public Evenement updateEvenement(Evenement evenement);
    public Evenement getEvenement(int id);
    public List<Evenement> getAllEvenement();
    public void deleteEvenement(int id);
    public List<Evenement> getEvenementByEvenementName(String titre);
    public List<Evenement> findByUser(Utilisateur user);
    public List<Utilisateur> getParticipantsByEventId(int eventId);

    }
