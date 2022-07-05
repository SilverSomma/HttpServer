package backend.handlers;

import backend.Request;
import backend.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public abstract class Handler {

     public abstract Response getResponseBytes(Request request) throws IOException;

}
