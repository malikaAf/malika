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
 * A Exercice.
 */
@Entity
@Table(name = "exercice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Exercice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Min(value = 2)
    @Column(name = "annee")
    private Integer annee;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @OneToMany(mappedBy = "exercice")
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

    public Exercice id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getAnnee() {
        return this.annee;
    }

    public Exercice annee(Integer annee) {
        this.annee = annee;
        return this;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Exercice deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Entree> getEntrees() {
        return this.entrees;
    }

    public Exercice entrees(Set<Entree> entrees) {
        this.setEntrees(entrees);
        return this;
    }

    public Exercice addEntrees(Entree entree) {
        this.entrees.add(entree);
        entree.setExercice(this);
        return this;
    }

    public Exercice removeEntrees(Entree entree) {
        this.entrees.remove(entree);
        entree.setExercice(null);
        return this;
    }

    public void setEntrees(Set<Entree> entrees) {
        if (this.entrees != null) {
            this.entrees.forEach(i -> i.setExercice(null));
        }
        if (entrees != null) {
            entrees.forEach(i -> i.setExercice(this));
        }
        this.entrees = entrees;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exercice)) {
            return false;
        }
        return id != null && id.equals(((Exercice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Exercice{" +
            "id=" + getId() +
            ", annee=" + getAnnee() +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
