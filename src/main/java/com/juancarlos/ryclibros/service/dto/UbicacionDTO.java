package com.juancarlos.ryclibros.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.juancarlos.ryclibros.domain.Ubicacion} entity.
 */
public class UbicacionDTO implements Serializable {

    private Long id;

    @NotNull
    private String sector;

    @NotNull
    private Integer numero;

    @NotNull
    private String serie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UbicacionDTO)) {
            return false;
        }

        UbicacionDTO ubicacionDTO = (UbicacionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ubicacionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UbicacionDTO{" +
            "id=" + getId() +
            ", sector='" + getSector() + "'" +
            ", numero=" + getNumero() +
            ", serie='" + getSerie() + "'" +
            "}";
    }
}
