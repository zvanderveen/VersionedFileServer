package DistributedVersionedFileServer.Request;

import HttpServer.Request.HttpRequest;
import VersionedFileServer.Request.GetRequest;

import java.net.URI;
import java.util.List;

public class DistributedGetRequest extends GetRequest {
    public DistributedGetRequest(String s, List<URI> slaves) {
    }
}
