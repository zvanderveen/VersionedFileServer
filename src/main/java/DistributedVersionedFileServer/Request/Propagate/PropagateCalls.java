package DistributedVersionedFileServer.Request.Propagate;

import DistributedVersionedFileServer.VectorFile.VectorAndData;
import DistributedVersionedFileServer.VectorFile.VectorClock;
import VersionedFileServer.Response.VersionAndDataResponse;
import VersionedFileServer.VersionedFile.VersionAndData;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.net.URI;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

public class PropagateCalls {
    public static boolean delete(URI uri, String fileName, VectorClock vector) {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);

        WebResource webResource = client.resource(UriBuilder.fromUri(uri + "/" + fileName).build());
        ClientResponse response = webResource.header("Vector", vector).accept("text/plain").delete(ClientResponse.class);

        return (response.getStatus() == 200);
    }

    public static boolean write(URI uri, String fileName, VectorClock vector, String fileData) {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);

        WebResource webResource = client.resource(UriBuilder.fromUri(uri + "/" + fileName).build());
        ClientResponse response = webResource.header("Vector", vector).type("text/plain").put(ClientResponse.class, fileData);

        return (response.getStatus() == 200);
    }

    public static VectorAndData read(URI uri, String fileName) {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource webResource = client.resource(UriBuilder.fromUri(uri + "/" + fileName).build());

        ClientResponse response = webResource.accept("text/plain").get(ClientResponse.class);

        MultivaluedMap<String, String> headers = response.getHeaders();
        VectorClock vectorClock = new VectorClock(headers.getFirst("Vector"));

        String output = response.getEntity(String.class);

        return new VectorAndData(vectorClock, output.getBytes());
    }
}
