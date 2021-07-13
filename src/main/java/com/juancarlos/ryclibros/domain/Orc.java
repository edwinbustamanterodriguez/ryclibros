package com.juancarlos.ryclibros.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Orc.
 */
@Entity
@Table(name = "orc")
public class Orc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orc_id_pk", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "orc_numero", nullable = false, unique = true)
    private String numero;

    @ManyToMany
    @JoinTable(
        name = "rel_orc__persona",
        joinColumns = @JoinColumn(name = "orc_id_pk"),
        inverseJoinColumns = @JoinColumn(name = "persona_id_pk")
    )
    @JsonIgnoreProperties(value = { "orcs" }, allowSetters = true)
    private Set<Persona> personas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Orc id(Long id) {
        this.id = id;
        return this;
    }

    public String getNumero() {
        return this.numero;
    }

    public Orc numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Set<Persona> getPersonas() {
        return this.personas;
    }

    public Orc personas(Set<Persona> personas) {
        this.setPersonas(personas);
        return this;
    }

    public Orc addPersona(Persona persona) {
        this.personas.add(persona);
        persona.getOrcs().add(this);
        return this;
    }

    public Orc removePersona(Persona persona) {
        this.personas.remove(persona);
        persona.getOrcs().remove(this);
        return this;
    }

    public void setPersonas(Set<Persona> personas) {
        this.personas = personas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Orc)) {
            return false;
        }
        return id != null && id.equals(((Orc) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Orc{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            "}";
    }
}
