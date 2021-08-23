package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Exercice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Exercice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExerciceRepository extends JpaRepository<Exercice, Long> {}
