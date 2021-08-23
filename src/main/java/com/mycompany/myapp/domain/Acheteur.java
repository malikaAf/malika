package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.TypeAcheteur;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Acheteur.
 */
@Entity
@Table(name = "acheteur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Acheteur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_client")
    private TypeAcheteur typeClient;

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

    @OneToMany(mappedBy = "acheteur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "entree", "acheteur" }, allowSetters = true)
    private Set<Sortie> sorties = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Acheteur id(Long id) {
        this.id = id;
        return this;
    }

    public TypeAcheteur getTypeClient() {
        return this.typeClient;
    }

    public Acheteur typeClient(TypeAcheteur typeClient) {
        this.typeClient = typeClient;
        return this;
    }

    public void setTypeClient(TypeAcheteur typeClient) {
        this.typeClient = typeClient;
    }

    public String getNom() {
        return this.nom;
    }

    public Acheteur nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Acheteur prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTel() {
        return this.tel;
    }

    public Acheteur tel(String tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCnib() {
        return this.cnib;
    }

    public Acheteur cnib(String cnib) {
        this.cnib = cnib;
        return this;
    }

    public void setCnib(String cnib) {
        this.cnib = cnib;
    }

    public String getEmail() {
        return this.email;
    }

    public Acheteur email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Acheteur adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNumroBanquaire() {
        return this.numroBanquaire;
    }

    public Acheteur numroBanquaire(String numroBanquaire) {
        this.numroBanquaire = numroBanquaire;
        return this;
    }

    public void setNumroBanquaire(String numroBanquaire) {
        this.numroBanquaire = numroBanquaire;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Acheteur deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Sortie> getSorties() {
        return this.sorties;
    }

    public Acheteur sorties(Set<Sortie> sorties) {
        this.setSorties(sorties);
        return this;
    }

    public Acheteur addSorties(Sortie sortie) {
        this.sorties.add(sortie);
        sortie.setAcheteur(this);
        return this;
    }

    public Acheteur removeSorties(Sortie sortie) {
        this.sorties.remove(sortie);
        sortie.setAcheteur(null);
        return this;
    }

    public void setSorties(Set<Sortie> sorties) {
        if (this.sorties != null) {
            this.sorties.forEach(i -> i.setAcheteur(null));
        }
        if (sorties != null) {
            sorties.forEach(i -> i.setAcheteur(this));
        }
        this.sorties = sorties;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Acheteur)) {
            return false;
        }
        return id != null && id.equals(((Acheteur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Acheteur{" +
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
