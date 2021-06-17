package com.juancarlos.ryclibros.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.juancarlos.ryclibros.domain.Libro} entity.
 */
public class LibroDTO implements Serializable {

    private Long id;

    @NotNull
    private String numero;

    private String observaciones;

    private CategoriaDTO categoria;

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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public CategoriaDTO getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDTO categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LibroDTO)) {
            return false;
        }

        LibroDTO libroDTO = (LibroDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, libroDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LibroDTO{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            ", categoria=" + getCategoria() +
            "}";
    }
}
