package tn.idrm.receiptconstructor.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link tn.idrm.receiptconstructor.domain.Terminal} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TerminalDTO implements Serializable {

    private Long id;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TerminalDTO)) {
            return false;
        }

        TerminalDTO terminalDTO = (TerminalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, terminalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TerminalDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
