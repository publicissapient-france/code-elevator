package elevator.server.security;

import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestFilter;

import javax.annotation.Priority;

@Priority(Priorities.AUTHENTICATION)
@UserAuthentication
public class UserAuthenticationFilter extends AuthenticationFilter implements ContainerRequestFilter {
    public UserAuthenticationFilter(UserPasswordValidator userPasswordValidator) {
        super(userPasswordValidator);
    }
}
