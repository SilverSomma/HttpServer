package backend;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static backend.ServerController.controller;
import static backend.ServerService.*;


public class HttpServer {

    public static final byte[] CODE200 = "HTTP/1.1 200 OK \r\n\r\n".getBytes(StandardCharsets.UTF_8);

    public static void main(String[] args) throws IOException {
        runServer(8080);
    }
    private static void runServer(int port) throws IOException {
        final ServerSocket server = new ServerSocket(port);
        Map<String, String> params = null;
        while (true) {
            try (Socket socket = server.accept()) {
                InputStream rawRequestData = socket.getInputStream();
                String requestPath = getRequestPath(rawRequestData);
                if (requestPath.contains("?")) {
                    params = getParams(requestPath);
                }
                String filePath = controller(requestPath, params);
                byte[] fileBytes = getHtmlBytes(filePath);
                socket.getOutputStream().write(CODE200);
                socket.getOutputStream().write(fileBytes);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
