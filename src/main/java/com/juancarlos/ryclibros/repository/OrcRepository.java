package com.juancarlos.ryclibros.repository;

import com.juancarlos.ryclibros.domain.Orc;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Orc entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrcRepository extends JpaRepository<Orc, Long> {}
