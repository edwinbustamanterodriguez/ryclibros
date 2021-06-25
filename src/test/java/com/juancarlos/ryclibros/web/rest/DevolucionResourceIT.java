package com.juancarlos.ryclibros.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.juancarlos.ryclibros.IntegrationTest;
import com.juancarlos.ryclibros.domain.Devolucion;
import com.juancarlos.ryclibros.domain.Persona;
import com.juancarlos.ryclibros.domain.Prestamo;
import com.juancarlos.ryclibros.domain.User;
import com.juancarlos.ryclibros.repository.DevolucionRepository;
import com.juancarlos.ryclibros.service.dto.DevolucionDTO;
import com.juancarlos.ryclibros.service.mapper.DevolucionMapper;
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
 * Integration tests for the {@link DevolucionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DevolucionResourceIT {

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/devolucions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DevolucionRepository devolucionRepository;

    @Autowired
    private DevolucionMapper devolucionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDevolucionMockMvc;

    private Devolucion devolucion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Devolucion createEntity(EntityManager em) {
        Devolucion devolucion = new Devolucion().observaciones(DEFAULT_OBSERVACIONES);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        devolucion.setUser(user);
        // Add required entity
        Persona persona;
        if (TestUtil.findAll(em, Persona.class).isEmpty()) {
            persona = PersonaResourceIT.createEntity(em);
            em.persist(persona);
            em.flush();
        } else {
            persona = TestUtil.findAll(em, Persona.class).get(0);
        }
        devolucion.setPersona(persona);
        // Add required entity
        Prestamo prestamo;
        if (TestUtil.findAll(em, Prestamo.class).isEmpty()) {
            prestamo = PrestamoResourceIT.createEntity(em);
            em.persist(prestamo);
            em.flush();
        } else {
            prestamo = TestUtil.findAll(em, Prestamo.class).get(0);
        }
        devolucion.setPrestamo(prestamo);
        return devolucion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Devolucion createUpdatedEntity(EntityManager em) {
        Devolucion devolucion = new Devolucion().observaciones(UPDATED_OBSERVACIONES);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        devolucion.setUser(user);
        // Add required entity
        Persona persona;
        if (TestUtil.findAll(em, Persona.class).isEmpty()) {
            persona = PersonaResourceIT.createUpdatedEntity(em);
            em.persist(persona);
            em.flush();
        } else {
            persona = TestUtil.findAll(em, Persona.class).get(0);
        }
        devolucion.setPersona(persona);
        // Add required entity
        Prestamo prestamo;
        if (TestUtil.findAll(em, Prestamo.class).isEmpty()) {
            prestamo = PrestamoResourceIT.createUpdatedEntity(em);
            em.persist(prestamo);
            em.flush();
        } else {
            prestamo = TestUtil.findAll(em, Prestamo.class).get(0);
        }
        devolucion.setPrestamo(prestamo);
        return devolucion;
    }

    @BeforeEach
    public void initTest() {
        devolucion = createEntity(em);
    }

    @Test
    @Transactional
    void createDevolucion() throws Exception {
        int databaseSizeBeforeCreate = devolucionRepository.findAll().size();
        // Create the Devolucion
        DevolucionDTO devolucionDTO = devolucionMapper.toDto(devolucion);
        restDevolucionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(devolucionDTO)))
            .andExpect(status().isCreated());

        // Validate the Devolucion in the database
        List<Devolucion> devolucionList = devolucionRepository.findAll();
        assertThat(devolucionList).hasSize(databaseSizeBeforeCreate + 1);
        Devolucion testDevolucion = devolucionList.get(devolucionList.size() - 1);
        assertThat(testDevolucion.getObservaciones()).isEqualTo(DEFAULT_OBSERVACIONES);
    }

    @Test
    @Transactional
    void createDevolucionWithExistingId() throws Exception {
        // Create the Devolucion with an existing ID
        devolucion.setId(1L);
        DevolucionDTO devolucionDTO = devolucionMapper.toDto(devolucion);

        int databaseSizeBeforeCreate = devolucionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDevolucionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(devolucionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Devolucion in the database
        List<Devolucion> devolucionList = devolucionRepository.findAll();
        assertThat(devolucionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDevolucions() throws Exception {
        // Initialize the database
        devolucionRepository.saveAndFlush(devolucion);

        // Get all the devolucionList
        restDevolucionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(devolucion.getId().intValue())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));
    }

    @Test
    @Transactional
    void getDevolucion() throws Exception {
        // Initialize the database
        devolucionRepository.saveAndFlush(devolucion);

        // Get the devolucion
        restDevolucionMockMvc
            .perform(get(ENTITY_API_URL_ID, devolucion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(devolucion.getId().intValue()))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES));
    }

    @Test
    @Transactional
    void getNonExistingDevolucion() throws Exception {
        // Get the devolucion
        restDevolucionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDevolucion() throws Exception {
        // Initialize the database
        devolucionRepository.saveAndFlush(devolucion);

        int databaseSizeBeforeUpdate = devolucionRepository.findAll().size();

        // Update the devolucion
        Devolucion updatedDevolucion = devolucionRepository.findById(devolucion.getId()).get();
        // Disconnect from session so that the updates on updatedDevolucion are not directly saved in db
        em.detach(updatedDevolucion);
        updatedDevolucion.observaciones(UPDATED_OBSERVACIONES);
        DevolucionDTO devolucionDTO = devolucionMapper.toDto(updatedDevolucion);

        restDevolucionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, devolucionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(devolucionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Devolucion in the database
        List<Devolucion> devolucionList = devolucionRepository.findAll();
        assertThat(devolucionList).hasSize(databaseSizeBeforeUpdate);
        Devolucion testDevolucion = devolucionList.get(devolucionList.size() - 1);
        assertThat(testDevolucion.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void putNonExistingDevolucion() throws Exception {
        int databaseSizeBeforeUpdate = devolucionRepository.findAll().size();
        devolucion.setId(count.incrementAndGet());

        // Create the Devolucion
        DevolucionDTO devolucionDTO = devolucionMapper.toDto(devolucion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDevolucionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, devolucionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(devolucionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Devolucion in the database
        List<Devolucion> devolucionList = devolucionRepository.findAll();
        assertThat(devolucionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDevolucion() throws Exception {
        int databaseSizeBeforeUpdate = devolucionRepository.findAll().size();
        devolucion.setId(count.incrementAndGet());

        // Create the Devolucion
        DevolucionDTO devolucionDTO = devolucionMapper.toDto(devolucion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDevolucionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(devolucionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Devolucion in the database
        List<Devolucion> devolucionList = devolucionRepository.findAll();
        assertThat(devolucionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDevolucion() throws Exception {
        int databaseSizeBeforeUpdate = devolucionRepository.findAll().size();
        devolucion.setId(count.incrementAndGet());

        // Create the Devolucion
        DevolucionDTO devolucionDTO = devolucionMapper.toDto(devolucion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDevolucionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(devolucionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Devolucion in the database
        List<Devolucion> devolucionList = devolucionRepository.findAll();
        assertThat(devolucionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDevolucionWithPatch() throws Exception {
        // Initialize the database
        devolucionRepository.saveAndFlush(devolucion);

        int databaseSizeBeforeUpdate = devolucionRepository.findAll().size();

        // Update the devolucion using partial update
        Devolucion partialUpdatedDevolucion = new Devolucion();
        partialUpdatedDevolucion.setId(devolucion.getId());

        partialUpdatedDevolucion.observaciones(UPDATED_OBSERVACIONES);

        restDevolucionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevolucion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDevolucion))
            )
            .andExpect(status().isOk());

        // Validate the Devolucion in the database
        List<Devolucion> devolucionList = devolucionRepository.findAll();
        assertThat(devolucionList).hasSize(databaseSizeBeforeUpdate);
        Devolucion testDevolucion = devolucionList.get(devolucionList.size() - 1);
        assertThat(testDevolucion.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void fullUpdateDevolucionWithPatch() throws Exception {
        // Initialize the database
        devolucionRepository.saveAndFlush(devolucion);

        int databaseSizeBeforeUpdate = devolucionRepository.findAll().size();

        // Update the devolucion using partial update
        Devolucion partialUpdatedDevolucion = new Devolucion();
        partialUpdatedDevolucion.setId(devolucion.getId());

        partialUpdatedDevolucion.observaciones(UPDATED_OBSERVACIONES);

        restDevolucionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevolucion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDevolucion))
            )
            .andExpect(status().isOk());

        // Validate the Devolucion in the database
        List<Devolucion> devolucionList = devolucionRepository.findAll();
        assertThat(devolucionList).hasSize(databaseSizeBeforeUpdate);
        Devolucion testDevolucion = devolucionList.get(devolucionList.size() - 1);
        assertThat(testDevolucion.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void patchNonExistingDevolucion() throws Exception {
        int databaseSizeBeforeUpdate = devolucionRepository.findAll().size();
        devolucion.setId(count.incrementAndGet());

        // Create the Devolucion
        DevolucionDTO devolucionDTO = devolucionMapper.toDto(devolucion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDevolucionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, devolucionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(devolucionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Devolucion in the database
        List<Devolucion> devolucionList = devolucionRepository.findAll();
        assertThat(devolucionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDevolucion() throws Exception {
        int databaseSizeBeforeUpdate = devolucionRepository.findAll().size();
        devolucion.setId(count.incrementAndGet());

        // Create the Devolucion
        DevolucionDTO devolucionDTO = devolucionMapper.toDto(devolucion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDevolucionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(devolucionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Devolucion in the database
        List<Devolucion> devolucionList = devolucionRepository.findAll();
        assertThat(devolucionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDevolucion() throws Exception {
        int databaseSizeBeforeUpdate = devolucionRepository.findAll().size();
        devolucion.setId(count.incrementAndGet());

        // Create the Devolucion
        DevolucionDTO devolucionDTO = devolucionMapper.toDto(devolucion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDevolucionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(devolucionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Devolucion in the database
        List<Devolucion> devolucionList = devolucionRepository.findAll();
        assertThat(devolucionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDevolucion() throws Exception {
        // Initialize the database
        devolucionRepository.saveAndFlush(devolucion);

        int databaseSizeBeforeDelete = devolucionRepository.findAll().size();

        // Delete the devolucion
        restDevolucionMockMvc
            .perform(delete(ENTITY_API_URL_ID, devolucion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Devolucion> devolucionList = devolucionRepository.findAll();
        assertThat(devolucionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
