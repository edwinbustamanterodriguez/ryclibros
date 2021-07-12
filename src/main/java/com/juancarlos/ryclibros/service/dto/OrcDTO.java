package com.juancarlos.ryclibros.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.juancarlos.ryclibros.domain.Orc} entity.
 */
public class OrcDTO implements Serializable {

    private Long id;

    @NotNull
    private String numero;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrcDTO)) {
            return false;
        }

        OrcDTO orcDTO = (OrcDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orcDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrcDTO{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            "}";
    }
}
