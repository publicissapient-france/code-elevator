package elevator.server.security;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminAuthorizationFilterTest {

    @Mock
    private ContainerRequestContext containerRequestContext;

    @Rule
    public ExpectedException thrown = none();

    private Password passwordForTest = new Password() {
        @Override
        public String value() {
            return "password for test";
        }
    };

    @Test
    public void should_not_authorize_if_no_authorization_has_been_provided() throws IOException {
        AdminAuthorizationFilter adminAuthorizationFilter = new AdminAuthorizationFilter(passwordForTest);
        thrown.expect(WebApplicationException.class);

        adminAuthorizationFilter.filter(containerRequestContext);
    }

    @Test
    public void should_not_authorize_if_bad_authorization_has_been_provided() throws IOException {
        AdminAuthorizationFilter adminAuthorizationFilter = new AdminAuthorizationFilter(passwordForTest);
        when(containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION))
                .thenReturn("Basic " + printBase64Binary("admin:badpassword".getBytes()));
        thrown.expect(WebApplicationException.class);

        adminAuthorizationFilter.filter(containerRequestContext);
    }

    @Test
    public void should_authorize_if_good_authorization_has_been_provided() throws IOException {
        AdminAuthorizationFilter adminAuthorizationFilter = new AdminAuthorizationFilter(passwordForTest);
        when(containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION))
                .thenReturn("Basic " + printBase64Binary("admin:password for test".getBytes()));

        adminAuthorizationFilter.filter(containerRequestContext);
    }

}
