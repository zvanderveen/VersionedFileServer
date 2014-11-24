package VersionedFileServer;

import VersionedFileServer.VersionedFile.VersionedFileTestHelper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

public class VersionedFileServerTest {
    private static final String FILE_PATH = "C:\\Users\\zachvan\\Documents\\";
    private static final String FILE_NAME = "test.txt";
    private static final String FILE_CONTENTS = "testwrite\n\n";
    private static final String VERSION_SUFFIX = ".version";
    private static int PORT = 8080;

    @Before
    public void setup() {
        new Thread() {
            public void run() {
                VersionedFileServer versionedFileServer = new VersionedFileServer(PORT, "C:\\Users\\zachvan\\Documents\\", new VersionedFileHandler());
            }
        }.start();
    }

    @Test
    public void TestManyVersions() throws IOException {
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME + VERSION_SUFFIX);

        for (int i = 0; i < 2000; i++) {
            Write(200, i, i+1);
            Read(200, i + 1, FILE_CONTENTS);
        }

        Delete(200, 2000, 0);
    }

    @Test
    public void TestWriteWrongVersion() throws IOException {
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME + VERSION_SUFFIX);
        Write(200, 0, 1);
        Write(200, 1, 2);
        Write(409, 0, -1);
    }

    @Test
    public void TestWriteVersionFileDoesNotExist() throws IOException {
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME + VERSION_SUFFIX);
        Write(200, 0, 1);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME + VERSION_SUFFIX);
        Write(200, 0, 1);
    }

    @Test
    public void TestWriteFileDoesNotExistFailure() throws IOException {
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME + VERSION_SUFFIX);
        Write(200, 0, 1);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME);
        Write(409, 0, 1);
    }

    @Test
    public void TestWriteFileDoesNotExistSuccess() throws IOException {
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME + VERSION_SUFFIX);
        Write(200, 0, 1);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME);
        Write(200, 1, 2);
    }

    @Test
    public void TestReadVersionFileDoesNotExist() throws IOException {
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME + VERSION_SUFFIX);
        Write(200, 0, 1);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME + VERSION_SUFFIX);
        Read(200, 0, FILE_CONTENTS);
    }

    @Test
    public void TestReadFileDoesNotExist() throws IOException {
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME + VERSION_SUFFIX);
        Write(200, 0,1);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME);
        Read(200, 1, "");
    }

    @Test
    public void TestDeleteWrongVersion() throws IOException {
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME + VERSION_SUFFIX);
        Write(200, 0, 1);
        Write(200, 1, 2);
        Delete(409, 0, -1);
    }

    @Test
    public void TestDeleteVersionFileDoesNotExist() throws IOException {
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME + VERSION_SUFFIX);
        Write(200, 0, 1);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME + VERSION_SUFFIX);
        Delete(200, 0, 0);
    }

    @Test
    public void TestDeleteFileDoesNotExist() throws IOException {
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME + VERSION_SUFFIX);
        Write(200, 0, 1);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_PATH + FILE_NAME);
        Delete(200, 1, 0);
    }

    private void Delete(int expectedHttpResponse,int expected, int expectedResult) throws IOException {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);

        WebResource webResource = client.resource(UriBuilder.fromUri("http://localhost:" + PORT + "/" + FILE_NAME).build());
        ClientResponse response = webResource.header("Version", expected).accept("text/plain").delete(ClientResponse.class);
        Assert.assertEquals(response.getStatus(), expectedHttpResponse);
    }

    private void Write(int expectedHttpResponse,int expected, int expectedResult) throws IOException {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);

        WebResource webResource = client.resource(UriBuilder.fromUri("http://localhost:" + PORT + "/" + FILE_NAME).build());
        ClientResponse response = webResource.header("Version", expected).type("text/plain").put(ClientResponse.class, FILE_CONTENTS);
        Assert.assertEquals(expectedHttpResponse, response.getStatus());
    }

    private void Read(int expectedHttpResponse, int expectedResult, String expectedContents) throws IOException {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource webResource = client.resource(UriBuilder.fromUri("http://localhost:" + PORT + "/" + FILE_NAME).build());

        ClientResponse response = webResource.accept("text/plain").get(ClientResponse.class);
        Assert.assertEquals(response.getStatus(), expectedHttpResponse);

        MultivaluedMap<String, String> headers = response.getHeaders();
        String version = headers.getFirst("Version");
        Assert.assertTrue(version.equals(String.valueOf(expectedResult)));

        String output = response.getEntity(String.class);
        Assert.assertEquals(output, expectedContents.trim());
    }
}
