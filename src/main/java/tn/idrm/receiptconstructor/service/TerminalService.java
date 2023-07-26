package tn.idrm.receiptconstructor.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.idrm.receiptconstructor.domain.Terminal;
import tn.idrm.receiptconstructor.repository.TerminalRepository;
import tn.idrm.receiptconstructor.service.dto.TerminalDTO;
import tn.idrm.receiptconstructor.service.mapper.TerminalMapper;

/**
 * Service Implementation for managing {@link Terminal}.
 */
@Service
@Transactional
public class TerminalService {

    private final Logger log = LoggerFactory.getLogger(TerminalService.class);

    private final TerminalRepository terminalRepository;

    private final TerminalMapper terminalMapper;

    public TerminalService(TerminalRepository terminalRepository, TerminalMapper terminalMapper) {
        this.terminalRepository = terminalRepository;
        this.terminalMapper = terminalMapper;
    }

    /**
     * Save a terminal.
     *
     * @param terminalDTO the entity to save.
     * @return the persisted entity.
     */
    public TerminalDTO save(TerminalDTO terminalDTO) {
        log.debug("Request to save Terminal : {}", terminalDTO);
        Terminal terminal = terminalMapper.toEntity(terminalDTO);
        terminal = terminalRepository.save(terminal);
        return terminalMapper.toDto(terminal);
    }

    /**
     * Update a terminal.
     *
     * @param terminalDTO the entity to save.
     * @return the persisted entity.
     */
    public TerminalDTO update(TerminalDTO terminalDTO) {
        log.debug("Request to update Terminal : {}", terminalDTO);
        Terminal terminal = terminalMapper.toEntity(terminalDTO);
        terminal = terminalRepository.save(terminal);
        return terminalMapper.toDto(terminal);
    }

    /**
     * Partially update a terminal.
     *
     * @param terminalDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TerminalDTO> partialUpdate(TerminalDTO terminalDTO) {
        log.debug("Request to partially update Terminal : {}", terminalDTO);

        return terminalRepository
            .findById(terminalDTO.getId())
            .map(existingTerminal -> {
                terminalMapper.partialUpdate(existingTerminal, terminalDTO);

                return existingTerminal;
            })
            .map(terminalRepository::save)
            .map(terminalMapper::toDto);
    }

    /**
     * Get all the terminals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TerminalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Terminals");
        return terminalRepository.findAll(pageable).map(terminalMapper::toDto);
    }

    /**
     * Get one terminal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TerminalDTO> findOne(Long id) {
        log.debug("Request to get Terminal : {}", id);
        return terminalRepository.findById(id).map(terminalMapper::toDto);
    }

    /**
     * Delete the terminal by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Terminal : {}", id);
        terminalRepository.deleteById(id);
    }
}
