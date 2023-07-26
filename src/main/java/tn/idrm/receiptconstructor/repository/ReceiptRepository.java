package tn.idrm.receiptconstructor.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.idrm.receiptconstructor.domain.Receipt;

/**
 * Spring Data JPA repository for the Receipt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {}
