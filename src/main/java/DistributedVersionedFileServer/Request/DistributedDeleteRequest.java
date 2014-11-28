package DistributedVersionedFileServer.Request;

import HttpServer.Request.DeleteRequest;

import java.net.URI;
import java.util.List;

public class DistributedDeleteRequest extends DeleteRequest {
    String writer;
    List<URI> slaves;

    public DistributedDeleteRequest(String fileName, String writer, List<URI> slaves) {
        super(fileName);
        this.writer = writer;
        this.slaves = slaves;
    }


}
