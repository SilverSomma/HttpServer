package backend;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private final String method;
    private final String path;
    private final String extension;
    private final Map<String,String> params;

    public Request(InputStream requestData) throws IOException {
        String requestFirstRow = requestFirstRow(requestData);
        this.method = requestMethod(requestFirstRow);
        this.path = requestPath(requestFirstRow);
        this.extension = requestExtension(requestPath(requestFirstRow));
        this.params = requestParams(requestFirstRow);
    }


    public boolean isImage() {
        String ex = this.extension;
        return ex.contains(".jpg") || ex.contains(".ico") || ex.contains(".jpeg") || ex.contains(".png");
    }
    public boolean isJson() {
        String ex = this.extension;
        return ex.contains(".json");
    }
    public boolean isFrontEndFile() {
        String ex = this.extension;
        return ex.contains(".js") || ex.contains(".css") || ex.equals("");
    }

    public String getExtension() {
        return extension;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParams() {
        return params;
    }

    private String requestMethod(String firstRow) {
        int methodEnd = firstRow.indexOf(" ");
        return firstRow.substring(0,methodEnd);
    }

    private String requestPath(String firstRow) {
        int pathBeginning = firstRow.indexOf("/") + 1;
        int pathEnd = 0;
        if (firstRow.contains("?")) {
            pathEnd = firstRow.indexOf("?", pathBeginning);
        } else {
            pathEnd = firstRow.indexOf(" ", pathBeginning);
        }
        String requestPath = firstRow.substring(pathBeginning, pathEnd);
        if (requestPath.contains("/") && !(requestPath.contains("./"))) {
            return requestPath.substring(requestPath.lastIndexOf("/"));
        }
        return requestPath;
    }

    private Map<String, String> requestParams(String requestFirstRow) {
        requestFirstRow = requestFirstRow.substring(0,requestFirstRow.lastIndexOf(" "));
        if(!requestFirstRow.contains("?")){
            return null;
        }
        Map<String, String> params = new HashMap<>();
        int paramsStartIndex = requestFirstRow.indexOf("?");
        String paramsString = requestFirstRow.substring(paramsStartIndex + 1);
        String[] pathVariables = paramsString.split("&");
        for (String variable : pathVariables) {
            String[] keyAndValue = variable.split("=");
            params.put(keyAndValue[0], keyAndValue[1]);
        }
        return params;
    }

    private String requestFirstRow(InputStream requestData) throws IOException {
        StringBuilder firstRow = new StringBuilder();
        Reader reader = new InputStreamReader(requestData);
        int c;
        while ((c = reader.read()) != 13) {
            firstRow.append((char) c);
        }
        return firstRow.toString();
    }

    private String requestExtension(String requestPath) {
        if (!requestPath.contains(".")) {
            return "";
        } else {
            int dotPos = requestPath.indexOf(".");
            return requestPath.substring(dotPos);
        }
    }
}
