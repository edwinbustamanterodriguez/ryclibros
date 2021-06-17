package com.juancarlos.ryclibros.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.juancarlos.ryclibros.IntegrationTest;
import com.juancarlos.ryclibros.domain.Categoria;
import com.juancarlos.ryclibros.domain.Libro;
import com.juancarlos.ryclibros.repository.LibroRepository;
import com.juancarlos.ryclibros.service.criteria.LibroCriteria;
import com.juancarlos.ryclibros.service.dto.LibroDTO;
import com.juancarlos.ryclibros.service.mapper.LibroMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LibroResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LibroResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/libros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private LibroMapper libroMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLibroMockMvc;

    private Libro libro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Libro createEntity(EntityManager em) {
        Libro libro = new Libro().numero(DEFAULT_NUMERO).observaciones(DEFAULT_OBSERVACIONES);
        // Add required entity
        Categoria categoria;
        if (TestUtil.findAll(em, Categoria.class).isEmpty()) {
            categoria = CategoriaResourceIT.createEntity(em);
            em.persist(categoria);
            em.flush();
        } else {
            categoria = TestUtil.findAll(em, Categoria.class).get(0);
        }
        libro.setCategoria(categoria);
        return libro;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Libro createUpdatedEntity(EntityManager em) {
        Libro libro = new Libro().numero(UPDATED_NUMERO).observaciones(UPDATED_OBSERVACIONES);
        // Add required entity
        Categoria categoria;
        if (TestUtil.findAll(em, Categoria.class).isEmpty()) {
            categoria = CategoriaResourceIT.createUpdatedEntity(em);
            em.persist(categoria);
            em.flush();
        } else {
            categoria = TestUtil.findAll(em, Categoria.class).get(0);
        }
        libro.setCategoria(categoria);
        return libro;
    }

    @BeforeEach
    public void initTest() {
        libro = createEntity(em);
    }

    @Test
    @Transactional
    void createLibro() throws Exception {
        int databaseSizeBeforeCreate = libroRepository.findAll().size();
        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);
        restLibroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(libroDTO)))
            .andExpect(status().isCreated());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeCreate + 1);
        Libro testLibro = libroList.get(libroList.size() - 1);
        assertThat(testLibro.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testLibro.getObservaciones()).isEqualTo(DEFAULT_OBSERVACIONES);
    }

    @Test
    @Transactional
    void createLibroWithExistingId() throws Exception {
        // Create the Libro with an existing ID
        libro.setId(1L);
        LibroDTO libroDTO = libroMapper.toDto(libro);

        int databaseSizeBeforeCreate = libroRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLibroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(libroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = libroRepository.findAll().size();
        // set the field null
        libro.setNumero(null);

        // Create the Libro, which fails.
        LibroDTO libroDTO = libroMapper.toDto(libro);

        restLibroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(libroDTO)))
            .andExpect(status().isBadRequest());

        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLibros() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        // Get all the libroList
        restLibroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(libro.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));
    }

    @Test
    @Transactional
    void getLibro() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        // Get the libro
        restLibroMockMvc
            .perform(get(ENTITY_API_URL_ID, libro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(libro.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES));
    }

    @Test
    @Transactional
    void getLibrosByIdFiltering() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        Long id = libro.getId();

        defaultLibroShouldBeFound("id.equals=" + id);
        defaultLibroShouldNotBeFound("id.notEquals=" + id);

        defaultLibroShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLibroShouldNotBeFound("id.greaterThan=" + id);

        defaultLibroShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLibroShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLibrosByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        // Get all the libroList where numero equals to DEFAULT_NUMERO
        defaultLibroShouldBeFound("numero.equals=" + DEFAULT_NUMERO);

        // Get all the libroList where numero equals to UPDATED_NUMERO
        defaultLibroShouldNotBeFound("numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllLibrosByNumeroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        // Get all the libroList where numero not equals to DEFAULT_NUMERO
        defaultLibroShouldNotBeFound("numero.notEquals=" + DEFAULT_NUMERO);

        // Get all the libroList where numero not equals to UPDATED_NUMERO
        defaultLibroShouldBeFound("numero.notEquals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllLibrosByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        // Get all the libroList where numero in DEFAULT_NUMERO or UPDATED_NUMERO
        defaultLibroShouldBeFound("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO);

        // Get all the libroList where numero equals to UPDATED_NUMERO
        defaultLibroShouldNotBeFound("numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllLibrosByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        // Get all the libroList where numero is not null
        defaultLibroShouldBeFound("numero.specified=true");

        // Get all the libroList where numero is null
        defaultLibroShouldNotBeFound("numero.specified=false");
    }

    @Test
    @Transactional
    void getAllLibrosByNumeroContainsSomething() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        // Get all the libroList where numero contains DEFAULT_NUMERO
        defaultLibroShouldBeFound("numero.contains=" + DEFAULT_NUMERO);

        // Get all the libroList where numero contains UPDATED_NUMERO
        defaultLibroShouldNotBeFound("numero.contains=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllLibrosByNumeroNotContainsSomething() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        // Get all the libroList where numero does not contain DEFAULT_NUMERO
        defaultLibroShouldNotBeFound("numero.doesNotContain=" + DEFAULT_NUMERO);

        // Get all the libroList where numero does not contain UPDATED_NUMERO
        defaultLibroShouldBeFound("numero.doesNotContain=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllLibrosByObservacionesIsEqualToSomething() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        // Get all the libroList where observaciones equals to DEFAULT_OBSERVACIONES
        defaultLibroShouldBeFound("observaciones.equals=" + DEFAULT_OBSERVACIONES);

        // Get all the libroList where observaciones equals to UPDATED_OBSERVACIONES
        defaultLibroShouldNotBeFound("observaciones.equals=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllLibrosByObservacionesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        // Get all the libroList where observaciones not equals to DEFAULT_OBSERVACIONES
        defaultLibroShouldNotBeFound("observaciones.notEquals=" + DEFAULT_OBSERVACIONES);

        // Get all the libroList where observaciones not equals to UPDATED_OBSERVACIONES
        defaultLibroShouldBeFound("observaciones.notEquals=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllLibrosByObservacionesIsInShouldWork() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        // Get all the libroList where observaciones in DEFAULT_OBSERVACIONES or UPDATED_OBSERVACIONES
        defaultLibroShouldBeFound("observaciones.in=" + DEFAULT_OBSERVACIONES + "," + UPDATED_OBSERVACIONES);

        // Get all the libroList where observaciones equals to UPDATED_OBSERVACIONES
        defaultLibroShouldNotBeFound("observaciones.in=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllLibrosByObservacionesIsNullOrNotNull() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        // Get all the libroList where observaciones is not null
        defaultLibroShouldBeFound("observaciones.specified=true");

        // Get all the libroList where observaciones is null
        defaultLibroShouldNotBeFound("observaciones.specified=false");
    }

    @Test
    @Transactional
    void getAllLibrosByObservacionesContainsSomething() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        // Get all the libroList where observaciones contains DEFAULT_OBSERVACIONES
        defaultLibroShouldBeFound("observaciones.contains=" + DEFAULT_OBSERVACIONES);

        // Get all the libroList where observaciones contains UPDATED_OBSERVACIONES
        defaultLibroShouldNotBeFound("observaciones.contains=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllLibrosByObservacionesNotContainsSomething() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        // Get all the libroList where observaciones does not contain DEFAULT_OBSERVACIONES
        defaultLibroShouldNotBeFound("observaciones.doesNotContain=" + DEFAULT_OBSERVACIONES);

        // Get all the libroList where observaciones does not contain UPDATED_OBSERVACIONES
        defaultLibroShouldBeFound("observaciones.doesNotContain=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllLibrosByCategoriaIsEqualToSomething() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);
        Categoria categoria = CategoriaResourceIT.createEntity(em);
        em.persist(categoria);
        em.flush();
        libro.setCategoria(categoria);
        libroRepository.saveAndFlush(libro);
        Long categoriaId = categoria.getId();

        // Get all the libroList where categoria equals to categoriaId
        defaultLibroShouldBeFound("categoriaId.equals=" + categoriaId);

        // Get all the libroList where categoria equals to (categoriaId + 1)
        defaultLibroShouldNotBeFound("categoriaId.equals=" + (categoriaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLibroShouldBeFound(String filter) throws Exception {
        restLibroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(libro.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));

        // Check, that the count call also returns 1
        restLibroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLibroShouldNotBeFound(String filter) throws Exception {
        restLibroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLibroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLibro() throws Exception {
        // Get the libro
        restLibroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLibro() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        int databaseSizeBeforeUpdate = libroRepository.findAll().size();

        // Update the libro
        Libro updatedLibro = libroRepository.findById(libro.getId()).get();
        // Disconnect from session so that the updates on updatedLibro are not directly saved in db
        em.detach(updatedLibro);
        updatedLibro.numero(UPDATED_NUMERO).observaciones(UPDATED_OBSERVACIONES);
        LibroDTO libroDTO = libroMapper.toDto(updatedLibro);

        restLibroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, libroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(libroDTO))
            )
            .andExpect(status().isOk());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
        Libro testLibro = libroList.get(libroList.size() - 1);
        assertThat(testLibro.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testLibro.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void putNonExistingLibro() throws Exception {
        int databaseSizeBeforeUpdate = libroRepository.findAll().size();
        libro.setId(count.incrementAndGet());

        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, libroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(libroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLibro() throws Exception {
        int databaseSizeBeforeUpdate = libroRepository.findAll().size();
        libro.setId(count.incrementAndGet());

        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(libroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLibro() throws Exception {
        int databaseSizeBeforeUpdate = libroRepository.findAll().size();
        libro.setId(count.incrementAndGet());

        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(libroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLibroWithPatch() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        int databaseSizeBeforeUpdate = libroRepository.findAll().size();

        // Update the libro using partial update
        Libro partialUpdatedLibro = new Libro();
        partialUpdatedLibro.setId(libro.getId());

        partialUpdatedLibro.observaciones(UPDATED_OBSERVACIONES);

        restLibroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLibro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLibro))
            )
            .andExpect(status().isOk());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
        Libro testLibro = libroList.get(libroList.size() - 1);
        assertThat(testLibro.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testLibro.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void fullUpdateLibroWithPatch() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        int databaseSizeBeforeUpdate = libroRepository.findAll().size();

        // Update the libro using partial update
        Libro partialUpdatedLibro = new Libro();
        partialUpdatedLibro.setId(libro.getId());

        partialUpdatedLibro.numero(UPDATED_NUMERO).observaciones(UPDATED_OBSERVACIONES);

        restLibroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLibro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLibro))
            )
            .andExpect(status().isOk());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
        Libro testLibro = libroList.get(libroList.size() - 1);
        assertThat(testLibro.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testLibro.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void patchNonExistingLibro() throws Exception {
        int databaseSizeBeforeUpdate = libroRepository.findAll().size();
        libro.setId(count.incrementAndGet());

        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, libroDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(libroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLibro() throws Exception {
        int databaseSizeBeforeUpdate = libroRepository.findAll().size();
        libro.setId(count.incrementAndGet());

        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(libroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLibro() throws Exception {
        int databaseSizeBeforeUpdate = libroRepository.findAll().size();
        libro.setId(count.incrementAndGet());

        // Create the Libro
        LibroDTO libroDTO = libroMapper.toDto(libro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibroMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(libroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLibro() throws Exception {
        // Initialize the database
        libroRepository.saveAndFlush(libro);

        int databaseSizeBeforeDelete = libroRepository.findAll().size();

        // Delete the libro
        restLibroMockMvc
            .perform(delete(ENTITY_API_URL_ID, libro.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
