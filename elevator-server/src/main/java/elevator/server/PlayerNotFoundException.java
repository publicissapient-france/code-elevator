package elevator.server;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class PlayerNotFoundException extends WebApplicationException {

    public PlayerNotFoundException(String email) {
        super(Response.status(Response.Status.NOT_FOUND).entity(email).build());
    }

}
