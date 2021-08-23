package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.TypeAcheteur;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Acheteur} entity.
 */
public class AcheteurDTO implements Serializable {

    private Long id;

    private TypeAcheteur typeClient;

    @NotNull
    @Size(min = 2)
    private String nom;

    @Size(min = 2)
    private String prenom;

    @NotNull
    @Size(min = 6)
    private String tel;

    @NotNull
    @Size(min = 4)
    private String cnib;

    @NotNull
    @Size(min = 4)
    private String email;

    @Size(min = 2)
    private String adresse;

    @Size(min = 4)
    private String numroBanquaire;

    @NotNull
    private Boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeAcheteur getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(TypeAcheteur typeClient) {
        this.typeClient = typeClient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCnib() {
        return cnib;
    }

    public void setCnib(String cnib) {
        this.cnib = cnib;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNumroBanquaire() {
        return numroBanquaire;
    }

    public void setNumroBanquaire(String numroBanquaire) {
        this.numroBanquaire = numroBanquaire;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AcheteurDTO)) {
            return false;
        }

        AcheteurDTO acheteurDTO = (AcheteurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, acheteurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AcheteurDTO{" +
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
