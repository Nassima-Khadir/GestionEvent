package org.event.gestionevenement.Repository;

import org.event.gestionevenement.entities.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
    List<Evaluation> findByEvenement_Id(int evenementId);}