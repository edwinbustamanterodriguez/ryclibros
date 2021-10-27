package com.juancarlos.ryclibros.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.juancarlos.ryclibros.domain.Localidad} entity.
 */
public class LocalidadDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    private ProvinciaDTO provincia;

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

    public ProvinciaDTO getProvincia() {
        return provincia;
    }

    public void setProvincia(ProvinciaDTO provincia) {
        this.provincia = provincia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocalidadDTO)) {
            return false;
        }

        LocalidadDTO localidadDTO = (LocalidadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, localidadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocalidadDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", provincia='" + getProvincia() + "'" +
            "}";
    }
}
