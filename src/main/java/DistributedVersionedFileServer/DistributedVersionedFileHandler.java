package DistributedVersionedFileServer;

import DistributedVersionedFileServer.Request.DistributedGetRequest;
import HttpServer.Request.HttpRequest;
import HttpServer.Request.InvalidHttpRequest;
import DistributedVersionedFileServer.Request.DistributedDeleteRequest;
import DistributedVersionedFileServer.Request.DistributedGetRequest;
import DistributedVersionedFileServer.Request.DistributedPutRequest;
import VersionedFileServer.VersionedFileHandler;
import HttpServer.HttpRequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class DistributedVersionedFileHandler extends HttpRequestHandler {
    List<URI> slaves;

    public DistributedVersionedFileHandler(List<URI> slaves) {
        this.slaves = slaves;
    }

    @Override
    protected HttpRequest processRequest(BufferedReader bufferedReader, String baseDir) throws IOException {
        switch (action) {
            case "GET":
                return new DistributedGetRequest(baseDir + fileName, slaves);
            case "DELETE":
                return new DistributedDeleteRequest(baseDir + fileName, slaves);
            case "PUT":
                return new DistributedPutRequest(baseDir + fileName, parsePutBodyData(bufferedReader).toCharArray(), null, slaves);
        }

        return new InvalidHttpRequest();
    }
}
