package com.juancarlos.ryclibros.service;

import com.juancarlos.ryclibros.domain.Libro;
import com.juancarlos.ryclibros.repository.LibroRepository;
import com.juancarlos.ryclibros.service.dto.LibroDTO;
import com.juancarlos.ryclibros.service.mapper.LibroMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Libro}.
 */
@Service
@Transactional
public class LibroService {

    private final Logger log = LoggerFactory.getLogger(LibroService.class);

    private final LibroRepository libroRepository;

    private final LibroMapper libroMapper;

    public LibroService(LibroRepository libroRepository, LibroMapper libroMapper) {
        this.libroRepository = libroRepository;
        this.libroMapper = libroMapper;
    }

    /**
     * Save a libro.
     *
     * @param libroDTO the entity to save.
     * @return the persisted entity.
     */
    public LibroDTO save(LibroDTO libroDTO) {
        log.debug("Request to save Libro : {}", libroDTO);
        Libro libro = libroMapper.toEntity(libroDTO);
        libro = libroRepository.save(libro);
        return libroMapper.toDto(libro);
    }

    /**
     * Partially update a libro.
     *
     * @param libroDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LibroDTO> partialUpdate(LibroDTO libroDTO) {
        log.debug("Request to partially update Libro : {}", libroDTO);

        return libroRepository
            .findById(libroDTO.getId())
            .map(
                existingLibro -> {
                    libroMapper.partialUpdate(existingLibro, libroDTO);
                    return existingLibro;
                }
            )
            .map(libroRepository::save)
            .map(libroMapper::toDto);
    }

    /**
     * Get all the libros.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LibroDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Libros");
        return libroRepository.findAll(pageable).map(libroMapper::toDto);
    }

    /**
     * Get one libro by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LibroDTO> findOne(Long id) {
        log.debug("Request to get Libro : {}", id);
        return libroRepository.findById(id).map(libroMapper::toDto);
    }

    /**
     * Delete the libro by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Libro : {}", id);
        libroRepository.deleteById(id);
    }

    public Page<LibroDTO> getAllLibrosSearch(String search, Pageable pageable) {
        return libroRepository.findAllByNumeroContaining(search, pageable).map(libroMapper::toDto);
    }
}
