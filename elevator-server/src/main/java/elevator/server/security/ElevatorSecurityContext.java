package elevator.server.security;

import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;

class ElevatorSecurityContext implements SecurityContext {
    private final String email;

    ElevatorSecurityContext(String email) {
        this.email = email;
    }

    @Override
    public Principal getUserPrincipal() {
        return new UserPrincipal(email);
    }

    @Override
    public boolean isUserInRole(String role) {
        return false;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return null;
    }
}
