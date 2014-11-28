package DistributedVersionedFileServer.Request;

import DistributedVersionedFileServer.VectorFile.VectorClock;
import DistributedVersionedFileServer.VectorFile.VectorClockFile;
import HttpServer.Request.PutRequest;
import HttpServer.Response.HttpResponse;
import HttpServer.Response.InvalidHttpResponse;
import HttpServer.Response.ValidHttpResponse;
import com.google.common.base.Throwables;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PropagatedPutRequest extends PutRequest {
    private static final String FILE_VERSION_FORMAT = "%s.version";

    VectorClock newVectorClock;
    String writer;

    public PropagatedPutRequest(String fileName, String writer, char[] data, VectorClock vectorClock) {
        super(fileName, data);
        this.writer = writer;
        this.newVectorClock = vectorClock;
    }

    @Override
    public HttpResponse execute() {
        String vectorFileName = String.format(FILE_VERSION_FORMAT, fileName);
        VectorClockFile vectorClockFile = new VectorClockFile(vectorFileName);
        VectorClock oldVectorClock = vectorClockFile.read();
        if (!oldVectorClock.conflict(newVectorClock)) {
            vectorClockFile.write(newVectorClock);
            writeFile();
            return new ValidHttpResponse();
        } else {
            Set<String> maxKeys = newVectorClock.getMaxKeys();
            maxKeys.add(writer);
            String maxWriter = Collections.max(maxKeys);

            if (maxWriter.equals(writer)) {
                writeFile();
            }

            oldVectorClock.increment(maxWriter);
            vectorClockFile.write(oldVectorClock);
        }

        return new InvalidHttpResponse();
    }
}
