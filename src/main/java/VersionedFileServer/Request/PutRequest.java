package VersionedFileServer.Request;

import HttpServer.Request.HttpRequest;
import HttpServer.Response.HttpResponse;
import HttpServer.Response.InvalidHttpResponse;
import HttpServer.Response.ValidHttpResponse;
import VersionedFileServer.Response.ConflictResponse;
import VersionedFileServer.VersionedFile.VersionedFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PutRequest implements HttpRequest {
    String fileName;
    char[] data;
    int expectedVersion;

    public PutRequest(String fileName, char[] data, int expectedVersion) {
        this.fileName = fileName;
        this.data = data;
        this.expectedVersion = expectedVersion;
    }

    @Override
    public HttpResponse execute() {
        VersionedFile versionedFile = new VersionedFile(fileName);

        try {
            int returnedVersion = versionedFile.write(expectedVersion, data);
            if (returnedVersion == expectedVersion + 1) {
                return new ValidHttpResponse(null);
            } else {
                return new ConflictResponse();
            }

        }
        catch (IOException exception) {
            return new InvalidHttpResponse();
        }
    }
}
