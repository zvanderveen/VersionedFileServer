package DistributedVersionedFileServer.Request;

import HttpServer.Request.HttpRequest;
import HttpServer.Request.GetRequest;

import java.net.URI;
import java.util.List;

public class DistributedGetRequest extends GetRequest {
    private static final String FILE_VERSION_FORMAT = "%s.version";

    List<URI> slaves;

    public DistributedGetRequest(String fileName, List<URI> slaves) {
        super(fileName);
        this.slaves = slaves;
    }


}
