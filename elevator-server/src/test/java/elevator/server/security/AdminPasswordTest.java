package elevator.server.security;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class AdminPasswordTest {

    @Test
    public void should_have_default_password() {
        final Password password = new AdminPassword();

        assertThat(password.value()).isEqualTo("admin");
    }

}
