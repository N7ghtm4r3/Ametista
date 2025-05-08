import com.tecknobit.apimanager.apis.ServerProtector;
import com.tecknobit.apimanager.exceptions.SaveData;
import com.tecknobit.equinoxbackend.environment.services.builtin.controller.EquinoxController;
import com.tecknobit.equinoxbackend.resourcesutils.ResourcesProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.security.NoSuchAlgorithmException;

import static com.tecknobit.ametista.configuration.resources.AmetistaResourcesManager.APPLICATION_ICONS_FOLDER;
import static com.tecknobit.ametista.services.users.controller.AmetistaUsersController.adminCodeProvider;
import static com.tecknobit.apimanager.apis.ServerProtector.DELETE_SERVER_SECRET_AND_INTERRUPT_COMMAND;
import static com.tecknobit.equinoxbackend.resourcesutils.ResourcesProvider.CUSTOM_CONFIGURATION_FILE_PATH;
import static com.tecknobit.equinoxbackend.resourcesutils.ResourcesProvider.DEFAULT_CONFIGURATION_FILE_PATH;
import static java.lang.String.format;

/**
 * The {@code Launcher} class is useful to launch <b>Ametista's backend service</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@PropertySources({
        @PropertySource(value = "classpath:" + DEFAULT_CONFIGURATION_FILE_PATH),
        @PropertySource(value = "file:" + CUSTOM_CONFIGURATION_FILE_PATH, ignoreResourceNotFound = true)
})
@EnableJpaRepositories("com.tecknobit.*")
@EntityScan("com.tecknobit.*")
@ComponentScan("com.tecknobit.ametista.*")
@SpringBootApplication
public class Launcher {

    /**
     * Main method to start the backend, will be created also the resources directories if not exist invoking the
     * {@link ResourcesProvider} routine
     *
     * @param args: custom arguments to share with {@link SpringApplication} and with the {@link EquinoxController#protector}
     * @apiNote the arguments scheme:
     * <ul>
     *     <li>
     *         {@link EquinoxController#protector} ->
     *         <ul>
     *          <li>
     *             <b>rss</b> -> launch your java application with "rss" to recreate the server secret and admin code<br>
     *                       e.g java -jar Ametista.jar rss
     *             </li>
     *              <li>
     *                  <b>dss</b> -> launch your java application with "dss" to delete the current server secret and
     *                  admin code <br> e.g java -jar Ametista.jar dss
     *              </li>
     *              <li>
     *                  <b>dssi</b> -> avoid to use, because to correctly delete the server secret and the admin code
     *                  this command interrupts the workflow before deleting the server secret
     *              </li>
     *          </ul>
     *     </li>
     *     <li>
     *         {@link SpringApplication} -> see the allowed arguments <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html">here</a>
     *     </li>
     * </ul>
     */
    public static void main(String[] args) {
        if (hasInvalidCommand(args)) {
            throw new IllegalArgumentException("Avoid using the 'dssi' command, as it interrupts the workflow before " +
                    "correctly deleting the server secret and admin code.");
        }
        String adminCode = generateAdminCode(args);
        EquinoxController.initEquinoxEnvironment(
                "tecknobit/ametista/backend",
                getSaveMessage(adminCode),
                Launcher.class,
                args,
                APPLICATION_ICONS_FOLDER);
        SpringApplication.run(Launcher.class, args);
    }

    /**
     * Method to check whether the program arguments list contains invalid commands such {@link ServerProtector#DELETE_SERVER_SECRET_AND_INTERRUPT_COMMAND}
     *
     * @param args The program arguments list
     * @return whether the program arguments list contains invalid commands as {@code boolean}
     */
    private static boolean hasInvalidCommand(String[] args) {
        for (String arg : args)
            if (arg.equals(DELETE_SERVER_SECRET_AND_INTERRUPT_COMMAND))
                return true;
        return false;
    }

    /**
     * Method to generate the admin code for the server instance
     *
     * @param args The program arguments list
     *
     * @return the generated admin code as {@link String}
     */
    private static String generateAdminCode(String[] args) {
        String adminCode = null;
        try {
            adminCodeProvider.launch(args);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (SaveData e) {
            adminCode = e.getSaveableContent();
        }
        return adminCode;
    }

    /**
     * Method to assemble the save message for the {@link SaveData} exception
     *
     * @param adminCode The generated admin code
     *
     * @return the save message assembled as {@link String}
     */
    private static String getSaveMessage(String adminCode) {
        return format(" to correctly register a new user in the Ametista system.\nThe admin code: %sto authenticate as " +
                "an admin, keep it safe!", adminCode);
    }

}
