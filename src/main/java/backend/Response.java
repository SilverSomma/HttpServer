package backend;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Response {
    String code;
    byte[] content;
    String status;
    String date;
    String contentLength;
    Boolean authorization = false;


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
        this.code = "HTTP/1.1 200 OK";
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
    }

    public String getDate() {
        return date;
    }

    public Boolean getAuthorization() {
        return authorization;
    }

    public void setNeedAuthorization(Boolean needAuthorization) {
        this.authorization = needAuthorization;
    }

    public String getContentLength() {
        return contentLength;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public byte[] getContent() {
        return content;
    }
}
