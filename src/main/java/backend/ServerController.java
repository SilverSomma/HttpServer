package backend;

import backend.handlers.FrontEndFileHandler;
import backend.handlers.ImageHandler;
import backend.handlers.JsonHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ServerController {


    protected static byte[] controller(Request request) throws IOException {
        Map <String,String> params =  request.getParams();
        if (params != null) {
            return new JsonHandler().getResponseBytes(request);
        } else if(request.isImage()) {
            return new ImageHandler().getResponseBytes(request);
        } else if (request.isJson()) {
            return new JsonHandler().getResponseBytes(request);
        } else if (request.isFrontEndFile()) {
            return new FrontEndFileHandler().getResponseBytes(request);
        }
        return "CODE 404 PAGE NOT FOUND".getBytes(StandardCharsets.UTF_8);
    }
}
