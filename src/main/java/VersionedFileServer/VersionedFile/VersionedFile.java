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
            }
            if (fileVersion.exists()) {
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
        if (file.exists()) {
            try (
                    FileInputStream fileInputStream = new FileInputStream(file);
            ) {
                int fileLength = (int) file.length();
                byte[] ret = new byte[fileLength];
                fileInputStream.read(ret);
                return new VersionAndData(getVersion(), ret);
            }
        }

        return new VersionAndData(getVersion(), new byte[0]);
    }

    public int getVersion() throws IOException {
        if (fileVersion.exists()) {
            try (
                FileReader fileReader = new FileReader(fileVersion);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
            ) {
                return Integer.parseInt(bufferedReader.readLine());
            }
        }

        return 0;
    }

    private void setVersion(int newVersion) throws IOException {
        if (!fileVersion.exists()) {
            fileVersion.createNewFile();
        }
        try (
                FileWriter fileWriter = new FileWriter(fileVersion);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        ) {
            bufferedWriter.write(String.valueOf(newVersion));
        }
    }

    private void incrementVersion() throws IOException {
        int currentVersion = getVersion();
        currentVersion++;
        setVersion(currentVersion);
    }
}
