package com.juancarlos.ryclibros.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.juancarlos.ryclibros.IntegrationTest;
import com.juancarlos.ryclibros.domain.Orc;
import com.juancarlos.ryclibros.repository.OrcRepository;
import com.juancarlos.ryclibros.service.dto.OrcDTO;
import com.juancarlos.ryclibros.service.mapper.OrcMapper;
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
 * Integration tests for the {@link OrcResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrcResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/orcs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrcRepository orcRepository;

    @Autowired
    private OrcMapper orcMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrcMockMvc;

    private Orc orc;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orc createEntity(EntityManager em) {
        Orc orc = new Orc().numero(DEFAULT_NUMERO);
        return orc;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orc createUpdatedEntity(EntityManager em) {
        Orc orc = new Orc().numero(UPDATED_NUMERO);
        return orc;
    }

    @BeforeEach
    public void initTest() {
        orc = createEntity(em);
    }

    @Test
    @Transactional
    void createOrc() throws Exception {
        int databaseSizeBeforeCreate = orcRepository.findAll().size();
        // Create the Orc
        OrcDTO orcDTO = orcMapper.toDto(orc);
        restOrcMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orcDTO)))
            .andExpect(status().isCreated());

        // Validate the Orc in the database
        List<Orc> orcList = orcRepository.findAll();
        assertThat(orcList).hasSize(databaseSizeBeforeCreate + 1);
        Orc testOrc = orcList.get(orcList.size() - 1);
        assertThat(testOrc.getNumero()).isEqualTo(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    void createOrcWithExistingId() throws Exception {
        // Create the Orc with an existing ID
        orc.setId(1L);
        OrcDTO orcDTO = orcMapper.toDto(orc);

        int databaseSizeBeforeCreate = orcRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrcMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orcDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Orc in the database
        List<Orc> orcList = orcRepository.findAll();
        assertThat(orcList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = orcRepository.findAll().size();
        // set the field null
        orc.setNumero(null);

        // Create the Orc, which fails.
        OrcDTO orcDTO = orcMapper.toDto(orc);

        restOrcMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orcDTO)))
            .andExpect(status().isBadRequest());

        List<Orc> orcList = orcRepository.findAll();
        assertThat(orcList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrcs() throws Exception {
        // Initialize the database
        orcRepository.saveAndFlush(orc);

        // Get all the orcList
        restOrcMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orc.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)));
    }

    @Test
    @Transactional
    void getOrc() throws Exception {
        // Initialize the database
        orcRepository.saveAndFlush(orc);

        // Get the orc
        restOrcMockMvc
            .perform(get(ENTITY_API_URL_ID, orc.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orc.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO));
    }

    @Test
    @Transactional
    void getNonExistingOrc() throws Exception {
        // Get the orc
        restOrcMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrc() throws Exception {
        // Initialize the database
        orcRepository.saveAndFlush(orc);

        int databaseSizeBeforeUpdate = orcRepository.findAll().size();

        // Update the orc
        Orc updatedOrc = orcRepository.findById(orc.getId()).get();
        // Disconnect from session so that the updates on updatedOrc are not directly saved in db
        em.detach(updatedOrc);
        updatedOrc.numero(UPDATED_NUMERO);
        OrcDTO orcDTO = orcMapper.toDto(updatedOrc);

        restOrcMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orcDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orcDTO))
            )
            .andExpect(status().isOk());

        // Validate the Orc in the database
        List<Orc> orcList = orcRepository.findAll();
        assertThat(orcList).hasSize(databaseSizeBeforeUpdate);
        Orc testOrc = orcList.get(orcList.size() - 1);
        assertThat(testOrc.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void putNonExistingOrc() throws Exception {
        int databaseSizeBeforeUpdate = orcRepository.findAll().size();
        orc.setId(count.incrementAndGet());

        // Create the Orc
        OrcDTO orcDTO = orcMapper.toDto(orc);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrcMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orcDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orcDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orc in the database
        List<Orc> orcList = orcRepository.findAll();
        assertThat(orcList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrc() throws Exception {
        int databaseSizeBeforeUpdate = orcRepository.findAll().size();
        orc.setId(count.incrementAndGet());

        // Create the Orc
        OrcDTO orcDTO = orcMapper.toDto(orc);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrcMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orcDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orc in the database
        List<Orc> orcList = orcRepository.findAll();
        assertThat(orcList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrc() throws Exception {
        int databaseSizeBeforeUpdate = orcRepository.findAll().size();
        orc.setId(count.incrementAndGet());

        // Create the Orc
        OrcDTO orcDTO = orcMapper.toDto(orc);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrcMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orcDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Orc in the database
        List<Orc> orcList = orcRepository.findAll();
        assertThat(orcList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrcWithPatch() throws Exception {
        // Initialize the database
        orcRepository.saveAndFlush(orc);

        int databaseSizeBeforeUpdate = orcRepository.findAll().size();

        // Update the orc using partial update
        Orc partialUpdatedOrc = new Orc();
        partialUpdatedOrc.setId(orc.getId());

        partialUpdatedOrc.numero(UPDATED_NUMERO);

        restOrcMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrc.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrc))
            )
            .andExpect(status().isOk());

        // Validate the Orc in the database
        List<Orc> orcList = orcRepository.findAll();
        assertThat(orcList).hasSize(databaseSizeBeforeUpdate);
        Orc testOrc = orcList.get(orcList.size() - 1);
        assertThat(testOrc.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void fullUpdateOrcWithPatch() throws Exception {
        // Initialize the database
        orcRepository.saveAndFlush(orc);

        int databaseSizeBeforeUpdate = orcRepository.findAll().size();

        // Update the orc using partial update
        Orc partialUpdatedOrc = new Orc();
        partialUpdatedOrc.setId(orc.getId());

        partialUpdatedOrc.numero(UPDATED_NUMERO);

        restOrcMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrc.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrc))
            )
            .andExpect(status().isOk());

        // Validate the Orc in the database
        List<Orc> orcList = orcRepository.findAll();
        assertThat(orcList).hasSize(databaseSizeBeforeUpdate);
        Orc testOrc = orcList.get(orcList.size() - 1);
        assertThat(testOrc.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void patchNonExistingOrc() throws Exception {
        int databaseSizeBeforeUpdate = orcRepository.findAll().size();
        orc.setId(count.incrementAndGet());

        // Create the Orc
        OrcDTO orcDTO = orcMapper.toDto(orc);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrcMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orcDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orcDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orc in the database
        List<Orc> orcList = orcRepository.findAll();
        assertThat(orcList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrc() throws Exception {
        int databaseSizeBeforeUpdate = orcRepository.findAll().size();
        orc.setId(count.incrementAndGet());

        // Create the Orc
        OrcDTO orcDTO = orcMapper.toDto(orc);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrcMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orcDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orc in the database
        List<Orc> orcList = orcRepository.findAll();
        assertThat(orcList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrc() throws Exception {
        int databaseSizeBeforeUpdate = orcRepository.findAll().size();
        orc.setId(count.incrementAndGet());

        // Create the Orc
        OrcDTO orcDTO = orcMapper.toDto(orc);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrcMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orcDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Orc in the database
        List<Orc> orcList = orcRepository.findAll();
        assertThat(orcList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrc() throws Exception {
        // Initialize the database
        orcRepository.saveAndFlush(orc);

        int databaseSizeBeforeDelete = orcRepository.findAll().size();

        // Delete the orc
        restOrcMockMvc.perform(delete(ENTITY_API_URL_ID, orc.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Orc> orcList = orcRepository.findAll();
        assertThat(orcList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
