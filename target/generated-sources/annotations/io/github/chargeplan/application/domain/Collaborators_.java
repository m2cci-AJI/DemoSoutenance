package io.github.chargeplan.application.domain;

import io.github.chargeplan.application.domain.enumeration.Skill;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Collaborators.class)
public abstract class Collaborators_ extends io.github.chargeplan.application.domain.AbstractAuditingEntity_ {

	public static volatile SetAttribute<Collaborators, Affectations> affectations;
	public static volatile SingularAttribute<Collaborators, String> nomCollaborator;
	public static volatile SingularAttribute<Collaborators, String> trigramme;
	public static volatile SingularAttribute<Collaborators, String> prenomCollaborator;
	public static volatile SingularAttribute<Collaborators, Skill> competencies;
	public static volatile SingularAttribute<Collaborators, Long> id;
	public static volatile SingularAttribute<Collaborators, String> email;

}

