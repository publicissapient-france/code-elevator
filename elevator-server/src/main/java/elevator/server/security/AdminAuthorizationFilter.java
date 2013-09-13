package elevator.server.security;

import javax.ws.rs.container.ContainerRequestFilter;

@AdminAuthorization
public class AdminAuthorizationFilter extends AuthorizationFilter implements ContainerRequestFilter {

    public AdminAuthorizationFilter() {
        super(new AdminPasswordValidator());
    }

    private static class AdminPasswordValidator implements UserPasswordValidator {
        @Override
        public Boolean validate(String email, String password) {
            return "admin".equals(email) && "admin".equals(password);
        }
    }

}
