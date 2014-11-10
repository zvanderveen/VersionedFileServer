package VersionedFileServer.VersionedFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VersionoFileTestHelper {
    public static void MakeSureFileExists(String fileName, String fileContent) throws IOException {
        File file = new File(fileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
    }

    public static void MakeSureFileDoesNotExist(String fileName) throws IOException {
        File file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }
    }
}
