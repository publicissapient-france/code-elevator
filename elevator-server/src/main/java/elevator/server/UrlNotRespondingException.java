package elevator.server;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.net.URL;

/**
 * @author <a href="mailto:ygrenzinger@xebia.fr">Yannick Grenzinger</a>
 *         Date: 6/1/13
 */
public class UrlNotRespondingException extends WebApplicationException {

    public UrlNotRespondingException(URL url) {
        super(Response.status(Response.Status.NOT_IMPLEMENTED).entity(url.getPath()).build());
    }
}
