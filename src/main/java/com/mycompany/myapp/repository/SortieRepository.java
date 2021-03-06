package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Sortie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Sortie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SortieRepository extends JpaRepository<Sortie, Long> {
}
