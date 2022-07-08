package backend;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static backend.ServerController.controller;

@Slf4j
public class HttpServer {


    public static void main(String[] args) throws IOException {
        runServer(8080);
    }
    private static void runServer(int port) throws IOException {
        final ServerSocket server = new ServerSocket(port);
        while (true) {
            try (Socket socket = server.accept()) {
                Request request = new Request(socket.getInputStream());
                Response response = controller(request);
                response.writeResponse(socket, response);
            } catch (Exception e) {
                        log.error(e.getMessage()+" "+Arrays.toString(e.getStackTrace()));
            }
        }
    }
}
