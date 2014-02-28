package elevator.server.security;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestFilter;

@Priority(Priorities.AUTHENTICATION)
@AdminAuthentication
public class AdminAuthenticationFilter extends AuthenticationFilter implements ContainerRequestFilter {
    public AdminAuthenticationFilter() {
        super(new AdminPasswordValidator());
    }

    private static class AdminPasswordValidator implements UserPasswordValidator {
        private Password password = new AdminPassword();

        @Override
        public boolean validate(String user, String password) {
            return this.password.value().equals(password);
        }
    }
}
