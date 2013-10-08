package elevator.server;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import static org.fest.assertions.Assertions.assertThat;

class DontConnectURLStreamHandler extends URLStreamHandler {

    private final URLConnection urlConnection;
    private final String expectedURL;

    DontConnectURLStreamHandler(String expectedURL, URLConnection urlConnection) {
        this.expectedURL = expectedURL;
        this.urlConnection = urlConnection;
    }

    DontConnectURLStreamHandler(URLConnection urlConnection) {
        this(null, urlConnection);
    }

    @Override
    protected URLConnection openConnection(URL actualURL) throws IOException {
        if (expectedURL != null) {
            assertThat(actualURL.toString()).isEqualTo(expectedURL);
        }
        return urlConnection;
    }

}
