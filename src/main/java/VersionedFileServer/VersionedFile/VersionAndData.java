package VersionedFileServer.VersionedFile;

public class VersionAndData {
    int version;
    char[] data;

    public VersionAndData(int version, char[] data) {
        this.version = version;
        this.data = data;
    }

    public int getVersion() {
        return this.version;
    }

    public char[] getData() {
        return this.data;
    }
}
