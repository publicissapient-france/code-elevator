package elevator.server.security;

import javax.ws.rs.container.ContainerRequestFilter;

@UserAuthorization
public class UserAuthorizationFilter extends AuthorizationFilter implements ContainerRequestFilter {

    public UserAuthorizationFilter(UserPasswordValidator userPasswordValidator) {
        super(userPasswordValidator);
    }

}
