package com.juancarlos.ryclibros.service;

import com.juancarlos.ryclibros.domain.Devolucion;
import com.juancarlos.ryclibros.repository.DevolucionRepository;
import com.juancarlos.ryclibros.repository.LibroRepository;
import com.juancarlos.ryclibros.repository.UserRepository;
import com.juancarlos.ryclibros.security.SecurityUtils;
import com.juancarlos.ryclibros.service.dto.DevolucionDTO;
import com.juancarlos.ryclibros.service.dto.UserDTO;
import com.juancarlos.ryclibros.service.mapper.DevolucionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Devolucion}.
 */
@Service
@Transactional
public class DevolucionService {

    private final Logger log = LoggerFactory.getLogger(DevolucionService.class);

    private final DevolucionRepository devolucionRepository;

    private final DevolucionMapper devolucionMapper;

    private final UserRepository userRepository;

    private final PrestamoService prestamoService;

    private final LibroRepository libroRepository;

    public DevolucionService(
        DevolucionRepository devolucionRepository,
        DevolucionMapper devolucionMapper,
        UserRepository userRepository,
        PrestamoService prestamoService,
        LibroRepository libroRepository
    ) {
        this.devolucionRepository = devolucionRepository;
        this.devolucionMapper = devolucionMapper;
        this.userRepository = userRepository;
        this.prestamoService = prestamoService;
        this.libroRepository = libroRepository;
    }

    /**
     * Save a devolucion.
     *
     * @param devolucionDTO the entity to save.
     * @return the persisted entity.
     */
    public DevolucionDTO save(DevolucionDTO devolucionDTO) {
        log.debug("Request to save Devolucion : {}", devolucionDTO);
        Devolucion devolucion = devolucionMapper.toEntity(devolucionDTO);
        devolucion = devolucionRepository.save(devolucion);
        return devolucionMapper.toDto(devolucion);
    }

    /**
     * Partially update a devolucion.
     *
     * @param devolucionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DevolucionDTO> partialUpdate(DevolucionDTO devolucionDTO) {
        log.debug("Request to partially update Devolucion : {}", devolucionDTO);

        return devolucionRepository
            .findById(devolucionDTO.getId())
            .map(
                existingDevolucion -> {
                    devolucionMapper.partialUpdate(existingDevolucion, devolucionDTO);
                    return existingDevolucion;
                }
            )
            .map(devolucionRepository::save)
            .map(devolucionMapper::toDto);
    }

    /**
     * Get all the devolucions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DevolucionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Devolucions");
        return devolucionRepository.findAll(pageable).map(devolucionMapper::toDto);
    }

    /**
     * Get one devolucion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DevolucionDTO> findOne(Long id) {
        log.debug("Request to get Devolucion : {}", id);
        return devolucionRepository.findById(id).map(devolucionMapper::toDto);
    }

    /**
     * Delete the devolucion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Devolucion : {}", id);
        devolucionRepository.deleteById(id);
    }

    public DevolucionDTO setStatusDevueltoToTrue(DevolucionDTO devolucionDTO) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(
                user -> {
                    Long id = user.getId();
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId(id);
                    devolucionDTO.setUser(userDTO);
                }
            );

        this.prestamoService.setStatusDevueltoToTrue(devolucionDTO.getPrestamo().getId());
        this.libroRepository.setCantidadPlus(devolucionDTO.getPrestamo().getLibro().getId());
        Devolucion devolucion = devolucionMapper.toEntity(devolucionDTO);
        devolucion = devolucionRepository.save(devolucion);
        return devolucionMapper.toDto(devolucion);
    }
}
