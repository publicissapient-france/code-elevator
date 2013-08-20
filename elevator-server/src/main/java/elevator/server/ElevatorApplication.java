package elevator.server;

import com.google.common.collect.Sets;
import elevator.server.security.AdminAuthorizationFilter;
import elevator.server.security.RandomPassword;
import elevator.server.security.UserAuthorizationFilter;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public class ElevatorApplication extends Application {

    private final HashSet<Object> singletons;

    public ElevatorApplication() {
        ElevatorServer server = new ElevatorServer();
        singletons = newHashSet(
                new WebResource(server),
                new UserAuthorizationFilter(server),
                new AdminAuthorizationFilter(new RandomPassword()));
    }

    @Override
    public Set<Class<?>> getClasses() {
        return Sets.<Class<?>>newHashSet(JacksonJsonProvider.class);
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

}
