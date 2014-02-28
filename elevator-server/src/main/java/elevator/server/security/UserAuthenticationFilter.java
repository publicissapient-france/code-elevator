package elevator.server.security;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestFilter;

@Priority(Priorities.AUTHENTICATION)
@UserAuthentication
public class UserAuthenticationFilter extends AuthenticationFilter implements ContainerRequestFilter {
    public UserAuthenticationFilter(UserPasswordValidator userPasswordValidator) {
        super(userPasswordValidator);
    }
}
