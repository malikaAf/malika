package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Entree.
 */
@Entity
@Table(name = "entree")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Entree implements Serializable {

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

    @Column(name = "restant")
    private Double restant;

    @NotNull
    @Column(name = "serie", nullable = false)
    private String serie;

    @NotNull
    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "caract_supplementaire")
    private String caractSupplementaire;

    @Column(name = "date_entree")
    private LocalDate dateEntree;

    @Lob
    @Column(name = "bordereau")
    private byte[] bordereau;

    @Column(name = "bordereau_content_type")
    private String bordereauContentType;

    @NotNull
    @Size(min = 10, max = 1024)
    @Column(name = "observation", length = 1024, nullable = false)
    private String observation;

    @Column(name = "en_stock")
    private Boolean enStock;

    @Column(name = "en_commande")
    private Boolean enCommande;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @OneToMany(mappedBy = "entree")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "entree", "acheteur" }, allowSetters = true)
    private Set<Sortie> sorties = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "entrees", "mark" }, allowSetters = true)
    private Article article;

    @ManyToOne
    @JsonIgnoreProperties(value = { "entrees" }, allowSetters = true)
    private Fournisseur fournisseur;

    @ManyToOne
    @JsonIgnoreProperties(value = { "entrees" }, allowSetters = true)
    private Exercice exercice;

    @ManyToOne
    @JsonIgnoreProperties(value = { "entrees" }, allowSetters = true)
    private Parametre parametre;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Entree id(Long id) {
        this.id = id;
        return this;
    }

    public Double getRestant() {
        return restant;
    }

    public void setRestant(Double restant) {
        this.restant = restant;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Entree libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getQuantite() {
        return this.quantite;
    }

    public Entree quantite(Double quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Double getPrixUnitaireTTC() {
        return this.prixUnitaireTTC;
    }

    public Entree prixUnitaireTTC(Double prixUnitaireTTC) {
        this.prixUnitaireTTC = prixUnitaireTTC;
        return this;
    }

    public void setPrixUnitaireTTC(Double prixUnitaireTTC) {
        this.prixUnitaireTTC = prixUnitaireTTC;
    }

    public String getSerie() {
        return this.serie;
    }

    public Entree serie(String serie) {
        this.serie = serie;
        return this;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getModel() {
        return this.model;
    }

    public Entree model(String model) {
        this.model = model;
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCaractSupplementaire() {
        return this.caractSupplementaire;
    }

    public Entree caractSupplementaire(String caractSupplementaire) {
        this.caractSupplementaire = caractSupplementaire;
        return this;
    }

    public void setCaractSupplementaire(String caractSupplementaire) {
        this.caractSupplementaire = caractSupplementaire;
    }

    public LocalDate getDateEntree() {
        return this.dateEntree;
    }

    public Entree dateEntree(LocalDate dateEntree) {
        this.dateEntree = dateEntree;
        return this;
    }

    public void setDateEntree(LocalDate dateEntree) {
        this.dateEntree = dateEntree;
    }

    public byte[] getBordereau() {
        return this.bordereau;
    }

    public Entree bordereau(byte[] bordereau) {
        this.bordereau = bordereau;
        return this;
    }

    public void setBordereau(byte[] bordereau) {
        this.bordereau = bordereau;
    }

    public String getBordereauContentType() {
        return this.bordereauContentType;
    }

    public Entree bordereauContentType(String bordereauContentType) {
        this.bordereauContentType = bordereauContentType;
        return this;
    }

    public void setBordereauContentType(String bordereauContentType) {
        this.bordereauContentType = bordereauContentType;
    }

    public String getObservation() {
        return this.observation;
    }

    public Entree observation(String observation) {
        this.observation = observation;
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Boolean getEnStock() {
        return this.enStock;
    }

    public Entree enStock(Boolean enStock) {
        this.enStock = enStock;
        return this;
    }

    public void setEnStock(Boolean enStock) {
        this.enStock = enStock;
    }

    public Boolean getEnCommande() {
        return this.enCommande;
    }

    public Entree enCommande(Boolean enCommande) {
        this.enCommande = enCommande;
        return this;
    }

    public void setEnCommande(Boolean enCommande) {
        this.enCommande = enCommande;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Entree deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Sortie> getSorties() {
        return this.sorties;
    }

    public Entree sorties(Set<Sortie> sorties) {
        this.setSorties(sorties);
        return this;
    }

    public Entree addSorties(Sortie sortie) {
        this.sorties.add(sortie);
        sortie.setEntree(this);
        return this;
    }

    public Entree removeSorties(Sortie sortie) {
        this.sorties.remove(sortie);
        sortie.setEntree(null);
        return this;
    }

    public void setSorties(Set<Sortie> sorties) {
        if (this.sorties != null) {
            this.sorties.forEach(i -> i.setEntree(null));
        }
        if (sorties != null) {
            sorties.forEach(i -> i.setEntree(this));
        }
        this.sorties = sorties;
    }

    public Article getArticle() {
        return this.article;
    }

    public Entree article(Article article) {
        this.setArticle(article);
        return this;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Fournisseur getFournisseur() {
        return this.fournisseur;
    }

    public Entree fournisseur(Fournisseur fournisseur) {
        this.setFournisseur(fournisseur);
        return this;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Exercice getExercice() {
        return this.exercice;
    }

    public Entree exercice(Exercice exercice) {
        this.setExercice(exercice);
        return this;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }

    public Parametre getParametre() {
        return this.parametre;
    }

    public Entree parametre(Parametre parametre) {
        this.setParametre(parametre);
        return this;
    }

    public void setParametre(Parametre parametre) {
        this.parametre = parametre;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entree)) {
            return false;
        }
        return id != null && id.equals(((Entree) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Entree{" +
            "id=" +
            id +
            ", libelle='" +
            libelle +
            '\'' +
            ", quantite=" +
            quantite +
            ", prixUnitaireTTC=" +
            prixUnitaireTTC +
            ", restant=" +
            restant +
            ", serie='" +
            serie +
            '\'' +
            ", model='" +
            model +
            '\'' +
            ", caractSupplementaire='" +
            caractSupplementaire +
            '\'' +
            ", dateEntree=" +
            dateEntree +
            ", bordereau=" +
            Arrays.toString(bordereau) +
            ", bordereauContentType='" +
            bordereauContentType +
            '\'' +
            ", observation='" +
            observation +
            '\'' +
            ", enStock=" +
            enStock +
            ", enCommande=" +
            enCommande +
            ", deleted=" +
            deleted +
            '}'
        );
    }
}
