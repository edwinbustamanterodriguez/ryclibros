package com.juancarlos.ryclibros.repository;

import com.juancarlos.ryclibros.domain.Persona;
import java.nio.channels.FileChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Persona entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Page<Persona> findAllByNombreContaining(String nombre, Pageable pageable);

    Page<Persona> findAllByApaternoContaining(String Apaterno, Pageable pageable);

    Page<Persona> findAllByAmaternoContaining(String Amaterno, Pageable pageable);

    Page<Persona> findAllByCiContaining(String Ci, Pageable pageable);

    Page<Persona> findAllByEsOficialDeRegistroIsTrue(Pageable pageable);
}
