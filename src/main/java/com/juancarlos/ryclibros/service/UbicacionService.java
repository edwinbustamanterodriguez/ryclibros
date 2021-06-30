package com.juancarlos.ryclibros.service;

import com.juancarlos.ryclibros.domain.Ubicacion;
import com.juancarlos.ryclibros.repository.UbicacionRepository;
import com.juancarlos.ryclibros.service.dto.UbicacionDTO;
import com.juancarlos.ryclibros.service.mapper.UbicacionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ubicacion}.
 */
@Service
@Transactional
public class UbicacionService {

    private final Logger log = LoggerFactory.getLogger(UbicacionService.class);

    private final UbicacionRepository ubicacionRepository;

    private final UbicacionMapper ubicacionMapper;

    public UbicacionService(UbicacionRepository ubicacionRepository, UbicacionMapper ubicacionMapper) {
        this.ubicacionRepository = ubicacionRepository;
        this.ubicacionMapper = ubicacionMapper;
    }

    /**
     * Save a ubicacion.
     *
     * @param ubicacionDTO the entity to save.
     * @return the persisted entity.
     */
    public UbicacionDTO save(UbicacionDTO ubicacionDTO) {
        log.debug("Request to save Ubicacion : {}", ubicacionDTO);
        Ubicacion ubicacion = ubicacionMapper.toEntity(ubicacionDTO);
        ubicacion = ubicacionRepository.save(ubicacion);
        return ubicacionMapper.toDto(ubicacion);
    }

    /**
     * Partially update a ubicacion.
     *
     * @param ubicacionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UbicacionDTO> partialUpdate(UbicacionDTO ubicacionDTO) {
        log.debug("Request to partially update Ubicacion : {}", ubicacionDTO);

        return ubicacionRepository
            .findById(ubicacionDTO.getId())
            .map(
                existingUbicacion -> {
                    ubicacionMapper.partialUpdate(existingUbicacion, ubicacionDTO);
                    return existingUbicacion;
                }
            )
            .map(ubicacionRepository::save)
            .map(ubicacionMapper::toDto);
    }

    /**
     * Get all the ubicacions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UbicacionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ubicacions");
        return ubicacionRepository.findAll(pageable).map(ubicacionMapper::toDto);
    }

    /**
     * Get one ubicacion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UbicacionDTO> findOne(Long id) {
        log.debug("Request to get Ubicacion : {}", id);
        return ubicacionRepository.findById(id).map(ubicacionMapper::toDto);
    }

    /**
     * Delete the ubicacion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ubicacion : {}", id);
        ubicacionRepository.deleteById(id);
    }
}
