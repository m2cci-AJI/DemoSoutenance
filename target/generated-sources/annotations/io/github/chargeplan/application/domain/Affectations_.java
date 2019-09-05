package io.github.chargeplan.application.domain;

import io.github.chargeplan.application.domain.enumeration.Colors;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Affectations.class)
public abstract class Affectations_ extends io.github.chargeplan.application.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<Affectations, Integer> charge;
	public static volatile SingularAttribute<Affectations, Colors> color;
	public static volatile SingularAttribute<Affectations, LocalDate> dateDebut;
	public static volatile SingularAttribute<Affectations, Projects> project;
	public static volatile SingularAttribute<Affectations, Long> id;
	public static volatile SingularAttribute<Affectations, LocalDate> dateFin;
	public static volatile SingularAttribute<Affectations, String> commentaire;
	public static volatile SingularAttribute<Affectations, Collaborators> collaborator;

}

