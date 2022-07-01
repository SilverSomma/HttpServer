package backend.handlers;

import backend.Request;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FrontEndFileHandler extends Handler {

    @Override
    public byte[] getResponseBytes(Request request) throws IOException {
        String ex = request.getExtension();
        if (request.getPath().equals("")) {
            InputStream in = new FileInputStream("./src/main/java/frontend/index.html");
           return in.readAllBytes();
        } else if (ex.equals("")) {
            InputStream in = new FileInputStream("./src/main/java/frontend/" + request.getPath() + ".html");
            return in.readAllBytes();
        } else if (ex.equals(".js") || ex.equals(".css")) {
            InputStream in = new FileInputStream("./src/main/java/frontend/" + request.getPath());
            return in.readAllBytes();
        } else {
            return new byte[0];
        }
    }
}
