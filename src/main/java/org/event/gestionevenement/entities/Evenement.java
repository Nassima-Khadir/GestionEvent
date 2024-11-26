package org.event.gestionevenement.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Evenement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String titre;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String lieu;
    private double capacite;
    private String type;
    private double prix;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Utilisateur user;
    @ManyToMany
    @JoinTable(
            name="event_waiting_List",
            joinColumns = @JoinColumn(name="evenement_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")
    )
    private List<Utilisateur> waitingList = new ArrayList<>();
}
