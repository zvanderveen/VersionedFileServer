package VersionedFileServer.Request;

import HttpServer.Request.HttpRequest;
import HttpServer.Response.HttpResponse;
import HttpServer.Response.InvalidHttpResponse;
import HttpServer.Response.NotFoundHttpResponse;
import HttpServer.Response.ValidHttpResponse;
import VersionedFileServer.Response.VersionAndDataResponse;
import VersionedFileServer.VersionedFile.VersionAndData;
import VersionedFileServer.VersionedFile.VersionedFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class GetRequest implements HttpRequest {
    String fileName;

    public GetRequest(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public HttpResponse execute() {
        VersionedFile fileToGet = new VersionedFile(fileName);

        try {
            VersionAndData versionAndData = fileToGet.read();

            return new VersionAndDataResponse(versionAndData);
        }
        catch (IOException exception) {
            return new InvalidHttpResponse();
        }
    }
}
