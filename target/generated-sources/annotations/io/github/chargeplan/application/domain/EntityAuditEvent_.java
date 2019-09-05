package io.github.chargeplan.application.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EntityAuditEvent.class)
public abstract class EntityAuditEvent_ {

	public static volatile SingularAttribute<EntityAuditEvent, String> entityValue;
	public static volatile SingularAttribute<EntityAuditEvent, String> entityType;
	public static volatile SingularAttribute<EntityAuditEvent, Instant> modifiedDate;
	public static volatile SingularAttribute<EntityAuditEvent, String> action;
	public static volatile SingularAttribute<EntityAuditEvent, Long> entityId;
	public static volatile SingularAttribute<EntityAuditEvent, String> modifiedBy;
	public static volatile SingularAttribute<EntityAuditEvent, Long> id;
	public static volatile SingularAttribute<EntityAuditEvent, Integer> commitVersion;

}

