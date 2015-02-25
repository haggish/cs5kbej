package org.katastrofi.cs5k;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that the field should be final but can't because of Hibernate's
 * constraints.
 */
@Target(ElementType.FIELD)
public @interface NonFinalForHibernate {
}
