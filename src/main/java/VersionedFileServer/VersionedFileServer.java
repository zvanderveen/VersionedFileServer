package VersionedFileServer;

import HttpServer.HttpServer;
import VersionedFileServer.VersionedFileHandler;

public class VersionedFileServer extends HttpServer {
    public VersionedFileServer(int port, String baseDir, VersionedFileHandler httpRequestHandler) {
        super(port, baseDir, httpRequestHandler);
    }

    public static void main(String[] args) {
        int port = 8080;
        String baseDir = "C:\\Users\\zachvan\\Documents\\";

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            baseDir = args[0];
        }

        VersionedFileServer myServer = new VersionedFileServer(8080, baseDir, new VersionedFileHandler());
    }
}