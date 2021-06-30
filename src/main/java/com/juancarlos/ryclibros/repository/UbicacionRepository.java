package com.juancarlos.ryclibros.repository;

import com.juancarlos.ryclibros.domain.Ubicacion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ubicacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {}
