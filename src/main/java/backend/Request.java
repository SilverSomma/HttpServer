package backend;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Value
public class Request {

    String method;
    String path;
    String extension;
    Map<String, String> params;
    Map<String, String> headers;
    String body;

    public Request(InputStream in) throws IOException {
        String data = getData(in);
        String firstRow = getFirstRow(data);
        this.method = requestMethod(firstRow);
        this.path = requestPath(firstRow);
        this.extension = requestExtension(requestPath(firstRow));
        this.params = requestParams(firstRow);
        if (hasBody(data)) {
            this.headers = requestHeaders(getHead(data));
            this.body = getBody(data);
        } else {
            this.headers = requestHeaders(data);
            this.body = "";
        }
        log.info("{} request made on {}",this.method, this.path);
    }

    private boolean hasBody(String data) {
        return !data.endsWith("\r\n\r\n");
    }

    private String getBody(String data) {
        String[] dataStr = data.split("\r\n\r\n");
        return dataStr[1];
    }

    private String getHead(String data) {
        String[] dataStr = data.split("\r\n\r\n");
        return dataStr[0];
    }


    private String getFirstRow(String rawData) {
        String[] rows = rawData.split("\r\n");
        return rows[0];
    }

    private Map<String, String> requestHeaders(String data) {
        Map<String, String> headers = new HashMap<>();
        String[] rows = data.split("\r\n");
        for (int i = 1; i < rows.length; i++) {
            String[] header = rows[i].split(":");
            headers.put(header[0], header[1].substring(1));
        }
        return headers;
    }

    private String getData(InputStream requestData) throws IOException {
        StringBuilder data = new StringBuilder();
        Reader reader = new InputStreamReader(requestData);
        while (true) {
            data.append((char) reader.read());
            if (data.toString().endsWith("\r\n\r\n")) break;
        }
        int contentLength = getRequestBodyLength(data.toString());
        for (int i = 0; i < contentLength; i++) {
            data.append((char) reader.read());
        }
        return data.toString();
    }

    private int getRequestBodyLength(String data) {
        Map<String, String> headers = requestHeaders(data);
        if (headers.containsKey("Content-Length")) {
            return Integer.valueOf(headers.get("Content-Length"));
        }
        return 0;
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

    private String requestMethod(String firstRow) {
        int methodEnd = firstRow.indexOf(" ");
        return firstRow.substring(0, methodEnd);
    }

    private String requestPath(String firstRow) {
        int pathBeginning = firstRow.indexOf("/") + 1;
        int pathEnd = 0;
        if (firstRow.contains("?")) {
            pathEnd = firstRow.indexOf("?", pathBeginning);
        } else {
            pathEnd = firstRow.indexOf(" ", pathBeginning);
        }

        return firstRow.substring(pathBeginning, pathEnd);
    }

    private Map<String, String> requestParams(String requestFirstRow) {
        requestFirstRow = requestFirstRow.substring(0, requestFirstRow.lastIndexOf(" "));
        if (!requestFirstRow.contains("?")) {
            return null;
        }
        Map<String, String> params = new HashMap<>();
        int paramsStartIndex = requestFirstRow.indexOf("?");
        byte[] paramsStringBytes = requestFirstRow.substring(paramsStartIndex + 1).getBytes();
        String paramsString = new String(paramsStringBytes, StandardCharsets.ISO_8859_1);
        String[] pathVariables = paramsString.split("&");
        for (String variable : pathVariables) {
            String[] keyAndValue = variable.split("=");
            params.put(keyAndValue[0], keyAndValue[1]);
        }
        return params;
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
