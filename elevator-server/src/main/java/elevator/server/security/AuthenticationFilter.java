package elevator.server.security;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;

import java.util.Base64;

import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;

abstract class AuthenticationFilter implements ContainerRequestFilter {
    private final UserPasswordValidator userPasswordValidator;

    AuthenticationFilter(UserPasswordValidator userPasswordValidator) {
        this.userPasswordValidator = userPasswordValidator;
    }

    @Override
    public final void filter(ContainerRequestContext requestContext) {
        String authorization = requestContext.getHeaderString(AUTHORIZATION);
        if (authorization == null) {
            throw new WebApplicationException(UNAUTHORIZED);
        }

        authorization = authorization.replaceFirst("[Bb]asic ", "");
        String userAndPassword = new String(Base64.getDecoder().decode(authorization));
        if (!userAndPassword.contains(":")) {
            throw new WebApplicationException(UNAUTHORIZED);
        }
        final String email = userAndPassword.split(":")[0];
        String password = userAndPassword.split(":")[1];

        if (!userPasswordValidator.validate(email, password)) {
            throw new WebApplicationException(UNAUTHORIZED);
        }

        requestContext.setSecurityContext(new ElevatorSecurityContext(email));
    }
}
