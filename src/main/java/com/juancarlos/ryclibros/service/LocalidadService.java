package com.juancarlos.ryclibros.service;

import com.juancarlos.ryclibros.domain.Localidad;
import com.juancarlos.ryclibros.repository.LocalidadRepository;
import com.juancarlos.ryclibros.service.dto.LocalidadDTO;
import com.juancarlos.ryclibros.service.mapper.LocalidadMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Localidad}.
 */
@Service
@Transactional
public class LocalidadService {

    private final Logger log = LoggerFactory.getLogger(LocalidadService.class);

    private final LocalidadRepository localidadRepository;

    private final LocalidadMapper localidadMapper;

    public LocalidadService(LocalidadRepository localidadRepository, LocalidadMapper localidadMapper) {
        this.localidadRepository = localidadRepository;
        this.localidadMapper = localidadMapper;
    }

    /**
     * Save a localidad.
     *
     * @param localidadDTO the entity to save.
     * @return the persisted entity.
     */
    public LocalidadDTO save(LocalidadDTO localidadDTO) {
        log.debug("Request to save Localidad : {}", localidadDTO);
        Localidad localidad = localidadMapper.toEntity(localidadDTO);
        localidad = localidadRepository.save(localidad);
        return localidadMapper.toDto(localidad);
    }

    /**
     * Partially update a localidad.
     *
     * @param localidadDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LocalidadDTO> partialUpdate(LocalidadDTO localidadDTO) {
        log.debug("Request to partially update Localidad : {}", localidadDTO);

        return localidadRepository
            .findById(localidadDTO.getId())
            .map(
                existingLocalidad -> {
                    localidadMapper.partialUpdate(existingLocalidad, localidadDTO);
                    return existingLocalidad;
                }
            )
            .map(localidadRepository::save)
            .map(localidadMapper::toDto);
    }

    /**
     * Get all the localidads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LocalidadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Localidads");
        return localidadRepository.findAll(pageable).map(localidadMapper::toDto);
    }

    /**
     * Get one localidad by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LocalidadDTO> findOne(Long id) {
        log.debug("Request to get Localidad : {}", id);
        return localidadRepository.findById(id).map(localidadMapper::toDto);
    }

    /**
     * Delete the localidad by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Localidad : {}", id);
        localidadRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<LocalidadDTO> findAllByProvincia(Long id, Pageable pageable) {
        log.debug("Request to get all Localidads");
        return localidadRepository.findAllByProvincia_Id(id, pageable).map(localidadMapper::toDto);
    }
}
