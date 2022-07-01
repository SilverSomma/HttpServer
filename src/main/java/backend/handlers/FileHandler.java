package backend.handlers;

import backend.Request;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FileHandler extends Handler{
    @Override
    public byte[] getResponseBytes(Request request) throws IOException {
                return new FileInputStream(request.getPath()).readAllBytes();
            }
}
