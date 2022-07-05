package backend.handlers;

import backend.Request;
import backend.Response;

import java.io.FileInputStream;
import java.io.IOException;

public class ImageHandler extends Handler {
    @Override
    public Response getResponseBytes(Request request) throws IOException {
        return new Response(new FileInputStream("./"+request.getPath()).readAllBytes());
    }
}
