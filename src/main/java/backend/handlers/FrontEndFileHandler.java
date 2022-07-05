package backend.handlers;

import backend.Request;
import backend.Response;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FrontEndFileHandler extends Handler {

    public static final String CODE404 = "HTTP/1.1 404 NOT FOUND";
    @Override
    public Response getResponseBytes(Request request) throws IOException {
        String ex = request.getExtension();
        if (request.getPath().equals("")) {
            InputStream in = new FileInputStream("./src/main/java/frontend/index.html");
           return new Response(in.readAllBytes());
        } else if (ex.equals("")) {
            InputStream in = new FileInputStream("./src/main/java/frontend/" + request.getPath() + ".html");
            return new Response(in.readAllBytes());
        } else if (ex.equals(".js") || ex.equals(".css")) {
            InputStream in = new FileInputStream("./src/main/java/frontend/" + request.getPath());
            return new Response(in.readAllBytes());
        } else {
            return new Response(CODE404,new byte[0]);
        }
    }
}
