package VersionedFileServer.VersionedFile;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class VersionedFileTest {
    private static final String FILE_NAME = "C:\\Users\\zachvan\\Documents\\test.txt";
    private static final byte[] FILE_CONTENTS = "testwrite".getBytes();
    private static final String VERSION_SUFFIX = ".version";

    @Test
    public void TestManyVersions() throws IOException {
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME + VERSION_SUFFIX);

        for (int i = 0; i < 2000; i++) {
            Write(i, i+1);
            Read(i + 1, FILE_CONTENTS);
        }

        Delete(2000, 0);
    }

    @Test
    public void TestWriteWrongVersion() throws IOException {
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME + VERSION_SUFFIX);
        Write(0,1);
        Write(1,2);
        Write(0,-1);
    }

    @Test
    public void TestWriteVersionFileDoesNotExist() throws IOException {
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME + VERSION_SUFFIX);
        Write(0,1);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME + VERSION_SUFFIX);
        Write(0,1);
    }

    @Test
    public void TestWriteFileDoesNotExist() throws IOException {
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME + VERSION_SUFFIX);
        Write(0,1);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME);
        Write(1,2);
    }

    @Test
    public void TestReadVersionFileDoesNotExist() throws IOException {
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME + VERSION_SUFFIX);
        Write(0,1);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME + VERSION_SUFFIX);
        Read(0, FILE_CONTENTS);
    }

    @Test
    public void TestReadFileDoesNotExist() throws IOException {
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME + VERSION_SUFFIX);
        Write(0,1);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME);
        Read(1, new byte[0]);
    }

    @Test
    public void TestDeleteWrongVersion() throws IOException {
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME + VERSION_SUFFIX);
        Write(0,1);
        Write(1,2);
        Delete(0,-1);
    }

    @Test
    public void TestDeleteVersionFileDoesNotExist() throws IOException {
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME + VERSION_SUFFIX);
        Write(0,1);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME + VERSION_SUFFIX);
        Delete(0,0);
    }

    @Test
    public void TestDeleteFileDoesNotExist() throws IOException {
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME + VERSION_SUFFIX);
        Write(0,1);
        VersionedFileTestHelper.MakeSureFileDoesNotExist(FILE_NAME);
        Delete(1,0);
    }


    private void Delete(int expected, int expectedResult) throws IOException {
        VersionedFile versionedFile = new VersionedFile(FILE_NAME);
        int result = versionedFile.delete(expected);
        Assert.assertEquals(result, expectedResult);
    }

    private void Write(int expected, int expectedResult) throws IOException {
        VersionedFile versionedFile = new VersionedFile(FILE_NAME);
        int result = versionedFile.write(expected, new String(FILE_CONTENTS).toCharArray());
        Assert.assertEquals(result, expectedResult);
    }

    private void Read(int expectedResult, byte[] expectedContents) throws IOException {
        VersionedFile versionedFile = new VersionedFile(FILE_NAME);
        VersionAndData result = versionedFile.read();
        Assert.assertEquals(result.getVersion(),expectedResult);
        Assert.assertArrayEquals(result.getData(), expectedContents);
    }
}
