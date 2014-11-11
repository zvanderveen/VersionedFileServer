package VersionedFileServer.Response;

import HttpServer.Response.HttpResponse;
import VersionedFileServer.VersionedFile.VersionAndData;

import java.io.IOException;
import java.io.Writer;

// 200
public class VersionAndDataResponse implements HttpResponse {
    VersionAndData versionAndData;

    public VersionAndDataResponse(VersionAndData versionAndData) {
        this.versionAndData = versionAndData;
    }

    @Override
    public void write(Writer writer) {
        try {
            byte[] data = versionAndData.getData();
            int version = versionAndData.getVersion();

            writer.write("HTTP/1.1 200 OK" + System.lineSeparator());
            writer.write("Server: Java HTTP Server 1.0" + System.lineSeparator());
            writer.write("Content-type: text/html" + System.lineSeparator());
            writer.write("Content-length: " + data.length + System.lineSeparator());
            writer.write("Version: " + version + System.lineSeparator());
            writer.write("Connection: close" + System.lineSeparator());
            writer.write(System.lineSeparator());
            writer.write(new String(data));
            writer.flush();
        }
        catch (IOException exception) {

        }
    }
}
