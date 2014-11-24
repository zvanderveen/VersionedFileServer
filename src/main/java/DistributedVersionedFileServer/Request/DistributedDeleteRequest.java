package DistributedVersionedFileServer.Request;

import VersionedFileServer.Request.DeleteRequest;

import java.net.URI;
import java.util.List;

public class DistributedDeleteRequest extends DeleteRequest {
    public DistributedDeleteRequest(String s, List<URI> slaves) {
    }
}
