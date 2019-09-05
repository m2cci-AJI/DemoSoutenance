package io.github.chargeplan.application.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Projects.class)
public abstract class Projects_ extends io.github.chargeplan.application.domain.AbstractAuditingEntity_ {

	public static volatile SetAttribute<Projects, Affectations> affectations;
	public static volatile SingularAttribute<Projects, String> nameProject;
	public static volatile SingularAttribute<Projects, String> projectCode;
	public static volatile SingularAttribute<Projects, String> client;
	public static volatile SingularAttribute<Projects, String> description;
	public static volatile SingularAttribute<Projects, Long> id;
	public static volatile SingularAttribute<Projects, String> dP;

}

