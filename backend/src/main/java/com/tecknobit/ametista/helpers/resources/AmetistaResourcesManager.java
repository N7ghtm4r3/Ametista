package com.tecknobit.ametista.helpers.resources;

import com.tecknobit.apimanager.annotations.Wrapper;
import com.tecknobit.equinox.resourcesutils.ResourcesManager;
import org.springframework.web.multipart.MultipartFile;

public interface AmetistaResourcesManager extends ResourcesManager {

    String APPLICATION_ICONS_FOLDER = "icons";

    @Wrapper
    default String createAppIcon(MultipartFile icon, String applicationId) {
        return createResource(icon, APPLICATION_ICONS_FOLDER, applicationId);
    }

    @Wrapper
    default boolean deleteAppIcon(String applicationId) {
        return deleteResource(APPLICATION_ICONS_FOLDER, applicationId);
    }

}
