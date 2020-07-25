package org.powerapi.junitjjoules.mesureit;

import org.junit.jupiter.api.extension.ExtendWith;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sanoussy
 *
 */

@Target({TYPE, METHOD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(EnergyMesureExtension.class)
public @interface MesureIt {
}
