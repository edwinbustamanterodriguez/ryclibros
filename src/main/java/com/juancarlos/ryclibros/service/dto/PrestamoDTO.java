package com.juancarlos.ryclibros.service.dto;

import java.io.Serializable;
import java.time.Instant;
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

    @NotNull
    private Boolean devuelto;

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

    public Boolean getDevuelto() {
        return devuelto;
    }

    public void setDevuelto(Boolean devuelto) {
        this.devuelto = devuelto;
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
            ", devuelto='" + getDevuelto() + "'" +
            ", createdBy=" + createdBy +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            "}";
    }
}
