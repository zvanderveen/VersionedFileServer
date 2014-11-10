package VersionedFileServer.Response;

import HttpServer.Response.HttpResponse;

import java.io.IOException;
import java.io.Writer;

// 409
public class ConflictResponse implements HttpResponse {
    @Override
    public void write(Writer writer) {
        try {
            writer.write("HTTP/1.1 409 Conflict" + System.lineSeparator());
            writer.write("Server: Java HTTP Server 1.0" + System.lineSeparator());
            writer.write("Content-type: text/html" + System.lineSeparator());
            writer.write("Connection: close" + System.lineSeparator());
            writer.write(System.lineSeparator());
            writer.write("Conflict" + System.lineSeparator());
            writer.flush();
        }
        catch (IOException exception) {
            // what can we do?
        }
    }
}
