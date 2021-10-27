package com.juancarlos.ryclibros.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.juancarlos.ryclibros.domain.Devolucion} entity.
 */
public class DevolucionDTO implements Serializable {

    private Long id;

    private String observaciones;

    private UserDTO user;

    private PersonaDTO persona;

    private PrestamoDTO prestamo;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public PersonaDTO getPersona() {
        return persona;
    }

    public void setPersona(PersonaDTO persona) {
        this.persona = persona;
    }

    public PrestamoDTO getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(PrestamoDTO prestamo) {
        this.prestamo = prestamo;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DevolucionDTO)) {
            return false;
        }

        DevolucionDTO devolucionDTO = (DevolucionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, devolucionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DevolucionDTO{" +
            "id=" + getId() +
            ", observaciones='" + getObservaciones() + "'" +
            ", user=" + getUser() +
            ", persona=" + getPersona() +
            ", prestamo=" + getPrestamo() +
            "}";
    }
}
