package elevator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class ElevatorResource {

    @GET
    public String firstResource() {
        return "Hello World";
    }

}
