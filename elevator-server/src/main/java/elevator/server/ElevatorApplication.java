package elevator.server;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class ElevatorApplication extends Application {

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
