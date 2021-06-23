package com.juancarlos.ryclibros.web.rest;

import static com.juancarlos.ryclibros.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.juancarlos.ryclibros.IntegrationTest;
import com.juancarlos.ryclibros.domain.Libro;
import com.juancarlos.ryclibros.domain.Persona;
import com.juancarlos.ryclibros.domain.Prestamo;
import com.juancarlos.ryclibros.domain.User;
import com.juancarlos.ryclibros.repository.PrestamoRepository;
import com.juancarlos.ryclibros.service.dto.PrestamoDTO;
import com.juancarlos.ryclibros.service.mapper.PrestamoMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link PrestamoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrestamoResourceIT {

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FECHA_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/prestamos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private PrestamoMapper prestamoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrestamoMockMvc;

    private Prestamo prestamo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prestamo createEntity(EntityManager em) {
        Prestamo prestamo = new Prestamo().observaciones(DEFAULT_OBSERVACIONES).fechaFin(DEFAULT_FECHA_FIN);
        // Add required entity
        Libro libro;
        if (TestUtil.findAll(em, Libro.class).isEmpty()) {
            libro = LibroResourceIT.createEntity(em);
            em.persist(libro);
            em.flush();
        } else {
            libro = TestUtil.findAll(em, Libro.class).get(0);
        }
        prestamo.setLibro(libro);
        // Add required entity
        Persona persona;
        if (TestUtil.findAll(em, Persona.class).isEmpty()) {
            persona = PersonaResourceIT.createEntity(em);
            em.persist(persona);
            em.flush();
        } else {
            persona = TestUtil.findAll(em, Persona.class).get(0);
        }
        prestamo.setPersona(persona);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        prestamo.setUser(user);
        return prestamo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prestamo createUpdatedEntity(EntityManager em) {
        Prestamo prestamo = new Prestamo().observaciones(UPDATED_OBSERVACIONES).fechaFin(UPDATED_FECHA_FIN);
        // Add required entity
        Libro libro;
        if (TestUtil.findAll(em, Libro.class).isEmpty()) {
            libro = LibroResourceIT.createUpdatedEntity(em);
            em.persist(libro);
            em.flush();
        } else {
            libro = TestUtil.findAll(em, Libro.class).get(0);
        }
        prestamo.setLibro(libro);
        // Add required entity
        Persona persona;
        if (TestUtil.findAll(em, Persona.class).isEmpty()) {
            persona = PersonaResourceIT.createUpdatedEntity(em);
            em.persist(persona);
            em.flush();
        } else {
            persona = TestUtil.findAll(em, Persona.class).get(0);
        }
        prestamo.setPersona(persona);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        prestamo.setUser(user);
        return prestamo;
    }

    @BeforeEach
    public void initTest() {
        prestamo = createEntity(em);
    }

    @Test
    @Transactional
    void createPrestamo() throws Exception {
        int databaseSizeBeforeCreate = prestamoRepository.findAll().size();
        // Create the Prestamo
        PrestamoDTO prestamoDTO = prestamoMapper.toDto(prestamo);
        restPrestamoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prestamoDTO)))
            .andExpect(status().isCreated());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeCreate + 1);
        Prestamo testPrestamo = prestamoList.get(prestamoList.size() - 1);
        assertThat(testPrestamo.getObservaciones()).isEqualTo(DEFAULT_OBSERVACIONES);
        assertThat(testPrestamo.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
    }

    @Test
    @Transactional
    void createPrestamoWithExistingId() throws Exception {
        // Create the Prestamo with an existing ID
        prestamo.setId(1L);
        PrestamoDTO prestamoDTO = prestamoMapper.toDto(prestamo);

        int databaseSizeBeforeCreate = prestamoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrestamoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prestamoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = prestamoRepository.findAll().size();
        // set the field null
        prestamo.setFechaFin(null);

        // Create the Prestamo, which fails.
        PrestamoDTO prestamoDTO = prestamoMapper.toDto(prestamo);

        restPrestamoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prestamoDTO)))
            .andExpect(status().isBadRequest());

        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrestamos() throws Exception {
        // Initialize the database
        prestamoRepository.saveAndFlush(prestamo);

        // Get all the prestamoList
        restPrestamoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prestamo.getId().intValue())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(sameInstant(DEFAULT_FECHA_FIN))));
    }

    @Test
    @Transactional
    void getPrestamo() throws Exception {
        // Initialize the database
        prestamoRepository.saveAndFlush(prestamo);

        // Get the prestamo
        restPrestamoMockMvc
            .perform(get(ENTITY_API_URL_ID, prestamo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prestamo.getId().intValue()))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES))
            .andExpect(jsonPath("$.fechaFin").value(sameInstant(DEFAULT_FECHA_FIN)));
    }

    @Test
    @Transactional
    void getNonExistingPrestamo() throws Exception {
        // Get the prestamo
        restPrestamoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrestamo() throws Exception {
        // Initialize the database
        prestamoRepository.saveAndFlush(prestamo);

        int databaseSizeBeforeUpdate = prestamoRepository.findAll().size();

        // Update the prestamo
        Prestamo updatedPrestamo = prestamoRepository.findById(prestamo.getId()).get();
        // Disconnect from session so that the updates on updatedPrestamo are not directly saved in db
        em.detach(updatedPrestamo);
        updatedPrestamo.observaciones(UPDATED_OBSERVACIONES).fechaFin(UPDATED_FECHA_FIN);
        PrestamoDTO prestamoDTO = prestamoMapper.toDto(updatedPrestamo);

        restPrestamoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prestamoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prestamoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeUpdate);
        Prestamo testPrestamo = prestamoList.get(prestamoList.size() - 1);
        assertThat(testPrestamo.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
        assertThat(testPrestamo.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void putNonExistingPrestamo() throws Exception {
        int databaseSizeBeforeUpdate = prestamoRepository.findAll().size();
        prestamo.setId(count.incrementAndGet());

        // Create the Prestamo
        PrestamoDTO prestamoDTO = prestamoMapper.toDto(prestamo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestamoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prestamoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prestamoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrestamo() throws Exception {
        int databaseSizeBeforeUpdate = prestamoRepository.findAll().size();
        prestamo.setId(count.incrementAndGet());

        // Create the Prestamo
        PrestamoDTO prestamoDTO = prestamoMapper.toDto(prestamo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestamoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prestamoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrestamo() throws Exception {
        int databaseSizeBeforeUpdate = prestamoRepository.findAll().size();
        prestamo.setId(count.incrementAndGet());

        // Create the Prestamo
        PrestamoDTO prestamoDTO = prestamoMapper.toDto(prestamo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestamoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prestamoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrestamoWithPatch() throws Exception {
        // Initialize the database
        prestamoRepository.saveAndFlush(prestamo);

        int databaseSizeBeforeUpdate = prestamoRepository.findAll().size();

        // Update the prestamo using partial update
        Prestamo partialUpdatedPrestamo = new Prestamo();
        partialUpdatedPrestamo.setId(prestamo.getId());

        partialUpdatedPrestamo.observaciones(UPDATED_OBSERVACIONES);

        restPrestamoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestamo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrestamo))
            )
            .andExpect(status().isOk());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeUpdate);
        Prestamo testPrestamo = prestamoList.get(prestamoList.size() - 1);
        assertThat(testPrestamo.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
        assertThat(testPrestamo.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
    }

    @Test
    @Transactional
    void fullUpdatePrestamoWithPatch() throws Exception {
        // Initialize the database
        prestamoRepository.saveAndFlush(prestamo);

        int databaseSizeBeforeUpdate = prestamoRepository.findAll().size();

        // Update the prestamo using partial update
        Prestamo partialUpdatedPrestamo = new Prestamo();
        partialUpdatedPrestamo.setId(prestamo.getId());

        partialUpdatedPrestamo.observaciones(UPDATED_OBSERVACIONES).fechaFin(UPDATED_FECHA_FIN);

        restPrestamoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestamo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrestamo))
            )
            .andExpect(status().isOk());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeUpdate);
        Prestamo testPrestamo = prestamoList.get(prestamoList.size() - 1);
        assertThat(testPrestamo.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
        assertThat(testPrestamo.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void patchNonExistingPrestamo() throws Exception {
        int databaseSizeBeforeUpdate = prestamoRepository.findAll().size();
        prestamo.setId(count.incrementAndGet());

        // Create the Prestamo
        PrestamoDTO prestamoDTO = prestamoMapper.toDto(prestamo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestamoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prestamoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prestamoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrestamo() throws Exception {
        int databaseSizeBeforeUpdate = prestamoRepository.findAll().size();
        prestamo.setId(count.incrementAndGet());

        // Create the Prestamo
        PrestamoDTO prestamoDTO = prestamoMapper.toDto(prestamo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestamoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prestamoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrestamo() throws Exception {
        int databaseSizeBeforeUpdate = prestamoRepository.findAll().size();
        prestamo.setId(count.incrementAndGet());

        // Create the Prestamo
        PrestamoDTO prestamoDTO = prestamoMapper.toDto(prestamo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestamoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(prestamoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prestamo in the database
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrestamo() throws Exception {
        // Initialize the database
        prestamoRepository.saveAndFlush(prestamo);

        int databaseSizeBeforeDelete = prestamoRepository.findAll().size();

        // Delete the prestamo
        restPrestamoMockMvc
            .perform(delete(ENTITY_API_URL_ID, prestamo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Prestamo> prestamoList = prestamoRepository.findAll();
        assertThat(prestamoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
