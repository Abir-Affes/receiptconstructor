package tn.idrm.receiptconstructor.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tn.idrm.receiptconstructor.IntegrationTest;
import tn.idrm.receiptconstructor.domain.Terminal;
import tn.idrm.receiptconstructor.repository.TerminalRepository;
import tn.idrm.receiptconstructor.service.dto.TerminalDTO;
import tn.idrm.receiptconstructor.service.mapper.TerminalMapper;

/**
 * Integration tests for the {@link TerminalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TerminalResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/terminals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private TerminalMapper terminalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTerminalMockMvc;

    private Terminal terminal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Terminal createEntity(EntityManager em) {
        Terminal terminal = new Terminal().description(DEFAULT_DESCRIPTION);
        return terminal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Terminal createUpdatedEntity(EntityManager em) {
        Terminal terminal = new Terminal().description(UPDATED_DESCRIPTION);
        return terminal;
    }

    @BeforeEach
    public void initTest() {
        terminal = createEntity(em);
    }

    @Test
    @Transactional
    void createTerminal() throws Exception {
        int databaseSizeBeforeCreate = terminalRepository.findAll().size();
        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);
        restTerminalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isCreated());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeCreate + 1);
        Terminal testTerminal = terminalList.get(terminalList.size() - 1);
        assertThat(testTerminal.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createTerminalWithExistingId() throws Exception {
        // Create the Terminal with an existing ID
        terminal.setId(1L);
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        int databaseSizeBeforeCreate = terminalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTerminalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTerminals() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList
        restTerminalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terminal.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getTerminal() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get the terminal
        restTerminalMockMvc
            .perform(get(ENTITY_API_URL_ID, terminal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(terminal.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingTerminal() throws Exception {
        // Get the terminal
        restTerminalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTerminal() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();

        // Update the terminal
        Terminal updatedTerminal = terminalRepository.findById(terminal.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTerminal are not directly saved in db
        em.detach(updatedTerminal);
        updatedTerminal.description(UPDATED_DESCRIPTION);
        TerminalDTO terminalDTO = terminalMapper.toDto(updatedTerminal);

        restTerminalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, terminalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalDTO))
            )
            .andExpect(status().isOk());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
        Terminal testTerminal = terminalList.get(terminalList.size() - 1);
        assertThat(testTerminal.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();
        terminal.setId(count.incrementAndGet());

        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTerminalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, terminalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();
        terminal.setId(count.incrementAndGet());

        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();
        terminal.setId(count.incrementAndGet());

        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTerminalWithPatch() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();

        // Update the terminal using partial update
        Terminal partialUpdatedTerminal = new Terminal();
        partialUpdatedTerminal.setId(terminal.getId());

        restTerminalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTerminal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTerminal))
            )
            .andExpect(status().isOk());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
        Terminal testTerminal = terminalList.get(terminalList.size() - 1);
        assertThat(testTerminal.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateTerminalWithPatch() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();

        // Update the terminal using partial update
        Terminal partialUpdatedTerminal = new Terminal();
        partialUpdatedTerminal.setId(terminal.getId());

        partialUpdatedTerminal.description(UPDATED_DESCRIPTION);

        restTerminalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTerminal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTerminal))
            )
            .andExpect(status().isOk());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
        Terminal testTerminal = terminalList.get(terminalList.size() - 1);
        assertThat(testTerminal.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();
        terminal.setId(count.incrementAndGet());

        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTerminalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, terminalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(terminalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();
        terminal.setId(count.incrementAndGet());

        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(terminalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();
        terminal.setId(count.incrementAndGet());

        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(terminalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTerminal() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        int databaseSizeBeforeDelete = terminalRepository.findAll().size();

        // Delete the terminal
        restTerminalMockMvc
            .perform(delete(ENTITY_API_URL_ID, terminal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
