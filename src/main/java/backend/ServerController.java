package backend;

import backend.handlers.FileHandler;
import backend.handlers.FrontEndFileHandler;
import backend.handlers.ImageHandler;
import backend.handlers.JsonHandler;

import java.io.IOException;
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
        } else {
            return new FileHandler().getResponseBytes(request);
        }
    }
}
