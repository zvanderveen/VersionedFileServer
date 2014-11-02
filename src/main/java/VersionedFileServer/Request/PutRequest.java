package VersionedFileServer.Request;

import HttpServer.Request.HttpRequest;
import HttpServer.Response.HttpResponse;
import HttpServer.Response.InvalidHttpResponse;

public class PutRequest implements HttpRequest {
    @Override
    public HttpResponse execute() {
        return new InvalidHttpResponse();
    }
}
