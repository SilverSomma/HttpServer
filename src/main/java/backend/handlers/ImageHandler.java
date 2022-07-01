package backend.handlers;

import backend.Request;

import java.io.*;

public class ImageHandler extends Handler{
    @Override
    public byte[] getResponseBytes(Request request) throws IOException {
        return new FileInputStream("./resources/"+request.getPath()).readAllBytes();
    }
}
