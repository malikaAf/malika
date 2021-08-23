package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Mark;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Mark entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {}
