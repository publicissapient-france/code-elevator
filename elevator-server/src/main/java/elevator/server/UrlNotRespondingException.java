package elevator.server;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.net.URL;

public class UrlNotRespondingException extends WebApplicationException {

    private static final long serialVersionUID = 5392577759387409019L;

    public UrlNotRespondingException(URL url) {
        super(Response.status(Response.Status.NOT_IMPLEMENTED).entity(url.getPath()).build());
    }

}
