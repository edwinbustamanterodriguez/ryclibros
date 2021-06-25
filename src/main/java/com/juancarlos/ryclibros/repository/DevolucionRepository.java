package com.juancarlos.ryclibros.repository;

import com.juancarlos.ryclibros.domain.Devolucion;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Devolucion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DevolucionRepository extends JpaRepository<Devolucion, Long> {
    @Query("select devolucion from Devolucion devolucion where devolucion.user.login = ?#{principal.username}")
    List<Devolucion> findByUserIsCurrentUser();
}
