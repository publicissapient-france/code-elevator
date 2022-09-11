package elevator.server;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import elevator.server.security.AdminAuthenticationFilter;
import elevator.server.security.UserAuthenticationFilter;
import jakarta.ws.rs.core.Application;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class ElevatorApplication extends Application {
    private final HashSet<Object> singletons;

    public ElevatorApplication() {
        ElevatorServer server = new ElevatorServer();
        singletons = new HashSet<>(asList(
                new WebResource(server),
                new UserAuthenticationFilter(server),
                new AdminAuthenticationFilter(),
                new ServerStarter(server)));
    }

    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<>(asList(JacksonJsonProvider.class, MultiPartFeature.class));
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
