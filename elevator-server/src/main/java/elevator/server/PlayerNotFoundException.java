package elevator.server;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.net.URL;

/**
 * @author <a href="mailto:ygrenzinger@xebia.fr">Yannick Grenzinger</a>
 *         Date: 6/1/13
 */
public class PlayerNotFoundException extends WebApplicationException {

    public PlayerNotFoundException(String email) {
        super(Response.status(Response.Status.NOT_FOUND).entity(email).build());
    }
}
