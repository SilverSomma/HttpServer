package backend;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Authorization {

    public static String generateKey() throws IOException {
        InputStream in = new FileInputStream("./Authorization.txt");
        String text = new String (in.readAllBytes(), "ISO-8859-15");
        String[] rows = text.split("\r\n");
        String user = "";
        String pass = "";
        for (int i = 0; i < rows.length; i++) {
            String[] fields = rows[i].split(":");
            if (i == 0) {
                user = fields[1];
            } else if (i == 1) {
                pass = fields[1];
            } else {
                System.out.println("Creating authorization keys failed!");
            }
        }
        return user+":"+pass;
    }


    public static boolean isValidKey(Request request) throws IOException {
        String myKey = generateKey();
        String clientKey = getRequestKey(request);
        return myKey.equals(clientKey);
    }

    public static String getRequestKey(Request request) throws UnsupportedEncodingException {
        if (request.getHeaders().containsKey("Authorization")) {
            String header = request.getHeaders().get("Authorization");
            String authKeyBase64 = header.substring(header.indexOf(" ")+1);
            return new String(Base64.getDecoder().decode(authKeyBase64),StandardCharsets.UTF_8);
        }
        return "";
    }
}
