package backend.handlers;

import backend.Request;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public abstract class Handler {

     public abstract byte[] getResponseBytes(Request request) throws IOException;

}
