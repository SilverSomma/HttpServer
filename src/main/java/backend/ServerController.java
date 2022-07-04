package backend;

import backend.handlers.FileHandler;
import backend.handlers.FrontEndFileHandler;
import backend.handlers.ImageHandler;
import backend.handlers.JsonHandler;

import java.io.IOException;
import java.util.Map;

public class ServerController {


    protected static Response controller(Request request) throws IOException {
        Map <String,String> params =  request.getParams();
        if (params != null) {
            return new Response(new JsonHandler().getResponseBytes(request));
        } else if(request.isImage()) {
            return new Response(new ImageHandler().getResponseBytes(request));
        } else if (request.isJson()) {
            return new Response(new JsonHandler().getResponseBytes(request)) ;
        } else if (request.isFrontEndFile()) {
            return new Response(new FrontEndFileHandler().getResponseBytes(request)) ;
        } else {
            return new Response(new FileHandler().getResponseBytes(request));
        }
    }
}
