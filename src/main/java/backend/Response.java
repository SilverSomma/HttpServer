package backend;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Response {
    String code;
    byte[] content;


    public Response(String code, byte[] content) {
        this.code = code;
        this.content = content;
    }

    public Response(byte[] content) {
        this.content = content;
        this.code = "HTTP/1.1 200 OK \r\n\r\n";
    }

    public void writeResponse(Socket socket, Response response) throws IOException {
        socket.getOutputStream().write(this.code.getBytes(StandardCharsets.UTF_8));
        if (this.content != null) {
            socket.getOutputStream().write(this.content);
        }
    }
}
