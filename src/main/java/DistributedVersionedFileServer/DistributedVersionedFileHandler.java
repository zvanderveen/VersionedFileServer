package DistributedVersionedFileServer;

import DistributedVersionedFileServer.Request.DistributedGetRequest;
import DistributedVersionedFileServer.Request.PropagatedPutRequest;
import DistributedVersionedFileServer.VectorFile.VectorClock;
import HttpServer.Request.HttpRequest;
import HttpServer.Request.InvalidHttpRequest;
import DistributedVersionedFileServer.Request.DistributedDeleteRequest;
import DistributedVersionedFileServer.Request.DistributedPutRequest;
import HttpServer.HttpRequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class DistributedVersionedFileHandler extends HttpRequestHandler {
    String writer;
    List<URI> slaves;
    VectorClock vectorClock = null;

    public DistributedVersionedFileHandler(String writer, List<URI> slaves) {
        this.writer = writer;
        this.slaves = slaves;
    }

    @Override
    protected HttpRequest parseRequest(BufferedReader bufferedReader, String baseDir) {
        try {
            parseCommand(bufferedReader);
            getVectorClock(bufferedReader);
            return processRequest(bufferedReader, baseDir);
        } catch (IOException exception) {
            return new InvalidHttpRequest();
        }
    }


    @Override
    protected HttpRequest processRequest(BufferedReader bufferedReader, String baseDir) throws IOException {
        if (vectorClock == null) {
            switch (action) {
                case "GET":
                    return new DistributedGetRequest(baseDir + fileName, writer, slaves);
                case "DELETE":
                    return new DistributedDeleteRequest(baseDir + fileName, writer, slaves);
                case "PUT":
                    return new DistributedPutRequest(baseDir + fileName, writer, slaves, parsePutBodyData(bufferedReader).toCharArray());
            }
        } else {
            switch (action) {
                case "PUT":
                    return new PropagatedPutRequest(baseDir + fileName, writer, parsePutBodyData(bufferedReader).toCharArray(), vectorClock);
            }
        }

        return new InvalidHttpRequest();
    }

    protected void getVectorClock(BufferedReader bufferedReader) throws IOException {
        for (String readString = bufferedReader.readLine(); readString.length() > 0; readString = bufferedReader.readLine()) {
            if (readString.startsWith("VectorClock:")) {
                vectorClock = new VectorClock(readString.substring(12).trim());
            }
        }
    }

}
