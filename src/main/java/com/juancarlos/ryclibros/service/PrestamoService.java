package com.juancarlos.ryclibros.service;

import com.juancarlos.ryclibros.domain.Prestamo;
import com.juancarlos.ryclibros.repository.PrestamoRepository;
import com.juancarlos.ryclibros.repository.UserRepository;
import com.juancarlos.ryclibros.security.SecurityUtils;
import com.juancarlos.ryclibros.service.dto.PrestamoDTO;
import com.juancarlos.ryclibros.service.dto.UserDTO;
import com.juancarlos.ryclibros.service.mapper.PrestamoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Prestamo}.
 */
@Service
@Transactional
public class PrestamoService {

    private final Logger log = LoggerFactory.getLogger(PrestamoService.class);

    private final PrestamoRepository prestamoRepository;

    private final PrestamoMapper prestamoMapper;

    private final UserRepository userRepository;

    public PrestamoService(PrestamoRepository prestamoRepository, PrestamoMapper prestamoMapper, UserRepository userRepository) {
        this.prestamoRepository = prestamoRepository;
        this.prestamoMapper = prestamoMapper;
        this.userRepository = userRepository;
    }

    /**
     * Save a prestamo.
     *
     * @param prestamoDTO the entity to save.
     * @return the persisted entity.
     */
    public PrestamoDTO save(PrestamoDTO prestamoDTO) {
        log.debug("Request to save Prestamo : {}", prestamoDTO);

        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(
                user -> {
                    Long id = user.getId();
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId(id);
                    prestamoDTO.setUser(userDTO);
                }
            );

        Prestamo prestamo = prestamoMapper.toEntity(prestamoDTO);
        prestamo = prestamoRepository.save(prestamo);
        return prestamoMapper.toDto(prestamo);
    }

    /**
     * Partially update a prestamo.
     *
     * @param prestamoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PrestamoDTO> partialUpdate(PrestamoDTO prestamoDTO) {
        log.debug("Request to partially update Prestamo : {}", prestamoDTO);

        return prestamoRepository
            .findById(prestamoDTO.getId())
            .map(
                existingPrestamo -> {
                    prestamoMapper.partialUpdate(existingPrestamo, prestamoDTO);
                    return existingPrestamo;
                }
            )
            .map(prestamoRepository::save)
            .map(prestamoMapper::toDto);
    }

    /**
     * Get all the prestamos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PrestamoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Prestamos");
        return prestamoRepository.findAll(pageable).map(prestamoMapper::toDto);
    }

    /**
     * Get one prestamo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PrestamoDTO> findOne(Long id) {
        log.debug("Request to get Prestamo : {}", id);
        return prestamoRepository.findById(id).map(prestamoMapper::toDto);
    }

    /**
     * Delete the prestamo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Prestamo : {}", id);
        prestamoRepository.deleteById(id);
    }
}
