package elevator.server.security;

import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestFilter;

import javax.annotation.Priority;

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
