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
 * A Article.
 */
@Entity
@Table(name = "article")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "libelle", nullable = false, unique = true)
    private String libelle;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @OneToMany(mappedBy = "article")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sorties", "article", "fournisseur", "exercice", "parametre" }, allowSetters = true)
    private Set<Entree> entrees = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "articles", "category" }, allowSetters = true)
    private Mark mark;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Article id(Long id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Article libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Article deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Entree> getEntrees() {
        return this.entrees;
    }

    public Article entrees(Set<Entree> entrees) {
        this.setEntrees(entrees);
        return this;
    }

    public Article addEntrees(Entree entree) {
        this.entrees.add(entree);
        entree.setArticle(this);
        return this;
    }

    public Article removeEntrees(Entree entree) {
        this.entrees.remove(entree);
        entree.setArticle(null);
        return this;
    }

    public void setEntrees(Set<Entree> entrees) {
        if (this.entrees != null) {
            this.entrees.forEach(i -> i.setArticle(null));
        }
        if (entrees != null) {
            entrees.forEach(i -> i.setArticle(this));
        }
        this.entrees = entrees;
    }

    public Mark getMark() {
        return this.mark;
    }

    public Article mark(Mark mark) {
        this.setMark(mark);
        return this;
    }

    public void setMark(Mark mark) {
        this.mark = mark;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        return id != null && id.equals(((Article) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Article{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
