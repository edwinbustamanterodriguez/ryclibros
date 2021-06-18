package com.juancarlos.ryclibros.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Persona.
 */
@Entity
@Table(name = "persona")
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "persona_id_pk")
    private Long id;

    @NotNull
    @Column(name = "persona_nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "persona_apaterno", nullable = false)
    private String apaterno;

    @Column(name = "persona_amaterno")
    private String amaterno;

    @NotNull
    @Column(name = "persona_ci", nullable = false)
    private Integer ci;

    @NotNull
    @Column(name = "persona_expedicion", nullable = false)
    private String expedicion;

    @Column(name = "persona_telefono")
    private String telefono;

    @Column(name = "persona_institucion")
    private String institucion;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Persona id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Persona nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApaterno() {
        return this.apaterno;
    }

    public Persona apaterno(String apaterno) {
        this.apaterno = apaterno;
        return this;
    }

    public void setApaterno(String apaterno) {
        this.apaterno = apaterno;
    }

    public String getAmaterno() {
        return this.amaterno;
    }

    public Persona amaterno(String amaterno) {
        this.amaterno = amaterno;
        return this;
    }

    public void setAmaterno(String amaterno) {
        this.amaterno = amaterno;
    }

    public Integer getCi() {
        return this.ci;
    }

    public Persona ci(Integer ci) {
        this.ci = ci;
        return this;
    }

    public void setCi(Integer ci) {
        this.ci = ci;
    }

    public String getExpedicion() {
        return this.expedicion;
    }

    public Persona expedicion(String expedicion) {
        this.expedicion = expedicion;
        return this;
    }

    public void setExpedicion(String expedicion) {
        this.expedicion = expedicion;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Persona telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getInstitucion() {
        return this.institucion;
    }

    public Persona institucion(String institucion) {
        this.institucion = institucion;
        return this;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Persona)) {
            return false;
        }
        return id != null && id.equals(((Persona) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Persona{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apaterno='" + getApaterno() + "'" +
            ", amaterno='" + getAmaterno() + "'" +
            ", ci=" + getCi() +
            ", expedicion='" + getExpedicion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", institucion='" + getInstitucion() + "'" +
            "}";
    }
}
