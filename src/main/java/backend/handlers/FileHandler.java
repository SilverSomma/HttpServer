package backend.handlers;

import backend.Request;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileHandler extends Handler {
    @Override
    public byte[] getResponseBytes(Request request) throws IOException {

        if (new File(request.getPath()).exists()) {
            File file = new File(request.getPath());
            return new FileInputStream(file).readAllBytes();
        } else {
            return "HTTP/1.1 404 NOT FOUND \r\n\r\n".getBytes(StandardCharsets.UTF_8);
        }
    }
}
