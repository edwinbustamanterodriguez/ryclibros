package com.juancarlos.ryclibros.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.juancarlos.ryclibros.domain.Provincia} entity.
 */
public class ProvinciaDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProvinciaDTO)) {
            return false;
        }

        ProvinciaDTO provinciaDTO = (ProvinciaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, provinciaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProvinciaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
