package elevator.server;

import com.sun.jersey.api.core.DefaultResourceConfig;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.ApplicationPath;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/resources")
public class ElevatorApplication extends DefaultResourceConfig {

    @Override
    public Set<Class<?>> getClasses() {
        HashSet<Class<?>> classes = new HashSet<>();
        classes.add(JacksonJsonProvider.class);
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        HashSet<Object> singletons = new HashSet<>();
        singletons.add(new WebResource(new StartedElevatorServer()));
        return singletons;
    }

}
