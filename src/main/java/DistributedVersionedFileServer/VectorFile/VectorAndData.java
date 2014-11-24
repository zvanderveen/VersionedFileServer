package DistributedVersionedFileServer.VectorFile;

public class VectorAndData {
    VectorClock vectorClock;
    byte[] data;

    public VectorAndData(VectorClock vectorClock, byte[] data) {
        this.vectorClock = vectorClock;
        this.data = data;
    }

    public VectorClock getVectorClock() {
        return this.vectorClock;
    }

    public byte[] getData() {
        return this.data;
    }
}
