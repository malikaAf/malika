package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Parametre.
 */
@Entity
@Table(name = "parametre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Parametre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @DecimalMin(value = "1")
    @Column(name = "tva")
    private Double tva;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @OneToMany(mappedBy = "parametre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sorties", "article", "fournisseur", "exercice", "parametre" }, allowSetters = true)
    private Set<Entree> entrees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Parametre id(Long id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Parametre libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getTva() {
        return this.tva;
    }

    public Parametre tva(Double tva) {
        this.tva = tva;
        return this;
    }

    public void setTva(Double tva) {
        this.tva = tva;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Parametre deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Entree> getEntrees() {
        return this.entrees;
    }

    public Parametre entrees(Set<Entree> entrees) {
        this.setEntrees(entrees);
        return this;
    }

    public Parametre addEntrees(Entree entree) {
        this.entrees.add(entree);
        entree.setParametre(this);
        return this;
    }

    public Parametre removeEntrees(Entree entree) {
        this.entrees.remove(entree);
        entree.setParametre(null);
        return this;
    }

    public void setEntrees(Set<Entree> entrees) {
        if (this.entrees != null) {
            this.entrees.forEach(i -> i.setParametre(null));
        }
        if (entrees != null) {
            entrees.forEach(i -> i.setParametre(this));
        }
        this.entrees = entrees;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parametre)) {
            return false;
        }
        return id != null && id.equals(((Parametre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Parametre{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", tva=" + getTva() +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
