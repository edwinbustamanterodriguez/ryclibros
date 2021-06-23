package com.juancarlos.ryclibros.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.juancarlos.ryclibros.domain.Prestamo} entity.
 */
public class PrestamoDTO implements Serializable {

    private Long id;

    private String observaciones;

    @NotNull
    private ZonedDateTime fechaFin;

    private LibroDTO libro;

    private PersonaDTO persona;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public ZonedDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(ZonedDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public LibroDTO getLibro() {
        return libro;
    }

    public void setLibro(LibroDTO libro) {
        this.libro = libro;
    }

    public PersonaDTO getPersona() {
        return persona;
    }

    public void setPersona(PersonaDTO persona) {
        this.persona = persona;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrestamoDTO)) {
            return false;
        }

        PrestamoDTO prestamoDTO = (PrestamoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prestamoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrestamoDTO{" +
            "id=" + getId() +
            ", observaciones='" + getObservaciones() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", libro=" + getLibro() +
            ", persona=" + getPersona() +
            ", user=" + getUser() +
            "}";
    }
}
