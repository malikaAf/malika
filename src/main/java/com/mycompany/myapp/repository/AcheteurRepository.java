package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Acheteur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Acheteur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AcheteurRepository extends JpaRepository<Acheteur, Long> {}
