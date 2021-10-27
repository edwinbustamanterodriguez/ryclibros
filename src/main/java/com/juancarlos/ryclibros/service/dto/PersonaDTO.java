package com.juancarlos.ryclibros.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.juancarlos.ryclibros.domain.Persona} entity.
 */
public class PersonaDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    private String apaterno;

    private String amaterno;

    @NotNull
    private String ci;

    @NotNull
    private String expedicion;

    private String telefono;

    private String institucion;

    @NotNull
    private Boolean esOficialDeRegistro;

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

    public String getApaterno() {
        return apaterno;
    }

    public void setApaterno(String apaterno) {
        this.apaterno = apaterno;
    }

    public String getAmaterno() {
        return amaterno;
    }

    public void setAmaterno(String amaterno) {
        this.amaterno = amaterno;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getExpedicion() {
        return expedicion;
    }

    public void setExpedicion(String expedicion) {
        this.expedicion = expedicion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public Boolean getEsOficialDeRegistro() {
        return esOficialDeRegistro;
    }

    public void setEsOficialDeRegistro(Boolean esOficialDeRegistro) {
        this.esOficialDeRegistro = esOficialDeRegistro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonaDTO)) {
            return false;
        }

        PersonaDTO personaDTO = (PersonaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, personaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apaterno='" + getApaterno() + "'" +
            ", amaterno='" + getAmaterno() + "'" +
            ", ci=" + getCi() + "'" +
            ", expedicion='" + getExpedicion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", institucion='" + getInstitucion() + "'" +
            ", esOficialDeRegistro='" + getEsOficialDeRegistro() + "'" +
            "}";
    }
}
