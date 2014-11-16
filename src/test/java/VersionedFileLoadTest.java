package VersionedFileServer;

import VersionedFileServer.VersionedFile.VersionedFileTestHelper;
import com.sun.jersey.api.client.ClientResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import javax.ws.rs.core.UriBuilder;

public class VersionedFileLoadTest {
    static final int PORT_A = 8090;
    static final int PORT_B = 8091;
    static final String FILE_A_NAME = "file_a.txt";
    static final String FILE_B_NAME = "file_b.txt";
    static final String FILE_A_TEXT = "file a text";
    static final String FILE_B_TEXT = "file b text";

    @Test
    public void GetLoadTest() throws IOException {
        StartServerOnPort(PORT_A);
        StartServerOnPort(PORT_B);

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);

        WebResource webResourceA = client.resource(UriBuilder.fromUri("http://localhost:" + PORT_A + "/" + FILE_A_NAME).build());
        WebResource webResourceB = client.resource(UriBuilder.fromUri("http://localhost:" + PORT_B + "/" + FILE_B_NAME).build());

        VersionedFileTestHelper.MakeSureFileExists(FILE_A_NAME, FILE_A_TEXT);
        VersionedFileTestHelper.MakeSureFileExists(FILE_B_NAME, FILE_B_TEXT);

        for(int i = 0; i < 1000; i++) {
            ClientResponse response = webResourceA.accept("text/plain").get(ClientResponse.class);
            Assert.assertEquals(response.getStatus(), 200);
            String output = response.getEntity(String.class);
            Assert.assertEquals(output, FILE_A_TEXT);

            response = webResourceB.accept("text/plain").get(ClientResponse.class);
            Assert.assertEquals(response.getStatus(),200);
            output = response.getEntity(String.class);
            Assert.assertEquals(output, FILE_B_TEXT);
        }
    }

    private void StartServerOnPort(final int port) {
        new Thread() {
            public void run() {
                VersionedFileServer versionedFileServer = new VersionedFileServer(port, "C:\\Users\\zachvan\\Documents\\", new VersionedFileHandler());
            }
        }.start();
    }
}
