package VersionedFileServer.VersionedFile;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class VersionedFileTest {
    private static final String FILE_NAME = "C:\\Users\\zachvan\\Documents\\test.txt";
    private static final char[] FILE_CONTENTS = "testwrite".toCharArray();

    @Test
    public void TestWrite() throws IOException {
        VersionedFile versionedFile = new VersionedFile(FILE_NAME);
        int result = versionedFile.write(0, FILE_CONTENTS);
        Assert.assertEquals(result, 1);
    }

    @Test
    public void TestRead() throws IOException {
        VersionedFile versionedFile = new VersionedFile(FILE_NAME);
        VersionAndData result = versionedFile.read();
        Assert.assertEquals(result.getVersion(),1);
        Assert.assertArrayEquals(result.getData(), FILE_CONTENTS);
    }

    @Test
    public void TestDelete() throws IOException {
        VersionedFile versionedFile = new VersionedFile(FILE_NAME);
        int result = versionedFile.delete(1);
        Assert.assertEquals(result, 0);
    }
}
