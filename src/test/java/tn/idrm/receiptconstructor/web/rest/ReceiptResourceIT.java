package tn.idrm.receiptconstructor.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
import tn.idrm.receiptconstructor.domain.Receipt;
import tn.idrm.receiptconstructor.domain.enumeration.result;
import tn.idrm.receiptconstructor.domain.enumeration.trans_type;
import tn.idrm.receiptconstructor.repository.ReceiptRepository;
import tn.idrm.receiptconstructor.service.dto.ReceiptDTO;
import tn.idrm.receiptconstructor.service.mapper.ReceiptMapper;

/**
 * Integration tests for the {@link ReceiptResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReceiptResourceIT {

    private static final Integer DEFAULT_RECEIPT_NO = 9999;
    private static final Integer UPDATED_RECEIPT_NO = 9998;

    private static final Integer DEFAULT_TRACE_NO = 999999;
    private static final Integer UPDATED_TRACE_NO = 999998;

    private static final Double DEFAULT_AMOUNT = 99999.99D;
    private static final Double UPDATED_AMOUNT = 99998D;

    private static final trans_type DEFAULT_TRANSACTION_TYPE = trans_type.UNKNOWN;
    private static final trans_type UPDATED_TRANSACTION_TYPE = trans_type.GIROCARD;

    private static final Integer DEFAULT_VU_NO = 1;
    private static final Integer UPDATED_VU_NO = 2;

    private static final String DEFAULT_RECEIPT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_RECEIPT_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_REF_PARAMETERS = 1;
    private static final Integer UPDATED_REF_PARAMETERS = 2;

    private static final Integer DEFAULT_LICENSING_NO = 1;
    private static final Integer UPDATED_LICENSING_NO = 2;

    private static final Integer DEFAULT_POS_INFO = 1;
    private static final Integer UPDATED_POS_INFO = 2;

    private static final result DEFAULT_RESULT = result.SUCCESS;
    private static final result UPDATED_RESULT = result.REJECTED;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/receipts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ReceiptMapper receiptMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReceiptMockMvc;

    private Receipt receipt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Receipt createEntity(EntityManager em) {
        Receipt receipt = new Receipt()
            .receipt_no(DEFAULT_RECEIPT_NO)
            .trace_no(DEFAULT_TRACE_NO)
            .amount(DEFAULT_AMOUNT)
            .transaction_type(DEFAULT_TRANSACTION_TYPE)
            .vu_no(DEFAULT_VU_NO)
            .receipt_type(DEFAULT_RECEIPT_TYPE)
            .ref_parameters(DEFAULT_REF_PARAMETERS)
            .licensing_no(DEFAULT_LICENSING_NO)
            .pos_info(DEFAULT_POS_INFO)
            .result(DEFAULT_RESULT)
            .date(DEFAULT_DATE);
        return receipt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Receipt createUpdatedEntity(EntityManager em) {
        Receipt receipt = new Receipt()
            .receipt_no(UPDATED_RECEIPT_NO)
            .trace_no(UPDATED_TRACE_NO)
            .amount(UPDATED_AMOUNT)
            .transaction_type(UPDATED_TRANSACTION_TYPE)
            .vu_no(UPDATED_VU_NO)
            .receipt_type(UPDATED_RECEIPT_TYPE)
            .ref_parameters(UPDATED_REF_PARAMETERS)
            .licensing_no(UPDATED_LICENSING_NO)
            .pos_info(UPDATED_POS_INFO)
            .result(UPDATED_RESULT)
            .date(UPDATED_DATE);
        return receipt;
    }

    @BeforeEach
    public void initTest() {
        receipt = createEntity(em);
    }

    @Test
    @Transactional
    void createReceipt() throws Exception {
        int databaseSizeBeforeCreate = receiptRepository.findAll().size();
        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);
        restReceiptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receiptDTO)))
            .andExpect(status().isCreated());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeCreate + 1);
        Receipt testReceipt = receiptList.get(receiptList.size() - 1);
        assertThat(testReceipt.getReceipt_no()).isEqualTo(DEFAULT_RECEIPT_NO);
        assertThat(testReceipt.getTrace_no()).isEqualTo(DEFAULT_TRACE_NO);
        assertThat(testReceipt.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testReceipt.getTransaction_type()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testReceipt.getVu_no()).isEqualTo(DEFAULT_VU_NO);
        assertThat(testReceipt.getReceipt_type()).isEqualTo(DEFAULT_RECEIPT_TYPE);
        assertThat(testReceipt.getRef_parameters()).isEqualTo(DEFAULT_REF_PARAMETERS);
        assertThat(testReceipt.getLicensing_no()).isEqualTo(DEFAULT_LICENSING_NO);
        assertThat(testReceipt.getPos_info()).isEqualTo(DEFAULT_POS_INFO);
        assertThat(testReceipt.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testReceipt.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createReceiptWithExistingId() throws Exception {
        // Create the Receipt with an existing ID
        receipt.setId(1L);
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        int databaseSizeBeforeCreate = receiptRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReceiptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receiptDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReceipts() throws Exception {
        // Initialize the database
        receiptRepository.saveAndFlush(receipt);

        // Get all the receiptList
        restReceiptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(receipt.getId().intValue())))
            .andExpect(jsonPath("$.[*].receipt_no").value(hasItem(DEFAULT_RECEIPT_NO)))
            .andExpect(jsonPath("$.[*].trace_no").value(hasItem(DEFAULT_TRACE_NO)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].transaction_type").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].vu_no").value(hasItem(DEFAULT_VU_NO)))
            .andExpect(jsonPath("$.[*].receipt_type").value(hasItem(DEFAULT_RECEIPT_TYPE)))
            .andExpect(jsonPath("$.[*].ref_parameters").value(hasItem(DEFAULT_REF_PARAMETERS)))
            .andExpect(jsonPath("$.[*].licensing_no").value(hasItem(DEFAULT_LICENSING_NO)))
            .andExpect(jsonPath("$.[*].pos_info").value(hasItem(DEFAULT_POS_INFO)))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getReceipt() throws Exception {
        // Initialize the database
        receiptRepository.saveAndFlush(receipt);

        // Get the receipt
        restReceiptMockMvc
            .perform(get(ENTITY_API_URL_ID, receipt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(receipt.getId().intValue()))
            .andExpect(jsonPath("$.receipt_no").value(DEFAULT_RECEIPT_NO))
            .andExpect(jsonPath("$.trace_no").value(DEFAULT_TRACE_NO))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.transaction_type").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.vu_no").value(DEFAULT_VU_NO))
            .andExpect(jsonPath("$.receipt_type").value(DEFAULT_RECEIPT_TYPE))
            .andExpect(jsonPath("$.ref_parameters").value(DEFAULT_REF_PARAMETERS))
            .andExpect(jsonPath("$.licensing_no").value(DEFAULT_LICENSING_NO))
            .andExpect(jsonPath("$.pos_info").value(DEFAULT_POS_INFO))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReceipt() throws Exception {
        // Get the receipt
        restReceiptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReceipt() throws Exception {
        // Initialize the database
        receiptRepository.saveAndFlush(receipt);

        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();

        // Update the receipt
        Receipt updatedReceipt = receiptRepository.findById(receipt.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReceipt are not directly saved in db
        em.detach(updatedReceipt);
        updatedReceipt
            .receipt_no(UPDATED_RECEIPT_NO)
            .trace_no(UPDATED_TRACE_NO)
            .amount(UPDATED_AMOUNT)
            .transaction_type(UPDATED_TRANSACTION_TYPE)
            .vu_no(UPDATED_VU_NO)
            .receipt_type(UPDATED_RECEIPT_TYPE)
            .ref_parameters(UPDATED_REF_PARAMETERS)
            .licensing_no(UPDATED_LICENSING_NO)
            .pos_info(UPDATED_POS_INFO)
            .result(UPDATED_RESULT)
            .date(UPDATED_DATE);
        ReceiptDTO receiptDTO = receiptMapper.toDto(updatedReceipt);

        restReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, receiptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(receiptDTO))
            )
            .andExpect(status().isOk());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
        Receipt testReceipt = receiptList.get(receiptList.size() - 1);
        assertThat(testReceipt.getReceipt_no()).isEqualTo(UPDATED_RECEIPT_NO);
        assertThat(testReceipt.getTrace_no()).isEqualTo(UPDATED_TRACE_NO);
        assertThat(testReceipt.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testReceipt.getTransaction_type()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testReceipt.getVu_no()).isEqualTo(UPDATED_VU_NO);
        assertThat(testReceipt.getReceipt_type()).isEqualTo(UPDATED_RECEIPT_TYPE);
        assertThat(testReceipt.getRef_parameters()).isEqualTo(UPDATED_REF_PARAMETERS);
        assertThat(testReceipt.getLicensing_no()).isEqualTo(UPDATED_LICENSING_NO);
        assertThat(testReceipt.getPos_info()).isEqualTo(UPDATED_POS_INFO);
        assertThat(testReceipt.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testReceipt.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();
        receipt.setId(count.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, receiptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(receiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();
        receipt.setId(count.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(receiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();
        receipt.setId(count.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceiptMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receiptDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReceiptWithPatch() throws Exception {
        // Initialize the database
        receiptRepository.saveAndFlush(receipt);

        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();

        // Update the receipt using partial update
        Receipt partialUpdatedReceipt = new Receipt();
        partialUpdatedReceipt.setId(receipt.getId());

        partialUpdatedReceipt.vu_no(UPDATED_VU_NO).licensing_no(UPDATED_LICENSING_NO).date(UPDATED_DATE);

        restReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReceipt))
            )
            .andExpect(status().isOk());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
        Receipt testReceipt = receiptList.get(receiptList.size() - 1);
        assertThat(testReceipt.getReceipt_no()).isEqualTo(DEFAULT_RECEIPT_NO);
        assertThat(testReceipt.getTrace_no()).isEqualTo(DEFAULT_TRACE_NO);
        assertThat(testReceipt.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testReceipt.getTransaction_type()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testReceipt.getVu_no()).isEqualTo(UPDATED_VU_NO);
        assertThat(testReceipt.getReceipt_type()).isEqualTo(DEFAULT_RECEIPT_TYPE);
        assertThat(testReceipt.getRef_parameters()).isEqualTo(DEFAULT_REF_PARAMETERS);
        assertThat(testReceipt.getLicensing_no()).isEqualTo(UPDATED_LICENSING_NO);
        assertThat(testReceipt.getPos_info()).isEqualTo(DEFAULT_POS_INFO);
        assertThat(testReceipt.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testReceipt.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateReceiptWithPatch() throws Exception {
        // Initialize the database
        receiptRepository.saveAndFlush(receipt);

        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();

        // Update the receipt using partial update
        Receipt partialUpdatedReceipt = new Receipt();
        partialUpdatedReceipt.setId(receipt.getId());

        partialUpdatedReceipt
            .receipt_no(UPDATED_RECEIPT_NO)
            .trace_no(UPDATED_TRACE_NO)
            .amount(UPDATED_AMOUNT)
            .transaction_type(UPDATED_TRANSACTION_TYPE)
            .vu_no(UPDATED_VU_NO)
            .receipt_type(UPDATED_RECEIPT_TYPE)
            .ref_parameters(UPDATED_REF_PARAMETERS)
            .licensing_no(UPDATED_LICENSING_NO)
            .pos_info(UPDATED_POS_INFO)
            .result(UPDATED_RESULT)
            .date(UPDATED_DATE);

        restReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReceipt))
            )
            .andExpect(status().isOk());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
        Receipt testReceipt = receiptList.get(receiptList.size() - 1);
        assertThat(testReceipt.getReceipt_no()).isEqualTo(UPDATED_RECEIPT_NO);
        assertThat(testReceipt.getTrace_no()).isEqualTo(UPDATED_TRACE_NO);
        assertThat(testReceipt.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testReceipt.getTransaction_type()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testReceipt.getVu_no()).isEqualTo(UPDATED_VU_NO);
        assertThat(testReceipt.getReceipt_type()).isEqualTo(UPDATED_RECEIPT_TYPE);
        assertThat(testReceipt.getRef_parameters()).isEqualTo(UPDATED_REF_PARAMETERS);
        assertThat(testReceipt.getLicensing_no()).isEqualTo(UPDATED_LICENSING_NO);
        assertThat(testReceipt.getPos_info()).isEqualTo(UPDATED_POS_INFO);
        assertThat(testReceipt.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testReceipt.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();
        receipt.setId(count.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, receiptDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(receiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();
        receipt.setId(count.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(receiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();
        receipt.setId(count.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(receiptDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReceipt() throws Exception {
        // Initialize the database
        receiptRepository.saveAndFlush(receipt);

        int databaseSizeBeforeDelete = receiptRepository.findAll().size();

        // Delete the receipt
        restReceiptMockMvc
            .perform(delete(ENTITY_API_URL_ID, receipt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
