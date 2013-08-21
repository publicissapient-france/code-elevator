package elevator.server.security;

import javax.ws.rs.container.ContainerRequestFilter;

@AdminAuthorization
public class AdminAuthorizationFilter extends AuthorizationFilter implements ContainerRequestFilter {

    public AdminAuthorizationFilter() {
        super(new AdminPasswordValidator());
    }

    private static class AdminPasswordValidator implements UserPasswordValidator {

        private Password password = new RandomPassword();

        @Override
        public Boolean validate(String email, String password) {
            return "admin".equals(email) && this.password.value().equals(password);
        }

    }

}
