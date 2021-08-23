package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Parametre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Parametre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParametreRepository extends JpaRepository<Parametre, Long> {}
