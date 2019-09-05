package io.github.chargeplan.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import io.github.chargeplan.application.domain.enumeration.Skill;

/**
 * A Collaborators.
 */
@Entity
@Table(name = "collaborators")
@Document(indexName = "collaborators")
public class Collaborators extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_collaborator", nullable = false)
    private String nomCollaborator;

    @Column(name = "prenom_collaborator")
    private String prenomCollaborator;

    @Column(name = "trigramme")
    private String trigramme;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "competencies")
    private Skill competencies;

    @OneToMany(mappedBy = "collaborator")
    private Set<Affectations> affectations = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomCollaborator() {
        return nomCollaborator;
    }

    public Collaborators nomCollaborator(String nomCollaborator) {
        this.nomCollaborator = nomCollaborator;
        return this;
    }

    public void setNomCollaborator(String nomCollaborator) {
        this.nomCollaborator = nomCollaborator;
    }

    public String getPrenomCollaborator() {
        return prenomCollaborator;
    }

    public Collaborators prenomCollaborator(String prenomCollaborator) {
        this.prenomCollaborator = prenomCollaborator;
        return this;
    }

    public void setPrenomCollaborator(String prenomCollaborator) {
        this.prenomCollaborator = prenomCollaborator;
    }

    public String getTrigramme() {
        return trigramme;
    }

    public Collaborators trigramme(String trigramme) {
        this.trigramme = trigramme;
        return this;
    }

    public void setTrigramme(String trigramme) {
        this.trigramme = trigramme;
    }

    public String getEmail() {
        return email;
    }

    public Collaborators email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Skill getCompetencies() {
        return competencies;
    }

    public Collaborators competencies(Skill competencies) {
        this.competencies = competencies;
        return this;
    }

    public void setCompetencies(Skill competencies) {
        this.competencies = competencies;
    }

    public Set<Affectations> getAffectations() {
        return affectations;
    }

    public Collaborators affectations(Set<Affectations> affectations) {
        this.affectations = affectations;
        return this;
    }

    public Collaborators addAffectation(Affectations affectations) {
        this.affectations.add(affectations);
        affectations.setCollaborator(this);
        return this;
    }

    public Collaborators removeAffectation(Affectations affectations) {
        this.affectations.remove(affectations);
        affectations.setCollaborator(null);
        return this;
    }

    public void setAffectations(Set<Affectations> affectations) {
        this.affectations = affectations;
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
        Collaborators collaborators = (Collaborators) o;
        if (collaborators.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), collaborators.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Collaborators{" +
            "id=" + getId() +
            ", nomCollaborator='" + getNomCollaborator() + "'" +
            ", prenomCollaborator='" + getPrenomCollaborator() + "'" +
            ", trigramme='" + getTrigramme() + "'" +
            ", email='" + getEmail() + "'" +
            ", competencies='" + getCompetencies() + "'" +
            "}";
    }
}
