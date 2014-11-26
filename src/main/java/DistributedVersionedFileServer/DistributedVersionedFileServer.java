package DistributedVersionedFileServer;

import HttpServer.HttpServer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class DistributedVersionedFileServer extends HttpServer {
    public DistributedVersionedFileServer(int port, String baseDir, List<URI> slaves) {
        super(port, baseDir, new DistributedVersionedFileHandler(slaves));
    }

    public static void main(String[] args) {
        int port = 8080;
        String baseDir = "C:\\Users\\zachvan\\Documents\\";
        List<URI> slaves = new ArrayList<URI>();

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            baseDir = args[0];
        }
        for (int i = 2; i < args.length; i++) {
            try {
                slaves.add(new URI(args[0]));
            } catch (URISyntaxException ex) {
                System.out.print("Syntax error in URI.  Server not added.");
            }
        }

        DistributedVersionedFileServer myServer = new DistributedVersionedFileServer(port, baseDir, slaves);
    }
}
