package VersionedFileServer;

import HttpServer.Request.HttpRequest;
import HttpServer.Response.HttpResponse;
import VersionedFileServer.VersionedFileHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class VersionedFileServer {
    public VersionedFileServer(int port) {
        try (
                ServerSocket serverSocket = new ServerSocket(port)
        ) {
            serverSocket.setReuseAddress(true);

            while(true) {
                Socket socket = serverSocket.accept();
                parseAndHandleRequest(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseAndHandleRequest(Socket socket) throws IOException {
        try (
                InputStream inputStream = socket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                OutputStream outputStream = socket.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        ) {
            HttpRequest httpRequest = VersionedFileHandler.parseRequest(bufferedReader);
            HttpResponse httpResponse = httpRequest.execute();
            httpResponse.write(outputStreamWriter);
        } catch (IOException exception) {
            System.out.println("Did not process request");
        }
    }

    public static void main(String[] args) {
        VersionedFileServer myServer = new VersionedFileServer(8080);
    }
}