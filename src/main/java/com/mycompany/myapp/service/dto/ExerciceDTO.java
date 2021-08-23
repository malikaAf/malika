package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Exercice} entity.
 */
public class ExerciceDTO implements Serializable {

    private Long id;

    @Min(value = 2)
    private Integer annee;

    @NotNull
    private Boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
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
        if (!(o instanceof ExerciceDTO)) {
            return false;
        }

        ExerciceDTO exerciceDTO = (ExerciceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, exerciceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExerciceDTO{" +
            "id=" + getId() +
            ", annee=" + getAnnee() +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
