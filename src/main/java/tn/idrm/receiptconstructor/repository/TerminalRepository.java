package tn.idrm.receiptconstructor.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.idrm.receiptconstructor.domain.Terminal;

/**
 * Spring Data JPA repository for the Terminal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TerminalRepository extends JpaRepository<Terminal, Long> {}
