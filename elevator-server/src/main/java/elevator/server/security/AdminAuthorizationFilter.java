package elevator.server.security;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

@AdminAuthorization
public class AdminAuthorizationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorization = requestContext.getHeaderString(AUTHORIZATION);
        if (authorization == null) {
            throw new WebApplicationException(UNAUTHORIZED);
        }

        authorization = authorization.replaceFirst("[Bb]asic ", "");
        String userAndPassword = new String(parseBase64Binary(authorization));

        if (!userAndPassword.equals("admin:toHah1ooMeor6Oht")) {
            throw new WebApplicationException(UNAUTHORIZED);
        }
    }

}
