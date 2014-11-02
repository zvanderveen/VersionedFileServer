package VersionedFileServer.VersionedFile;

import java.io.*;

public class VersionedFile {
    File file;
    File fileVersion;

    private static final String FILE_VERSION_FORMAT = ".%s.version";

    public VersionedFile(String fileName) {
        file = new File(fileName);
        fileVersion = new File(String.format(FILE_VERSION_FORMAT, fileName));
    }

    public void delete() {
        int currentVersion = getVersion();
        while(currentVersion != getVersion()){
            currentVersion++;
        }

        if (file.exists()) {
            file.delete();
        }

        incrementVersion();
    }

    public void write(char[] data) {
        int currentVersion = getVersion();
        while(currentVersion != getVersion()){
            currentVersion++;
        }

        if (file.exists()) {
            try (
                    FileWriter fileWriter = new FileWriter(fileVersion)
            ) {
                fileWriter.write(data);
            } catch (FileNotFoundException fileNotFoundException) {

            } catch (IOException ioException) {

            }
        }

        incrementVersion();
    }

    public char[] read() {
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
            } catch (FileNotFoundException fileNotFoundException) {

            } catch (IOException ioException) {

            }
        }

        return ret;
    }

    private int getVersion() {
        if (fileVersion.exists()) {
            try (
                FileReader fileReader = new FileReader(fileVersion)
            ) {
                return fileReader.read();
            } catch (FileNotFoundException fileNotFoundException) {

            } catch (IOException ioException) {

            }
        }

        return 0;
    }

    private void setVersion(int newVersion) {
        if (fileVersion.exists()) {
            try (
                    FileWriter fileWriter = new FileWriter(fileVersion)
            ) {
                fileWriter.write(newVersion);
            } catch (FileNotFoundException fileNotFoundException) {

            } catch (IOException ioException) {

            }
        }
    }

    private void incrementVersion() {
        int currentVersion = getVersion();
        currentVersion++;
        setVersion(currentVersion);
    }

    private boolean CompareAndSwap(int expectedVersion, int newVersion) {
        if(getVersion() == expectedVersion) {
            setVersion(newVersion);
        }

        return true;
    }
}
