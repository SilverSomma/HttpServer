package backend.handlers;

import backend.Request;
import backend.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileHandler extends Handler {
    public static final String CODE404 = "HTTP/1.1 404 NOT FOUND";

    @Override
    public Response getResponseBytes(Request request) throws IOException {

        if (new File(request.getPath()).exists()) {
            File file = new File(request.getPath());
            return new Response(new FileInputStream(file).readAllBytes());
        } else {
            return new Response(CODE404, new byte[0]);
        }
    }
}
