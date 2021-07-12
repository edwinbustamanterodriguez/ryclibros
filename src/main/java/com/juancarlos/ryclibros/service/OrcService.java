package com.juancarlos.ryclibros.service;

import com.juancarlos.ryclibros.domain.Orc;
import com.juancarlos.ryclibros.repository.OrcRepository;
import com.juancarlos.ryclibros.service.dto.OrcDTO;
import com.juancarlos.ryclibros.service.mapper.OrcMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Orc}.
 */
@Service
@Transactional
public class OrcService {

    private final Logger log = LoggerFactory.getLogger(OrcService.class);

    private final OrcRepository orcRepository;

    private final OrcMapper orcMapper;

    public OrcService(OrcRepository orcRepository, OrcMapper orcMapper) {
        this.orcRepository = orcRepository;
        this.orcMapper = orcMapper;
    }

    /**
     * Save a orc.
     *
     * @param orcDTO the entity to save.
     * @return the persisted entity.
     */
    public OrcDTO save(OrcDTO orcDTO) {
        log.debug("Request to save Orc : {}", orcDTO);
        Orc orc = orcMapper.toEntity(orcDTO);
        orc = orcRepository.save(orc);
        return orcMapper.toDto(orc);
    }

    /**
     * Partially update a orc.
     *
     * @param orcDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrcDTO> partialUpdate(OrcDTO orcDTO) {
        log.debug("Request to partially update Orc : {}", orcDTO);

        return orcRepository
            .findById(orcDTO.getId())
            .map(
                existingOrc -> {
                    orcMapper.partialUpdate(existingOrc, orcDTO);
                    return existingOrc;
                }
            )
            .map(orcRepository::save)
            .map(orcMapper::toDto);
    }

    /**
     * Get all the orcs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrcDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Orcs");
        return orcRepository.findAll(pageable).map(orcMapper::toDto);
    }

    /**
     * Get one orc by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrcDTO> findOne(Long id) {
        log.debug("Request to get Orc : {}", id);
        return orcRepository.findById(id).map(orcMapper::toDto);
    }

    /**
     * Delete the orc by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Orc : {}", id);
        orcRepository.deleteById(id);
    }
}
