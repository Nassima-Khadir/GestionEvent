package org.event.gestionevenement.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
public class Utilisateur {
    @Id
    private String id;
    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    private String nomPrenom;
    @Column(length = 15)
    private String numTele;
    @Column(nullable = true, length = 50)
    private String companyName;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;
    private LocalDateTime registrationDate;




}
