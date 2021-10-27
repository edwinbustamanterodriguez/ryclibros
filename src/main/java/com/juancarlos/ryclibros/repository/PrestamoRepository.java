package com.juancarlos.ryclibros.repository;

import com.juancarlos.ryclibros.domain.Prestamo;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Prestamo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    @Query("select prestamo from Prestamo prestamo where prestamo.user.login = ?#{principal.username}")
    List<Prestamo> findByUserIsCurrentUser();

    Page<Prestamo> findAllByDevueltoFalse(Pageable pageable);

    Page<Prestamo> findAllByLibroNumeroContainingAndDevuelto(String numeroLibro, Boolean devuelto, Pageable pageable);

    @Modifying
    @Query("update Prestamo prestamo set prestamo.devuelto = :status where prestamo.id = :id")
    int setStatusDevueltoToTrue(@Param("status") Boolean status, @Param("id") Long id);
}
