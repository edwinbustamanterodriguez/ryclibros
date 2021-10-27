package com.juancarlos.ryclibros.service;

import com.juancarlos.ryclibros.domain.Persona;
import com.juancarlos.ryclibros.repository.PersonaRepository;
import com.juancarlos.ryclibros.service.dto.PersonaDTO;
import com.juancarlos.ryclibros.service.mapper.PersonaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Persona}.
 */
@Service
@Transactional
public class PersonaService {

    private final Logger log = LoggerFactory.getLogger(PersonaService.class);

    private final PersonaRepository personaRepository;

    private final PersonaMapper personaMapper;

    public PersonaService(PersonaRepository personaRepository, PersonaMapper personaMapper) {
        this.personaRepository = personaRepository;
        this.personaMapper = personaMapper;
    }

    /**
     * Save a persona.
     *
     * @param personaDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonaDTO save(PersonaDTO personaDTO) {
        log.debug("Request to save Persona : {}", personaDTO);
        Persona persona = personaMapper.toEntity(personaDTO);
        persona = personaRepository.save(persona);
        return personaMapper.toDto(persona);
    }

    /**
     * Partially update a persona.
     *
     * @param personaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PersonaDTO> partialUpdate(PersonaDTO personaDTO) {
        log.debug("Request to partially update Persona : {}", personaDTO);

        return personaRepository
            .findById(personaDTO.getId())
            .map(
                existingPersona -> {
                    personaMapper.partialUpdate(existingPersona, personaDTO);
                    return existingPersona;
                }
            )
            .map(personaRepository::save)
            .map(personaMapper::toDto);
    }

    /**
     * Get all the personas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Personas");
        return personaRepository.findAll(pageable).map(personaMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PersonaDTO> findAllOR(Pageable pageable) {
        log.debug("Request to get all Personas");
        return personaRepository.findAllByEsOficialDeRegistroIsTrue(pageable).map(personaMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PersonaDTO> findAllORByOrc(Long id, Pageable pageable) {
        log.debug("Request to get all Personas");
        return personaRepository.findAllByOrcs_id(id, pageable).map(personaMapper::toDto);
    }

    /**
     * Get one persona by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonaDTO> findOne(Long id) {
        log.debug("Request to get Persona : {}", id);
        return personaRepository.findById(id).map(personaMapper::toDto);
    }

    /**
     * Delete the persona by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Persona : {}", id);
        personaRepository.deleteById(id);
    }

    public Page<PersonaDTO> findAllByQuery(String query, Integer filter, Pageable pageable) {
        switch (filter) {
            case 1:
                return personaRepository.findAllByApaternoContaining(query, pageable).map(personaMapper::toDto);
            case 2:
                return personaRepository.findAllByAmaternoContaining(query, pageable).map(personaMapper::toDto);
            case 3:
                return personaRepository.findAllByCiContaining(query, pageable).map(personaMapper::toDto);
            case 0:
            default:
                {
                    return personaRepository.findAllByNombreContaining(query, pageable).map(personaMapper::toDto);
                }
        }
    }
}
