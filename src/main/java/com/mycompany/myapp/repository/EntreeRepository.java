package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Entree;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Entree entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntreeRepository extends JpaRepository<Entree, Long> {}
