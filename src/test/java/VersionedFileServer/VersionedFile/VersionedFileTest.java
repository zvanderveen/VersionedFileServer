package VersionedFileServer.VersionedFile;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VersionedFileTest {
    private static final String FILE_NAME = "C:\\Users\\zachvan\\Documents\\test.txt";
    private static final char[] FILE_CONTENTS = "testwrite".toCharArray();
    private static final String VERSION_SUFFIX = ".version";

    @Test
    public void Runner() throws IOException {
        VersionoFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME);
        VersionoFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME + VERSION_SUFFIX);

        for (int i = 0; i < 20; i++) {
            TestWrite(i);
            TestRead(i+1);
        }

        TestDelete(20);
    }

    private void TestDelete(int expected) throws IOException {
        VersionedFile versionedFile = new VersionedFile(FILE_NAME);
        int result = versionedFile.delete(expected);
        Assert.assertEquals(result, 0);
    }

    private void TestWrite(int expected) throws IOException {
        VersionedFile versionedFile = new VersionedFile(FILE_NAME);
        int result = versionedFile.write(expected, FILE_CONTENTS);
        Assert.assertEquals(result, expected + 1);
    }

    private void TestRead(int expected) throws IOException {
        VersionedFile versionedFile = new VersionedFile(FILE_NAME);
        VersionAndData result = versionedFile.read();
        Assert.assertEquals(result.getVersion(),expected);
        Assert.assertArrayEquals(result.getData(), FILE_CONTENTS);
    }
}
