package VersionedFileServer.Request;

import HttpServer.Request.HttpRequest;
import HttpServer.Response.HttpResponse;
import HttpServer.Response.InvalidHttpResponse;
import HttpServer.Response.ValidHttpResponse;
import VersionedFileServer.Response.ConflictResponse;
import VersionedFileServer.VersionedFile.VersionedFile;

import java.io.IOException;

public class DeleteRequest implements HttpRequest {
    String fileName;
    int expectedVersion;

    public DeleteRequest(String fileName, int expectedVersion) {
        this.fileName = fileName;
        this.expectedVersion = expectedVersion;
    }

    @Override
    public HttpResponse execute() {
        VersionedFile versionedFile = new VersionedFile(fileName);

        try {
            int returnedVersion = versionedFile.delete(expectedVersion);
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
