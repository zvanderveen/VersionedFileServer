package VersionedFileServer;

import HttpServer.HttpRequestHandler;
import HttpServer.Request.HttpRequest;
import HttpServer.Request.InvalidHttpRequest;
import VersionedFileServer.Request.DeleteRequest;
import VersionedFileServer.Request.GetRequest;
import VersionedFileServer.Request.PutRequest;

import java.io.*;

public class VersionedFileHandler extends HttpRequestHandler {
    private static final String WEB_ROOT = "C:\\Users\\zachvan\\Documents\\";

    public static HttpRequest parseRequest(BufferedReader bufferedReader) {
        try {
            String readString = bufferedReader.readLine();
            if (readString == null) return new InvalidHttpRequest();
            String[] splitLine = readString.split(" ");

            if (splitLine.length < 3) return new InvalidHttpRequest();

            String fileName = splitLine[1];
            fileName = fileName.substring(1);
            if (fileName.equals("")) return new InvalidHttpRequest();

            String action = splitLine[0];

            int version = getVersion(bufferedReader);

            switch (action) {
                case "GET":
                    return new GetRequest(WEB_ROOT + fileName);
                case "DELETE":
                    return new DeleteRequest(WEB_ROOT + fileName, version);
                case "PUT":
                    return new PutRequest(WEB_ROOT + fileName, parsePutBodyData(bufferedReader).toCharArray(), version);
            }
        } catch (IOException exception) {
            return new InvalidHttpRequest();
        }

        return new InvalidHttpRequest();
    }

    protected static int getVersion(BufferedReader bufferedReader) throws IOException {
        for (String readString = bufferedReader.readLine(); readString.length() > 0; readString = bufferedReader.readLine()) {
            if (readString.startsWith("Version:")) {
                return Integer.parseInt(readString.substring(8).trim());
            }
        }

        return 0;
    }
}
