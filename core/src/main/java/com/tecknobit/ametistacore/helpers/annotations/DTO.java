package com.tecknobit.ametistacore.helpers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code DTO} annotation is useful to indicate whether a class is used as Data Transfer Object
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @since 1.0.5
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = ElementType.TYPE)
public @interface DTO {
}
