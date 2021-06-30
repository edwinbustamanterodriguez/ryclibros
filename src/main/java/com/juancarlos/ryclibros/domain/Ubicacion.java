package com.juancarlos.ryclibros.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Ubicacion.
 */
@Entity
@Table(name = "ubicacion")
public class Ubicacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ubicacion_id_pk")
    private Long id;

    @NotNull
    @Column(name = "ubicacion_sector", nullable = false)
    private String sector;

    @NotNull
    @Column(name = "ubicacion_numero", nullable = false)
    private Integer numero;

    @NotNull
    @Column(name = "ubicacion_serie", nullable = false)
    private String serie;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ubicacion id(Long id) {
        this.id = id;
        return this;
    }

    public String getSector() {
        return this.sector;
    }

    public Ubicacion sector(String sector) {
        this.sector = sector;
        return this;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Integer getNumero() {
        return this.numero;
    }

    public Ubicacion numero(Integer numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getSerie() {
        return this.serie;
    }

    public Ubicacion serie(String serie) {
        this.serie = serie;
        return this;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ubicacion)) {
            return false;
        }
        return id != null && id.equals(((Ubicacion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ubicacion{" +
            "id=" + getId() +
            ", sector='" + getSector() + "'" +
            ", numero=" + getNumero() +
            ", serie='" + getSerie() + "'" +
            "}";
    }
}
