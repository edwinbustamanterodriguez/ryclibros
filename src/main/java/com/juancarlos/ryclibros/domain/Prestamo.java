package com.juancarlos.ryclibros.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Prestamo.
 */
@Entity
@Table(name = "prestamo")
public class Prestamo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prestamo_id_pk")
    private Long id;

    @Column(name = "prestamo_observaciones")
    private String observaciones;

    @NotNull
    @Column(name = "prestamo_fecha_fin", nullable = false)
    private ZonedDateTime fechaFin;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "categoria" }, allowSetters = true)
    @JoinColumn(name = "prestamo_libro_id_fk", nullable = false)
    private Libro libro;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "prestamo_persona_id_fk", nullable = false)
    private Persona persona;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "prestamo_user_id_fk", nullable = false)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prestamo id(Long id) {
        this.id = id;
        return this;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Prestamo observaciones(String observaciones) {
        this.observaciones = observaciones;
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public ZonedDateTime getFechaFin() {
        return this.fechaFin;
    }

    public Prestamo fechaFin(ZonedDateTime fechaFin) {
        this.fechaFin = fechaFin;
        return this;
    }

    public void setFechaFin(ZonedDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Libro getLibro() {
        return this.libro;
    }

    public Prestamo libro(Libro libro) {
        this.setLibro(libro);
        return this;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Persona getPersona() {
        return this.persona;
    }

    public Prestamo persona(Persona persona) {
        this.setPersona(persona);
        return this;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public User getUser() {
        return this.user;
    }

    public Prestamo user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Prestamo)) {
            return false;
        }
        return id != null && id.equals(((Prestamo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prestamo{" +
            "id=" + getId() +
            ", observaciones='" + getObservaciones() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            "}";
    }
}
