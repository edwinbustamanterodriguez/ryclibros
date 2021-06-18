package com.juancarlos.ryclibros.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Libro.
 */
@Entity
@Table(name = "libro")
public class Libro extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "libro_id_pk")
    private Long id;

    @NotNull
    @Column(name = "libro_numero", nullable = false, unique = true)
    private String numero;

    @Column(name = "libro_observaciones")
    private String observaciones;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "libro_categoria_id_fk", nullable = false)
    private Categoria categoria;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Libro id(Long id) {
        this.id = id;
        return this;
    }

    public String getNumero() {
        return this.numero;
    }

    public Libro numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Libro observaciones(String observaciones) {
        this.observaciones = observaciones;
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public Libro categoria(Categoria categoria) {
        this.setCategoria(categoria);
        return this;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Libro)) {
            return false;
        }
        return id != null && id.equals(((Libro) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Libro{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            "}";
    }
}
