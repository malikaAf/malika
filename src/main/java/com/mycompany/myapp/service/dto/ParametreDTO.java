package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Parametre} entity.
 */
public class ParametreDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2)
    private String libelle;

    @DecimalMin(value = "1")
    private Double tva;

    @NotNull
    private Boolean deleted;

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

    public Double getTva() {
        return tva;
    }

    public void setTva(Double tva) {
        this.tva = tva;
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
        if (!(o instanceof ParametreDTO)) {
            return false;
        }

        ParametreDTO parametreDTO = (ParametreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, parametreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParametreDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", tva=" + getTva() +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
