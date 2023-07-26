package tn.idrm.receiptconstructor.service.mapper;

import org.mapstruct.*;
import tn.idrm.receiptconstructor.domain.Card;
import tn.idrm.receiptconstructor.domain.Receipt;
import tn.idrm.receiptconstructor.domain.Terminal;
import tn.idrm.receiptconstructor.service.dto.CardDTO;
import tn.idrm.receiptconstructor.service.dto.ReceiptDTO;
import tn.idrm.receiptconstructor.service.dto.TerminalDTO;

/**
 * Mapper for the entity {@link Receipt} and its DTO {@link ReceiptDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReceiptMapper extends EntityMapper<ReceiptDTO, Receipt> {
    @Mapping(target = "terminal_id", source = "terminal_id", qualifiedByName = "terminalId")
    @Mapping(target = "card_id", source = "card_id", qualifiedByName = "cardId")
    ReceiptDTO toDto(Receipt s);

    @Named("terminalId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TerminalDTO toDtoTerminalId(Terminal terminal);

    @Named("cardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CardDTO toDtoCardId(Card card);
}
