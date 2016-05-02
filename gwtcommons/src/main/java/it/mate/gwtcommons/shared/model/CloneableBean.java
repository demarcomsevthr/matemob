package it.mate.gwtcommons.shared.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target (ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CloneableBean {

  public String overrideTargetClassName();
  
}
