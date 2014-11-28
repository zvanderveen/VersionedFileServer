package DistributedVersionedFileServer.Request;

import HttpServer.Request.GetRequest;

import java.net.URI;
import java.util.List;

public class DistributedGetRequest extends GetRequest {
    private static final String FILE_VERSION_FORMAT = "%s.version";

    private String writer;
    List<URI> slaves;

    public DistributedGetRequest(String fileName, String writer, List<URI> slaves) {
        super(fileName);
        this.writer = writer;
        this.slaves = slaves;
    }


}
