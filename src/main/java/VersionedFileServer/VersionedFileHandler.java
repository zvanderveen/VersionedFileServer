package VersionedFileServer;

import HttpServer.HttpRequestHandler;
import HttpServer.Request.HttpRequest;
import HttpServer.Request.InvalidHttpRequest;
import VersionedFileServer.Request.GetRequest;
import VersionedFileServer.Request.DeleteRequest;
import VersionedFileServer.Request.PutRequest;

import java.io.*;

public class VersionedFileHandler extends HttpRequestHandler {
    protected int version;

    @Override
    protected HttpRequest parseRequest(BufferedReader bufferedReader, String baseDir) {
        try {
            parseCommand(bufferedReader);
            version = getVersion(bufferedReader);
            return processRequest(bufferedReader, baseDir);
        } catch (IOException exception) {
            return new InvalidHttpRequest();
        }
    }

    @Override
    protected HttpRequest processRequest(BufferedReader bufferedReader, String baseDir) throws IOException {
        switch (action) {
            case "GET":
                return new GetRequest(baseDir + fileName);
            case "DELETE":
                return new DeleteRequest(baseDir + fileName, version);
            case "PUT":
                return new PutRequest(baseDir + fileName, parsePutBodyData(bufferedReader).toCharArray(), version);
        }

        return new InvalidHttpRequest();
    }

    protected static int getVersion(BufferedReader bufferedReader) throws IOException {
        int version = 0;

        for (String readString = bufferedReader.readLine(); readString.length() > 0; readString = bufferedReader.readLine()) {
            if (readString.startsWith("Version:")) {
                version = Integer.parseInt(readString.substring(8).trim());
            }
        }

        return version;
    }
}
