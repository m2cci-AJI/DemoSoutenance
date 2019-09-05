package io.github.chargeplan.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import io.github.chargeplan.application.domain.enumeration.Colors;

/**
 * A Affectations.
 */
@Entity
@Table(name = "affectations")
@Document(indexName = "affectations")
public class Affectations extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "charge")
    private Integer charge;

    @Column(name = "commentaire")
    private String commentaire;

    @Enumerated(EnumType.STRING)
    @Column(name = "color")
    private Colors color;

    @ManyToOne
    @JsonIgnoreProperties("affectations")
    private Collaborators collaborator;

    @ManyToOne
    @JsonIgnoreProperties("affectations")
    private Projects project;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public Affectations dateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public Affectations dateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Integer getCharge() {
        return charge;
    }

    public Affectations charge(Integer charge) {
        this.charge = charge;
        return this;
    }

    public void setCharge(Integer charge) {
        this.charge = charge;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public Affectations commentaire(String commentaire) {
        this.commentaire = commentaire;
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Colors getColor() {
        return color;
    }

    public Affectations color(Colors color) {
        this.color = color;
        return this;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public Collaborators getCollaborator() {
        return collaborator;
    }

    public Affectations collaborator(Collaborators collaborators) {
        this.collaborator = collaborators;
        return this;
    }

    public void setCollaborator(Collaborators collaborators) {
        this.collaborator = collaborators;
    }

    public Projects getProject() {
        return project;
    }

    public Affectations project(Projects projects) {
        this.project = projects;
        return this;
    }

    public void setProject(Projects projects) {
        this.project = projects;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Affectations affectations = (Affectations) o;
        if (affectations.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), affectations.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Affectations{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", charge=" + getCharge() +
            ", commentaire='" + getCommentaire() + "'" +
            ", color='" + getColor() + "'" +
            "}";
    }
}
