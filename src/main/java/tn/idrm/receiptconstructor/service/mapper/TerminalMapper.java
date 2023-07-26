package tn.idrm.receiptconstructor.service.mapper;

import org.mapstruct.*;
import tn.idrm.receiptconstructor.domain.Terminal;
import tn.idrm.receiptconstructor.service.dto.TerminalDTO;

/**
 * Mapper for the entity {@link Terminal} and its DTO {@link TerminalDTO}.
 */
@Mapper(componentModel = "spring")
public interface TerminalMapper extends EntityMapper<TerminalDTO, Terminal> {}
