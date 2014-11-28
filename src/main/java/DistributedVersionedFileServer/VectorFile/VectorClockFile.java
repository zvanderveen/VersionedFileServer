package DistributedVersionedFileServer.VectorFile;

import com.google.common.base.Throwables;
import java.io.*;

public class VectorClockFile {
    File vectorClockFile;

    public VectorClockFile(String fileName) {
        this.vectorClockFile = new File(fileName);
    }

    public void write(VectorClock vectorClock) {
        try {
            if (!vectorClockFile.exists()) {
                vectorClockFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Throwables.propagate(e);
        }

        try (
                FileWriter fileWriter = new FileWriter(vectorClockFile);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        ) {
            bufferedWriter.write(vectorClock.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Throwables.propagate(e);
        }
    }

    public VectorClock read() {
        if (!vectorClockFile.exists()) {
            return new VectorClock();
        }

        String fileContents = null;

        try (
                FileReader fileReader = new FileReader(vectorClockFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
        ) {
            fileContents = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            Throwables.propagate(e);
        }

        return new VectorClock(fileContents);
    }
}
