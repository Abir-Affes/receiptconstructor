package tn.idrm.receiptconstructor.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.idrm.receiptconstructor.domain.Receipt;
import tn.idrm.receiptconstructor.repository.ReceiptRepository;
import tn.idrm.receiptconstructor.service.dto.ReceiptDTO;
import tn.idrm.receiptconstructor.service.mapper.ReceiptMapper;

/**
 * Service Implementation for managing {@link Receipt}.
 */
@Service
@Transactional
public class ReceiptService {

    private final Logger log = LoggerFactory.getLogger(ReceiptService.class);

    private final ReceiptRepository receiptRepository;

    private final ReceiptMapper receiptMapper;

    public ReceiptService(ReceiptRepository receiptRepository, ReceiptMapper receiptMapper) {
        this.receiptRepository = receiptRepository;
        this.receiptMapper = receiptMapper;
    }

    /**
     * Save a receipt.
     *
     * @param receiptDTO the entity to save.
     * @return the persisted entity.
     */
    public ReceiptDTO save(ReceiptDTO receiptDTO) {
        log.debug("Request to save Receipt : {}", receiptDTO);
        Receipt receipt = receiptMapper.toEntity(receiptDTO);
        receipt = receiptRepository.save(receipt);
        return receiptMapper.toDto(receipt);
    }

    /**
     * Update a receipt.
     *
     * @param receiptDTO the entity to save.
     * @return the persisted entity.
     */
    public ReceiptDTO update(ReceiptDTO receiptDTO) {
        log.debug("Request to update Receipt : {}", receiptDTO);
        Receipt receipt = receiptMapper.toEntity(receiptDTO);
        receipt = receiptRepository.save(receipt);
        return receiptMapper.toDto(receipt);
    }

    /**
     * Partially update a receipt.
     *
     * @param receiptDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReceiptDTO> partialUpdate(ReceiptDTO receiptDTO) {
        log.debug("Request to partially update Receipt : {}", receiptDTO);

        return receiptRepository
            .findById(receiptDTO.getId())
            .map(existingReceipt -> {
                receiptMapper.partialUpdate(existingReceipt, receiptDTO);

                return existingReceipt;
            })
            .map(receiptRepository::save)
            .map(receiptMapper::toDto);
    }

    /**
     * Get all the receipts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ReceiptDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Receipts");
        return receiptRepository.findAll(pageable).map(receiptMapper::toDto);
    }

    /**
     * Get one receipt by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReceiptDTO> findOne(Long id) {
        log.debug("Request to get Receipt : {}", id);
        return receiptRepository.findById(id).map(receiptMapper::toDto);
    }

    /**
     * Delete the receipt by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Receipt : {}", id);
        receiptRepository.deleteById(id);
    }
}
