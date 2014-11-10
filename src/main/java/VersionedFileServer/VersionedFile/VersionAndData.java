package VersionedFileServer.VersionedFile;

public class VersionAndData {
    int version;
    byte[] data;

    public VersionAndData(int version, byte[] data) {
        this.version = version;
        this.data = data;
    }

    public int getVersion() {
        return this.version;
    }

    public byte[] getData() {
        return this.data;
    }
}
