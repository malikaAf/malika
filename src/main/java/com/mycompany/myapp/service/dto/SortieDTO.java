package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Sortie} entity.
 */
public class SortieDTO implements Serializable {

    private Long id;

    private String libelle;

    @NotNull
    @DecimalMin(value = "1")
    private Double quantite;

    @NotNull
    @DecimalMin(value = "1")
    private Double prixUnitaireTTC;

    private LocalDate dateSortie;

    @Lob
    private byte[] bordereau;

    private String bordereauContentType;

    @NotNull
    @Size(min = 10, max = 1024)
    private String observation;

    @NotNull
    private Boolean deleted;

    private EntreeDTO entree;

    private AcheteurDTO acheteur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(LocalDate dateSortie) {
        this.dateSortie = dateSortie;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EntreeDTO getEntree() {
        return entree;
    }

    public void setEntree(EntreeDTO entree) {
        this.entree = entree;
    }

    public AcheteurDTO getAcheteur() {
        return acheteur;
    }

    public void setAcheteur(AcheteurDTO acheteur) {
        this.acheteur = acheteur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SortieDTO)) {
            return false;
        }

        SortieDTO sortieDTO = (SortieDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sortieDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SortieDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", quantite=" + getQuantite() +
            ", prixUnitaireTTC=" + getPrixUnitaireTTC() +
            ", dateSortie='" + getDateSortie() + "'" +
            ", bordereau='" + getBordereau() + "'" +
            ", observation='" + getObservation() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", entree=" + getEntree() +
            ", acheteur=" + getAcheteur() +
            "}";
    }
}
