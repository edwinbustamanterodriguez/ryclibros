package com.juancarlos.ryclibros.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Devolucion.
 */
@Entity
@Table(name = "devolucion")
public class Devolucion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "devolucion_id_pk")
    private Long id;

    @Column(name = "devolucion_observaciones")
    private String observaciones;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "devolucion_user_id_fk", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "devolucion_persona_id_fk", nullable = false)
    private Persona persona;

    @JsonIgnoreProperties(value = { "libro", "persona", "user" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(name = "devolucion_prestamo_id_fk", unique = true)
    private Prestamo prestamo;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Devolucion id(Long id) {
        this.id = id;
        return this;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Devolucion observaciones(String observaciones) {
        this.observaciones = observaciones;
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public User getUser() {
        return this.user;
    }

    public Devolucion user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Persona getPersona() {
        return this.persona;
    }

    public Devolucion persona(Persona persona) {
        this.setPersona(persona);
        return this;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Prestamo getPrestamo() {
        return this.prestamo;
    }

    public Devolucion prestamo(Prestamo prestamo) {
        this.setPrestamo(prestamo);
        return this;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Devolucion)) {
            return false;
        }
        return id != null && id.equals(((Devolucion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Devolucion{" +
            "id=" + getId() +
            ", observaciones='" + getObservaciones() + "'" +
            "}";
    }
}
