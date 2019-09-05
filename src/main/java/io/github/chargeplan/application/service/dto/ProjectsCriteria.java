package io.github.chargeplan.application.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Projects entity. This class is used in ProjectsResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /projects?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProjectsCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nameProject;

    private StringFilter projectCode;

    private StringFilter client;

    private StringFilter dP;

    private StringFilter description;

    private LongFilter affectationId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNameProject() {
        return nameProject;
    }

    public void setNameProject(StringFilter nameProject) {
        this.nameProject = nameProject;
    }

    public StringFilter getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(StringFilter projectCode) {
        this.projectCode = projectCode;
    }

    public StringFilter getClient() {
        return client;
    }

    public void setClient(StringFilter client) {
        this.client = client;
    }

    public StringFilter getdP() {
        return dP;
    }

    public void setdP(StringFilter dP) {
        this.dP = dP;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getAffectationId() {
        return affectationId;
    }

    public void setAffectationId(LongFilter affectationId) {
        this.affectationId = affectationId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProjectsCriteria that = (ProjectsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nameProject, that.nameProject) &&
            Objects.equals(projectCode, that.projectCode) &&
            Objects.equals(client, that.client) &&
            Objects.equals(dP, that.dP) &&
            Objects.equals(description, that.description) &&
            Objects.equals(affectationId, that.affectationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nameProject,
        projectCode,
        client,
        dP,
        description,
        affectationId
        );
    }

    @Override
    public String toString() {
        return "ProjectsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nameProject != null ? "nameProject=" + nameProject + ", " : "") +
                (projectCode != null ? "projectCode=" + projectCode + ", " : "") +
                (client != null ? "client=" + client + ", " : "") +
                (dP != null ? "dP=" + dP + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (affectationId != null ? "affectationId=" + affectationId + ", " : "") +
            "}";
    }

}
