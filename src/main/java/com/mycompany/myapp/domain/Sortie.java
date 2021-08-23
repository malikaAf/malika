package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Sortie.
 */
@Entity
@Table(name = "sortie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sortie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @NotNull
    @DecimalMin(value = "1")
    @Column(name = "quantite", nullable = false)
    private Double quantite;

    @NotNull
    @DecimalMin(value = "1")
    @Column(name = "prix_unitaire_ttc", nullable = false)
    private Double prixUnitaireTTC;

    @Column(name = "date_sortie")
    private LocalDate dateSortie;

    @Lob
    @Column(name = "bordereau")
    private byte[] bordereau;

    @Column(name = "bordereau_content_type")
    private String bordereauContentType;

    @NotNull
    @Size(min = 10, max = 1024)
    @Column(name = "observation", length = 1024, nullable = false)
    private String observation;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sorties", "article", "fournisseur", "exercice", "parametre" }, allowSetters = true)
    private Entree entree;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sorties" }, allowSetters = true)
    private Acheteur acheteur;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sortie id(Long id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Sortie libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getQuantite() {
        return this.quantite;
    }

    public Sortie quantite(Double quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Double getPrixUnitaireTTC() {
        return this.prixUnitaireTTC;
    }

    public Sortie prixUnitaireTTC(Double prixUnitaireTTC) {
        this.prixUnitaireTTC = prixUnitaireTTC;
        return this;
    }

    public void setPrixUnitaireTTC(Double prixUnitaireTTC) {
        this.prixUnitaireTTC = prixUnitaireTTC;
    }

    public LocalDate getDateSortie() {
        return this.dateSortie;
    }

    public Sortie dateSortie(LocalDate dateSortie) {
        this.dateSortie = dateSortie;
        return this;
    }

    public void setDateSortie(LocalDate dateSortie) {
        this.dateSortie = dateSortie;
    }

    public byte[] getBordereau() {
        return this.bordereau;
    }

    public Sortie bordereau(byte[] bordereau) {
        this.bordereau = bordereau;
        return this;
    }

    public void setBordereau(byte[] bordereau) {
        this.bordereau = bordereau;
    }

    public String getBordereauContentType() {
        return this.bordereauContentType;
    }

    public Sortie bordereauContentType(String bordereauContentType) {
        this.bordereauContentType = bordereauContentType;
        return this;
    }

    public void setBordereauContentType(String bordereauContentType) {
        this.bordereauContentType = bordereauContentType;
    }

    public String getObservation() {
        return this.observation;
    }

    public Sortie observation(String observation) {
        this.observation = observation;
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Sortie deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Entree getEntree() {
        return this.entree;
    }

    public Sortie entree(Entree entree) {
        this.setEntree(entree);
        return this;
    }

    public void setEntree(Entree entree) {
        this.entree = entree;
    }

    public Acheteur getAcheteur() {
        return this.acheteur;
    }

    public Sortie acheteur(Acheteur acheteur) {
        this.setAcheteur(acheteur);
        return this;
    }

    public void setAcheteur(Acheteur acheteur) {
        this.acheteur = acheteur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sortie)) {
            return false;
        }
        return id != null && id.equals(((Sortie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sortie{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", quantite=" + getQuantite() +
            ", prixUnitaireTTC=" + getPrixUnitaireTTC() +
            ", dateSortie='" + getDateSortie() + "'" +
            ", bordereau='" + getBordereau() + "'" +
            ", bordereauContentType='" + getBordereauContentType() + "'" +
            ", observation='" + getObservation() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
