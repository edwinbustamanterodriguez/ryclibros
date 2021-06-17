package com.juancarlos.ryclibros.service;

import com.juancarlos.ryclibros.domain.*; // for static metamodels
import com.juancarlos.ryclibros.domain.Libro;
import com.juancarlos.ryclibros.repository.LibroRepository;
import com.juancarlos.ryclibros.service.criteria.LibroCriteria;
import com.juancarlos.ryclibros.service.dto.LibroDTO;
import com.juancarlos.ryclibros.service.mapper.LibroMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Libro} entities in the database.
 * The main input is a {@link LibroCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LibroDTO} or a {@link Page} of {@link LibroDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LibroQueryService extends QueryService<Libro> {

    private final Logger log = LoggerFactory.getLogger(LibroQueryService.class);

    private final LibroRepository libroRepository;

    private final LibroMapper libroMapper;

    public LibroQueryService(LibroRepository libroRepository, LibroMapper libroMapper) {
        this.libroRepository = libroRepository;
        this.libroMapper = libroMapper;
    }

    /**
     * Return a {@link List} of {@link LibroDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LibroDTO> findByCriteria(LibroCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Libro> specification = createSpecification(criteria);
        return libroMapper.toDto(libroRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LibroDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LibroDTO> findByCriteria(LibroCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Libro> specification = createSpecification(criteria);
        return libroRepository.findAll(specification, page).map(libroMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LibroCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Libro> specification = createSpecification(criteria);
        return libroRepository.count(specification);
    }

    /**
     * Function to convert {@link LibroCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Libro> createSpecification(LibroCriteria criteria) {
        Specification<Libro> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Libro_.id));
            }
            if (criteria.getNumero() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumero(), Libro_.numero));
            }
            if (criteria.getObservaciones() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservaciones(), Libro_.observaciones));
            }
            if (criteria.getCategoriaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCategoriaId(), root -> root.join(Libro_.categoria, JoinType.LEFT).get(Categoria_.id))
                    );
            }
        }
        return specification;
    }
}
