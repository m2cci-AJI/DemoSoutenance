package io.github.chargeplan.application.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.chargeplan.application.domain.enumeration.Skill;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Collaborators entity. This class is used in CollaboratorsResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /collaborators?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CollaboratorsCriteria implements Serializable {
    /**
     * Class for filtering Skill
     */
    public static class SkillFilter extends Filter<Skill> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomCollaborator;

    private StringFilter prenomCollaborator;

    private StringFilter trigramme;

    private StringFilter email;

    private SkillFilter competencies;

    private LongFilter affectationId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNomCollaborator() {
        return nomCollaborator;
    }

    public void setNomCollaborator(StringFilter nomCollaborator) {
        this.nomCollaborator = nomCollaborator;
    }

    public StringFilter getPrenomCollaborator() {
        return prenomCollaborator;
    }

    public void setPrenomCollaborator(StringFilter prenomCollaborator) {
        this.prenomCollaborator = prenomCollaborator;
    }

    public StringFilter getTrigramme() {
        return trigramme;
    }

    public void setTrigramme(StringFilter trigramme) {
        this.trigramme = trigramme;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public SkillFilter getCompetencies() {
        return competencies;
    }

    public void setCompetencies(SkillFilter competencies) {
        this.competencies = competencies;
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
        final CollaboratorsCriteria that = (CollaboratorsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nomCollaborator, that.nomCollaborator) &&
            Objects.equals(prenomCollaborator, that.prenomCollaborator) &&
            Objects.equals(trigramme, that.trigramme) &&
            Objects.equals(email, that.email) &&
            Objects.equals(competencies, that.competencies) &&
            Objects.equals(affectationId, that.affectationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nomCollaborator,
        prenomCollaborator,
        trigramme,
        email,
        competencies,
        affectationId
        );
    }

    @Override
    public String toString() {
        return "CollaboratorsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nomCollaborator != null ? "nomCollaborator=" + nomCollaborator + ", " : "") +
                (prenomCollaborator != null ? "prenomCollaborator=" + prenomCollaborator + ", " : "") +
                (trigramme != null ? "trigramme=" + trigramme + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (competencies != null ? "competencies=" + competencies + ", " : "") +
                (affectationId != null ? "affectationId=" + affectationId + ", " : "") +
            "}";
    }

}
