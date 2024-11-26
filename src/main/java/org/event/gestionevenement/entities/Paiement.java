package org.event.gestionevenement.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paiement {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int id;
    private double amount;
    private String methode;
    private String statut;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime datePaiement;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Utilisateur participant;

    @ManyToOne
    @JoinColumn(name = "evenement_id")
    private Evenement evenement;

}