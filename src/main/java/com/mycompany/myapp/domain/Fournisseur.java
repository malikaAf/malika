package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.TypeFournisseur;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Fournisseur.
 */
@Entity
@Table(name = "fournisseur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Fournisseur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_client")
    private TypeFournisseur typeClient;

    @NotNull
    @Size(min = 2)
    @Column(name = "nom", nullable = false)
    private String nom;

    @Size(min = 2)
    @Column(name = "prenom")
    private String prenom;

    @NotNull
    @Size(min = 6)
    @Column(name = "tel", nullable = false, unique = true)
    private String tel;

    @NotNull
    @Size(min = 4)
    @Column(name = "cnib", nullable = false, unique = true)
    private String cnib;

    @NotNull
    @Size(min = 4)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Size(min = 2)
    @Column(name = "adresse")
    private String adresse;

    @Size(min = 4)
    @Column(name = "numro_banquaire", unique = true)
    private String numroBanquaire;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @OneToMany(mappedBy = "fournisseur")
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

    public Fournisseur id(Long id) {
        this.id = id;
        return this;
    }

    public TypeFournisseur getTypeClient() {
        return this.typeClient;
    }

    public Fournisseur typeClient(TypeFournisseur typeClient) {
        this.typeClient = typeClient;
        return this;
    }

    public void setTypeClient(TypeFournisseur typeClient) {
        this.typeClient = typeClient;
    }

    public String getNom() {
        return this.nom;
    }

    public Fournisseur nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Fournisseur prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTel() {
        return this.tel;
    }

    public Fournisseur tel(String tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCnib() {
        return this.cnib;
    }

    public Fournisseur cnib(String cnib) {
        this.cnib = cnib;
        return this;
    }

    public void setCnib(String cnib) {
        this.cnib = cnib;
    }

    public String getEmail() {
        return this.email;
    }

    public Fournisseur email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Fournisseur adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNumroBanquaire() {
        return this.numroBanquaire;
    }

    public Fournisseur numroBanquaire(String numroBanquaire) {
        this.numroBanquaire = numroBanquaire;
        return this;
    }

    public void setNumroBanquaire(String numroBanquaire) {
        this.numroBanquaire = numroBanquaire;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Fournisseur deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Entree> getEntrees() {
        return this.entrees;
    }

    public Fournisseur entrees(Set<Entree> entrees) {
        this.setEntrees(entrees);
        return this;
    }

    public Fournisseur addEntrees(Entree entree) {
        this.entrees.add(entree);
        entree.setFournisseur(this);
        return this;
    }

    public Fournisseur removeEntrees(Entree entree) {
        this.entrees.remove(entree);
        entree.setFournisseur(null);
        return this;
    }

    public void setEntrees(Set<Entree> entrees) {
        if (this.entrees != null) {
            this.entrees.forEach(i -> i.setFournisseur(null));
        }
        if (entrees != null) {
            entrees.forEach(i -> i.setFournisseur(this));
        }
        this.entrees = entrees;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fournisseur)) {
            return false;
        }
        return id != null && id.equals(((Fournisseur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fournisseur{" +
            "id=" + getId() +
            ", typeClient='" + getTypeClient() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", tel='" + getTel() + "'" +
            ", cnib='" + getCnib() + "'" +
            ", email='" + getEmail() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", numroBanquaire='" + getNumroBanquaire() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
