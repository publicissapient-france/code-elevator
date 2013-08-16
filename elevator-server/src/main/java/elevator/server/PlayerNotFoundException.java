package elevator.server;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

public class PlayerNotFoundException extends WebApplicationException {

    private static final long serialVersionUID = 328828166815504450L;

    public PlayerNotFoundException(String email) {
        super(Response.status(NOT_FOUND).entity(email).build());
    }

}
