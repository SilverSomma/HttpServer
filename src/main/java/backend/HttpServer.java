package backend;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static backend.ServerController.controller;


public class HttpServer {

    public static final byte[] CODE200 = "HTTP/1.1 200 OK \r\n\r\n".getBytes(StandardCharsets.UTF_8);

    public static void main(String[] args) throws IOException {
        runServer(8080);
    }
    private static void runServer(int port) throws IOException {
        final ServerSocket server = new ServerSocket(port);
        while (true) {
            try (Socket socket = server.accept()) {
                InputStream rawRequestData = socket.getInputStream();
                Request request = new Request(rawRequestData);
                System.out.println(request.getPath());
                Response response = controller(request);
                response.writeResponse(socket, response);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
