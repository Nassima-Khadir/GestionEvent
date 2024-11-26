package org.event.gestionevenement.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Utilisateur user; // L'utilisateur qui participe à l'événement

    @ManyToOne
    private Evenement evenement; // L'événement auquel l'utilisateur participe

    private Date dateParticipation = new Date();
}