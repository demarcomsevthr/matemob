package it.mate.commons.server.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target (ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UnownedRelationship {
  
  public String key();
  
  public Class itemClass() default Void.class;
  
}
