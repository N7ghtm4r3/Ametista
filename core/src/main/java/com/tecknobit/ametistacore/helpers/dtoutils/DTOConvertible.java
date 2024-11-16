package com.tecknobit.ametistacore.helpers.dtoutils;

/**
 * The {@code DTOConvertible} interface allows to convert a complete object into the related Data Transfer Object
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @since 1.0.5
 */
public interface DTOConvertible<T> {

    /**
     * Method to convert the object to related Transfer Data Object
     *
     * @return the DTO as {@link T}
     */
    T convertToRelatedDTO();

}
