package org.katastrofi.cs5k;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that a constructor is only for Hibernate use.
 */
@Target({ElementType.CONSTRUCTOR})
public @interface ForHibernateOnly {
}
