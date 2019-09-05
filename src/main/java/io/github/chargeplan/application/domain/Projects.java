package io.github.chargeplan.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Projects.
 */
@Entity
@Table(name = "projects")
@Document(indexName = "projects")
public class Projects extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name_project", nullable = false)
    private String nameProject;

    @NotNull
    @Column(name = "project_code", nullable = false)
    private String projectCode;

    @Column(name = "client")
    private String client;

    @Column(name = "d_p")
    private String dP;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "project")
    private Set<Affectations> affectations = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameProject() {
        return nameProject;
    }

    public Projects nameProject(String nameProject) {
        this.nameProject = nameProject;
        return this;
    }

    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public Projects projectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getClient() {
        return client;
    }

    public Projects client(String client) {
        this.client = client;
        return this;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getdP() {
        return dP;
    }

    public Projects dP(String dP) {
        this.dP = dP;
        return this;
    }

    public void setdP(String dP) {
        this.dP = dP;
    }

    public String getDescription() {
        return description;
    }

    public Projects description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Affectations> getAffectations() {
        return affectations;
    }

    public Projects affectations(Set<Affectations> affectations) {
        this.affectations = affectations;
        return this;
    }

    public Projects addAffectation(Affectations affectations) {
        this.affectations.add(affectations);
        affectations.setProject(this);
        return this;
    }

    public Projects removeAffectation(Affectations affectations) {
        this.affectations.remove(affectations);
        affectations.setProject(null);
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
        Projects projects = (Projects) o;
        if (projects.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projects.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Projects{" +
            "id=" + getId() +
            ", nameProject='" + getNameProject() + "'" +
            ", projectCode='" + getProjectCode() + "'" +
            ", client='" + getClient() + "'" +
            ", dP='" + getdP() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
