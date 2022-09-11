package elevator.server.security;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.HttpHeaders;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Base64;

import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminAuthenticationFilterTest {
    @Mock
    private ContainerRequestContext containerRequestContext;

    @Rule
    public ExpectedException thrown = none();

    @Test
    public void should_not_authorize_if_no_authorization_has_been_provided() throws IOException {
        AdminAuthenticationFilter adminAuthenticationFilter = new AdminAuthenticationFilter();
        thrown.expect(WebApplicationException.class);

        adminAuthenticationFilter.filter(containerRequestContext);
    }

    @Test
    public void should_not_authorize_if_bad_authorization_has_been_provided() throws IOException {
        AdminAuthenticationFilter adminAuthenticationFilter = new AdminAuthenticationFilter();
        when(containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION))
                .thenReturn("Basic " + Base64.getEncoder().encodeToString("admin:badpassword".getBytes()));
        thrown.expect(WebApplicationException.class);

        adminAuthenticationFilter.filter(containerRequestContext);
    }

    @Test
    public void should_authorize_if_good_authorization_has_been_provided() throws IOException {
        AdminAuthenticationFilter adminAuthenticationFilter = new AdminAuthenticationFilter();
        when(containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION))
                .thenReturn("Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes()));
        adminAuthenticationFilter.filter(containerRequestContext);
    }

    @Test
    public void should_authorize_even_without_user() throws IOException {
        AdminAuthenticationFilter adminAuthenticationFilter = new AdminAuthenticationFilter();
        when(containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION))
                .thenReturn("Basic " + Base64.getEncoder().encodeToString(":admin".getBytes()));
        adminAuthenticationFilter.filter(containerRequestContext);
    }
}
