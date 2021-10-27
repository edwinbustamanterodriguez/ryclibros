package com.juancarlos.ryclibros.repository;

import com.juancarlos.ryclibros.domain.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Libro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LibroRepository extends JpaRepository<Libro, Long>, JpaSpecificationExecutor<Libro> {
    Page<Libro> findAllByNumeroContaining(String search, Pageable pageable);

    Page<Libro> findAllByNumeroContainingAndCantidadGreaterThan(String search, Integer greaterThan, Pageable pageable);

    Page<Libro> findAllByCantidadGreaterThan(Integer greaterThan, Pageable pageable);

    @Modifying
    @Query("update Libro libro set libro.cantidad = libro.cantidad+1 where libro.id = :id")
    void setCantidadPlus(@Param("id") Long id);

    @Modifying
    @Query("update Libro libro set libro.cantidad = libro.cantidad-1 where libro.id = :id")
    void setCantidadMinus(@Param("id") Long id);
}
