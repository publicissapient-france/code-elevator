package elevator.server.port;

import org.junit.Rule;
import org.junit.Test;

import static elevator.server.port.PortFactory.ELEVATOR_SERVER_PORT_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;

public class PortFactoryTest {
    @Rule
    public SystemPropertyRule systemPropertyRule = new SystemPropertyRule(ELEVATOR_SERVER_PORT_PROPERTY);

    @Test
    public void should_create_fixed_port_when_property_is_defined() {
        System.setProperty(ELEVATOR_SERVER_PORT_PROPERTY, "1234");

        Port port = PortFactory.newPort();

        assertThat(port).isInstanceOf(FixedPort.class);
        assertThat(port.port()).isEqualTo(1234);
    }

    @Test
    public void should_create_random_port_when_property_is_not_defined() {
        Port port = PortFactory.newPort();

        assertThat(port).isInstanceOf(RandomPort.class);
    }
}
