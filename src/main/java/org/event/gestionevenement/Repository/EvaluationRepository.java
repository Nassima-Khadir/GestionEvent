package org.event.gestionevenement.Repository;

import org.event.gestionevenement.entities.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {

}