package com.tecknobit.ametista.configuration.resources;

import com.tecknobit.apimanager.annotations.Wrapper;
import com.tecknobit.equinoxbackend.resourcesutils.ResourcesManager;
import org.springframework.web.multipart.MultipartFile;

/**
 * The {@code AmetistaResourcesManager} interface is useful to create and manage the resources files
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ResourcesManager
 */
public interface AmetistaResourcesManager extends ResourcesManager {

    /**
     * {@code APPLICATION_ICONS_FOLDER} the folder where the icons pics will be saved
     */
    String APPLICATION_ICONS_FOLDER = "icons";

    /**
     * Method to create the pathname for an icon pic
     *
     * @param icon:   the resource from create its pathname
     * @param applicationId: the resource identifier
     * @return the pathname created for an icon path
     */
    @Wrapper
    default String createAppIcon(MultipartFile icon, String applicationId) {
        return createResource(icon, APPLICATION_ICONS_FOLDER, applicationId);
    }

    /**
     * Method to delete an icon pic
     *
     * @param applicationId: the user identifier of the icon pic to delete
     * @return whether the icon pic has been deleted as boolean
     */
    @Wrapper
    default boolean deleteAppIcon(String applicationId) {
        return deleteResource(APPLICATION_ICONS_FOLDER, applicationId);
    }

}
