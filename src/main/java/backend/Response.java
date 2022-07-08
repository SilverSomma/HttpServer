package backend;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Value
public class Response {
    public static final String CODE200 = "HTTP/1.1 200 OK";
    String code;
    byte[] content;
    String status;
    String date;
    String contentLength;
    Boolean authorization;


    public Response(String code, byte[] content) {
        this.code = code;
        this.content = content;
        this.status = getStatus(code);
        this.date = new Date().toString();
        this.contentLength = String.valueOf(content.length);
        this.authorization = false;
    }

    private String getStatus(String code) {
       return code.substring(code.indexOf(" ")+1,code.indexOf(" ")+4);
    }

    public Response(byte[] content) {
        this.code = CODE200;
        this.content = content;
        this.status = getStatus(this.code);
        this.date = new Date().toString();
        this.contentLength = String.valueOf(content.length);
        this.authorization = false;
    }

    public Response(String code, byte[] content, Boolean authorization) {
        this.code = code;
        this.content = content;
        this.status = getStatus(this.code);
        this.date = new Date().toString();
        this.contentLength = String.valueOf(content.length);
        this.authorization = authorization;
    }

    public void writeResponse(Socket socket, Response response) throws IOException {
        socket.getOutputStream().write((response.getCode()+"\r\n").getBytes(StandardCharsets.UTF_8));
        if (response.getAuthorization()) {
            socket.getOutputStream().write(("WWW-Authenticate: Basic realm=User Visible Realm"+"\r\n").getBytes(StandardCharsets.UTF_8));
        }
        socket.getOutputStream().write(("date: "+response.getDate()+"\r\n").getBytes(StandardCharsets.UTF_8));
        socket.getOutputStream().write(("content-length: "+response.getContentLength()+"\r\n").getBytes(StandardCharsets.UTF_8));
        socket.getOutputStream().write(("status: "+response.getStatus()+"\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        if (response.getContent() != null) {
            socket.getOutputStream().write(response.getContent());
        }
        socket.close();
        if (!Objects.equals(response.getCode(), CODE200)) {
            log.warn("Request failed with {}", response.getCode());
        }
    }
}
