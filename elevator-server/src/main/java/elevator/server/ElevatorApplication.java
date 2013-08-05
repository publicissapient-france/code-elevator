package elevator.server;

import static com.google.common.collect.Sets.newHashSet;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.google.common.collect.Sets;

import elevator.server.security.AdminAuthorizationFilter;
import elevator.server.security.RandomPassword;

public class ElevatorApplication extends Application {

    private final HashSet<Object> singletons;

    public ElevatorApplication() {
        singletons = newHashSet(
                new WebResource(new ElevatorServer()),
                new AdminAuthorizationFilter(new RandomPassword()));
    }

    @Override
    public Set<Class<?>> getClasses() {
        return Sets.<Class<?>> newHashSet(JacksonJsonProvider.class);
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

}
