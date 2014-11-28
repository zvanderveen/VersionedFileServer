package DistributedVersionedFileServer.Request;

import DistributedVersionedFileServer.VectorFile.VectorClock;
import DistributedVersionedFileServer.Request.Propagate.PropagateCalls;
import DistributedVersionedFileServer.VectorFile.VectorClockFile;
import HttpServer.Response.HttpResponse;
import HttpServer.Response.InvalidHttpResponse;
import HttpServer.Response.ValidHttpResponse;
import HttpServer.Request.PutRequest;
import java.net.URI;
import java.util.List;

public class DistributedPutRequest extends PutRequest {
    private static final String FILE_VERSION_FORMAT = "%s.version";

    VectorClock vectorClock;
    List<URI> slaves;
    String writer;

    public DistributedPutRequest(String fileName, String writer, List<URI> slaves, char[] data) {
        super(fileName, data);
        this.writer = writer;
        this.slaves = slaves;
    }

    @Override
    public HttpResponse execute() {
        writeFile();
        incrementClock();

        int numSuccess = 0;

        // make put requests to other servers.  TODO use futures
        for (URI slaveUri : slaves) {
            if (PropagateCalls.write(slaveUri, fileName, vectorClock, new String(data))) {
                numSuccess++;
            }
        }

        // if any fail, return partial.  up to client to retry.
        if (numSuccess == slaves.size()) {
            return new ValidHttpResponse();
        }

        return new InvalidHttpResponse();
    }

    private void incrementClock() {
        String vectorFileName = String.format(FILE_VERSION_FORMAT, fileName);
        VectorClockFile vectorClockFile = new VectorClockFile(vectorFileName);
        vectorClock = vectorClockFile.read();
        vectorClock.increment(writer);
        vectorClockFile.write(vectorClock);
    }
}
