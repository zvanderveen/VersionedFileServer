package DistributedVersionedFileServer.Request;

import DistributedVersionedFileServer.VectorFile.VectorClock;
import DistributedVersionedFileServer.VectorFile.VectorClockedFile;
import HttpServer.Response.HttpResponse;
import HttpServer.Response.InvalidHttpResponse;
import HttpServer.Response.ValidHttpResponse;
import HttpServer.Request.PutRequest;
import VersionedFileServer.Response.ConflictResponse;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class DistributedPutRequest extends PutRequest {
    List<URI> slaves;
    String writer;

    public DistributedPutRequest(String fileName, char[] data, String writer, List<URI> slaves) {
        this.fileName = fileName;
        this.data = data;
        this.writer = writer;
        this.slaves = slaves;
    }

    @Override
    public HttpResponse execute() {
        // execute locally
        VectorClockedFile vectorClockedFile = new VectorClockedFile(fileName);

        try {
            VectorClock vectorClock = vectorClockedFile.write(writer, data);
        } catch (IOException exception) {
            return new InvalidHttpResponse();
        }

        // make put requests to other servers.  use futures
        for (URI slaveURI : slaves) {
            // get a response
            // TODO extract out request code from tests so they can be used here
            // propagate using server id
        }

        // if any fail, return partial.  up to client to retry.


        return httpResponse;
    }
}
