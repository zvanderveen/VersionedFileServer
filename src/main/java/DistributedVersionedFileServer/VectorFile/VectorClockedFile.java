package DistributedVersionedFileServer.VectorFile;

import java.io.*;

public class VectorClockedFile {
    File file;
    File fileVersion;
    VectorClock vectorClock;

    private static final String FILE_VERSION_FORMAT = "%s.version";

    public VectorClockedFile(String fileName) {
        this.file = new File(fileName);
        this.fileVersion = new File(String.format(FILE_VERSION_FORMAT, fileName));
        this.vectorClock = new VectorClock(this.fileVersion);
    }

    public VectorClock delete(VectorClock newVectorClock) throws IOException {
        if(!this.vectorClock.conflict(newVectorClock)) {
            if (file.exists()) {
                file.delete();
            }
            if (fileVersion.exists()) {
                fileVersion.delete();
            }

            return 0;
        }

        return -1;
    }

    public VectorClock write(String writer, char[] data) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(data);
        vectorClock.increment(writer);
        vectorClock.writeToFile(fileVersion);
        return vectorClock;
    }

    public VectorAndData read() throws IOException {
        if (file.exists()) {
            try (
                    FileInputStream fileInputStream = new FileInputStream(file);
            ) {
                int fileLength = (int) file.length();
                byte[] ret = new byte[fileLength];
                fileInputStream.read(ret);
                return new VectorAndData(vectorClock, ret);
            }
        }

        return new VectorAndData(vectorClock, new byte[0]);
    }
}
