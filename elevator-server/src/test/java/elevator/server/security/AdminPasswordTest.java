package elevator.server.security;

import elevator.server.port.SystemPropertyRule;
import org.junit.Rule;
import org.junit.Test;

import static elevator.server.security.AdminPassword.PASSWORD_ROPERTY;
import static org.fest.assertions.Assertions.assertThat;

public class AdminPasswordTest {

    @Rule
    public SystemPropertyRule systemPropertyRule = new SystemPropertyRule("ADMIN_PASSWORD");

    @Test
    public void should_have_default_password() {
        final Password password = new AdminPassword();

        assertThat(password.value()).isEqualTo("admin");
    }

    @Test
    public void should_override_default_password_with_system_property() {
        System.setProperty(PASSWORD_ROPERTY, "an admin password");

        final Password password = new AdminPassword();

        assertThat(password.value()).isEqualTo("an admin password");
    }

}
