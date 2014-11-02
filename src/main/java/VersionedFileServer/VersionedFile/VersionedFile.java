package VersionedFileServer.VersionedFile;

import java.io.*;

public class VersionedFile {
    File file;
    File fileVersion;

    private static final String FILE_VERSION_FORMAT = "%s.version";

    public VersionedFile(String fileName) {
        file = new File(fileName);
        fileVersion = new File(String.format(FILE_VERSION_FORMAT, fileName));
    }

    public int delete(int expectedVersion) throws IOException {
        if(expectedVersion == getVersion()) {
            if (file.exists()) {
                file.delete();
                fileVersion.delete();
            }
        }

        return 0;
    }

    public int write(int expectedVersion, char[] data) throws IOException {
        if(expectedVersion == getVersion()) {
            if (!file.exists()) {
                file.createNewFile();
            }
            try (
                    FileWriter fileWriter = new FileWriter(file)
            ) {
                fileWriter.write(data);
                incrementVersion();
                return getVersion();
            }
        }

        return 0;
    }

    public VersionAndData read() throws IOException {
        char[] ret = new char[(int)file.length()];
        if (file.exists()) {
            try (
                    FileReader fileReader = new FileReader(file)
            ) {
                int i = 0;
                char c = (char) fileReader.read();
                while (c != -1 && i < file.length())
                {
                    ret[i++] = c;
                    c = (char) fileReader.read();
                }
            }
        }

        return new VersionAndData(getVersion(), ret);
    }

    public int getVersion() throws IOException {
        if (fileVersion.exists()) {
            try (
                FileReader fileReader = new FileReader(fileVersion)
            ) {
                return fileReader.read();
            }
        }

        return 0;
    }

    private void setVersion(int newVersion) throws IOException {
        if (!fileVersion.exists()) {
            fileVersion.createNewFile();
        }
        try (
                FileWriter fileWriter = new FileWriter(fileVersion)
        ) {
            fileWriter.write(newVersion);
        }
    }

    private void incrementVersion() throws IOException {
        int currentVersion = getVersion();
        currentVersion++;
        setVersion(currentVersion);
    }
}
