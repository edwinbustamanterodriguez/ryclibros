package com.juancarlos.ryclibros.repository;

import com.juancarlos.ryclibros.domain.Localidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Localidad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocalidadRepository extends JpaRepository<Localidad, Long> {
    Page<Localidad> findAllByProvincia_Id(Long id, Pageable pageable);
}
