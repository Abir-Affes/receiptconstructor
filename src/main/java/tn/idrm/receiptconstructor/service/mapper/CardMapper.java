package tn.idrm.receiptconstructor.service.mapper;

import org.mapstruct.*;
import tn.idrm.receiptconstructor.domain.Card;
import tn.idrm.receiptconstructor.service.dto.CardDTO;

/**
 * Mapper for the entity {@link Card} and its DTO {@link CardDTO}.
 */
@Mapper(componentModel = "spring")
public interface CardMapper extends EntityMapper<CardDTO, Card> {}
