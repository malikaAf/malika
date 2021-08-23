package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Entree} entity.
 */
public class EntreeDTO implements Serializable {

    private Long id;

    private String libelle;

    @NotNull
    @DecimalMin(value = "1")
    private Double quantite;

    @NotNull
    @DecimalMin(value = "1")
    private Double prixUnitaireTTC;

    private Double restant;

    @NotNull
    private String serie;

    @NotNull
    private String model;

    private String caractSupplementaire;

    private LocalDate dateEntree;

    @Lob
    private byte[] bordereau;

    private String bordereauContentType;

    @NotNull
    @Size(min = 10, max = 1024)
    private String observation;

    private Boolean enStock;

    private Boolean enCommande;

    @NotNull
    private Boolean deleted;

    private ArticleDTO article;

    private FournisseurDTO fournisseur;

    private ExerciceDTO exercice;

    private ParametreDTO parametre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRestant() {
        return restant;
    }

    public void setRestant(Double restant) {
        this.restant = restant;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Double getPrixUnitaireTTC() {
        return prixUnitaireTTC;
    }

    public void setPrixUnitaireTTC(Double prixUnitaireTTC) {
        this.prixUnitaireTTC = prixUnitaireTTC;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCaractSupplementaire() {
        return caractSupplementaire;
    }

    public void setCaractSupplementaire(String caractSupplementaire) {
        this.caractSupplementaire = caractSupplementaire;
    }

    public LocalDate getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(LocalDate dateEntree) {
        this.dateEntree = dateEntree;
    }

    public byte[] getBordereau() {
        return bordereau;
    }

    public void setBordereau(byte[] bordereau) {
        this.bordereau = bordereau;
    }

    public String getBordereauContentType() {
        return bordereauContentType;
    }

    public void setBordereauContentType(String bordereauContentType) {
        this.bordereauContentType = bordereauContentType;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Boolean getEnStock() {
        return enStock;
    }

    public void setEnStock(Boolean enStock) {
        this.enStock = enStock;
    }

    public Boolean getEnCommande() {
        return enCommande;
    }

    public void setEnCommande(Boolean enCommande) {
        this.enCommande = enCommande;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public ArticleDTO getArticle() {
        return article;
    }

    public void setArticle(ArticleDTO article) {
        this.article = article;
    }

    public FournisseurDTO getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(FournisseurDTO fournisseur) {
        this.fournisseur = fournisseur;
    }

    public ExerciceDTO getExercice() {
        return exercice;
    }

    public void setExercice(ExerciceDTO exercice) {
        this.exercice = exercice;
    }

    public ParametreDTO getParametre() {
        return parametre;
    }

    public void setParametre(ParametreDTO parametre) {
        this.parametre = parametre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntreeDTO)) {
            return false;
        }

        EntreeDTO entreeDTO = (EntreeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, entreeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EntreeDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", quantite=" + getQuantite() +
            ", prixUnitaireTTC=" + getPrixUnitaireTTC() +
            ", serie='" + getSerie() + "'" +
            ", model='" + getModel() + "'" +
            ", caractSupplementaire='" + getCaractSupplementaire() + "'" +
            ", dateEntree='" + getDateEntree() + "'" +
            ", bordereau='" + getBordereau() + "'" +
            ", observation='" + getObservation() + "'" +
            ", enStock='" + getEnStock() + "'" +
            ", enCommande='" + getEnCommande() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", article=" + getArticle() +
            ", fournisseur=" + getFournisseur() +
            ", exercice=" + getExercice() +
            ", parametre=" + getParametre() +
            "}";
    }
}
