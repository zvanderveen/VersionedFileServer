package DistributedVersionedFileServer.Request;

import DistributedVersionedFileServer.VectorFile.VectorClock;
import DistributedVersionedFileServer.Request.Propagate.PropagateCalls;
import HttpServer.Response.HttpResponse;
import HttpServer.Response.InvalidHttpResponse;
import HttpServer.Response.ValidHttpResponse;
import HttpServer.Request.PutRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class DistributedPutRequest extends PutRequest {
    private static final String FILE_VERSION_FORMAT = "%s.version";

    List<URI> slaves;
    String writer;

    public DistributedPutRequest(String fileName, char[] data, String writer, List<URI> slaves) {
        super(fileName, data);
        this.writer = writer;
        this.slaves = slaves;
    }

    @Override
    public HttpResponse execute() {
        File file = new File(fileName);
        VectorClock vectorClock = null;

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            return new InvalidHttpResponse();
        }

        String vectorFileName = String.format(FILE_VERSION_FORMAT, fileName);
        vectorClock = new VectorClock(fileName);
        vectorClock.increment(writer);

        int numSuccess = 0;

        // make put requests to other servers.  TODO use futures
        for (URI slaveUri : slaves) {
            if (PropagateCalls.write(slaveUri, fileName, vectorClock, new String(data))) {
                numSuccess++;
            }
        }

        // if any fail, return partial.  up to client to retry.
        if (numSuccess == slaves.size()) {
            return new ValidHttpResponse(null);
        }

        return new InvalidHttpResponse();
    }
}
