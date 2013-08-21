package elevator.server.security;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

abstract class AuthorizationFilter implements ContainerRequestFilter {

    private final UserPasswordValidator userPasswordValidator;

    protected AuthorizationFilter(UserPasswordValidator userPasswordValidator) {
        this.userPasswordValidator = userPasswordValidator;
    }

    @Override
    public final void filter(ContainerRequestContext requestContext) throws IOException {
        String authorization = requestContext.getHeaderString(AUTHORIZATION);
        if (authorization == null) {
            throw new WebApplicationException(UNAUTHORIZED);
        }

        authorization = authorization.replaceFirst("[Bb]asic ", "");
        String userAndPassword = new String(parseBase64Binary(authorization));
        if (!userAndPassword.contains(":")) {
            throw new WebApplicationException(UNAUTHORIZED);
        }
        String user = userAndPassword.split(":")[0];
        String password = userAndPassword.split(":")[1];

        if (!userPasswordValidator.validate(user, password)) {
            throw new WebApplicationException(UNAUTHORIZED);
        }
    }

}
