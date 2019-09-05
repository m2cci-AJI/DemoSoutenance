package io.github.chargeplan.application.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.chargeplan.application.domain.enumeration.Colors;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the Affectations entity. This class is used in AffectationsResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /affectations?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AffectationsCriteria implements Serializable {
    /**
     * Class for filtering Colors
     */
    public static class ColorsFilter extends Filter<Colors> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dateDebut;

    private LocalDateFilter dateFin;

    private IntegerFilter charge;

    private StringFilter commentaire;

    private ColorsFilter color;

    private LongFilter collaboratorId;

    private LongFilter projectId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateFilter dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateFilter getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateFilter dateFin) {
        this.dateFin = dateFin;
    }

    public IntegerFilter getCharge() {
        return charge;
    }

    public void setCharge(IntegerFilter charge) {
        this.charge = charge;
    }

    public StringFilter getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(StringFilter commentaire) {
        this.commentaire = commentaire;
    }

    public ColorsFilter getColor() {
        return color;
    }

    public void setColor(ColorsFilter color) {
        this.color = color;
    }

    public LongFilter getCollaboratorId() {
        return collaboratorId;
    }

    public void setCollaboratorId(LongFilter collaboratorId) {
        this.collaboratorId = collaboratorId;
    }

    public LongFilter getProjectId() {
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AffectationsCriteria that = (AffectationsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dateFin, that.dateFin) &&
            Objects.equals(charge, that.charge) &&
            Objects.equals(commentaire, that.commentaire) &&
            Objects.equals(color, that.color) &&
            Objects.equals(collaboratorId, that.collaboratorId) &&
            Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        dateDebut,
        dateFin,
        charge,
        commentaire,
        color,
        collaboratorId,
        projectId
        );
    }

    @Override
    public String toString() {
        return "AffectationsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (dateDebut != null ? "dateDebut=" + dateDebut + ", " : "") +
                (dateFin != null ? "dateFin=" + dateFin + ", " : "") +
                (charge != null ? "charge=" + charge + ", " : "") +
                (commentaire != null ? "commentaire=" + commentaire + ", " : "") +
                (color != null ? "color=" + color + ", " : "") +
                (collaboratorId != null ? "collaboratorId=" + collaboratorId + ", " : "") +
                (projectId != null ? "projectId=" + projectId + ", " : "") +
            "}";
    }

}
