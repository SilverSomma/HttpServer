import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import salarycalculatorjava.GrossSalary;
import salarycalculatorjava.SalaryCalculatorService;
import salarycalculatorjava.SalaryInformationResponse;

import java.io.*;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpServer {

    public static final byte[] CODE200 = "HTTP/1.1 200 OK \r\n\r\n".getBytes(StandardCharsets.UTF_8);

    public static void main(String[] args) throws IOException {
        runServer(8080);
    }

    private static void runServer(int port) throws IOException {
        final ServerSocket server = new ServerSocket(port);
        Map<String, String> params = null;
        while (true) {
            try (Socket socket = server.accept()) {
                InputStream rawRequestData = socket.getInputStream();
                String requestPath = getRequestPath(rawRequestData);
                if (requestPath.contains("?")) {
                    params = getParams(requestPath);
                }
                String filePath = routeHandler(requestPath, params);
                System.out.println(requestPath);
                byte[] fileBytes = getHtmlBytes(filePath);
                socket.getOutputStream().write(CODE200);
                socket.getOutputStream().write(fileBytes);
            }
        }

    }

    private static Map<String, String> getParams(String request) {
        Map<String, String> params = new HashMap<>();
        int variableStartIndex = request.indexOf("?");
        request = request.substring(variableStartIndex + 1);
        String[] pathVariables = request.split("&");
        for (String variable : pathVariables) {
            String[] keyAndValue = variable.split("=");
            params.put(keyAndValue[0], keyAndValue[1]);
        }
        return params;
    }

    private static String routeHandler(String requestPath, Map<String, String> params) throws IOException {
        if (requestPath.contains(".jpg") || requestPath.contains(".jpeg") || requestPath.contains(".png") || requestPath.contains(".ico")) {
            return "./resources/" + requestPath;
        } else if (requestPath.contains(".json")) {
            if (requestPath.contains("getpicturelist")) {
                createPictureListJson();
                return "./src/main/java/getpicturelist.json";
            }
        } else if (requestPath.contains(".js") || requestPath.contains(".css")) {
            return "./src/main/java/" + requestPath;
        } else if (requestPath.equals("")) {
            return "./src/main/java/index.html";
        } else if (requestPath.contains("?")) {
            if (requestPath.contains("salarycalculator?")) {
                createSalaryResponseJson(params);
                return "./src/main/java/salaryResponse.json";
            }
        } else {
            return "./src/main/java/" + requestPath + ".html";
        }
        return "File not found.";
    }

    private static void createPictureListJson() throws IOException {
        File[] resourcesFiles = new File("./resources").listFiles();
        List<String> names = new ArrayList<>();
        for (File resourcesFile : resourcesFiles) {
            names.add(resourcesFile.getName());
        }

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(names);

        OutputStream out = new FileOutputStream("./src/main/java/getpicturelist.json");
        out.write(json.getBytes(StandardCharsets.UTF_8));
        out.close();
    }

    private static void createSalaryResponseJson(Map<String, String> params) throws IOException {
        SalaryInformationResponse response = SalaryCalculatorService.getSalaryInformation(new GrossSalary(new BigDecimal(params.get("salary"))));

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(response);

        OutputStream out = new FileOutputStream("./src/main/java/salaryResponse.json");
        out.write(json.getBytes(StandardCharsets.UTF_8));
        out.close();
    }


    private static byte[] getHtmlBytes(String pathname) throws IOException {
        File file = new File(pathname);
        InputStream in = new FileInputStream(file);
        byte[] fileBytes = in.readAllBytes();
        in.close();
        return fileBytes;
    }


    private static String getRequestPath(InputStream inputStream) {
        String firstRow = "";
        Reader reader = new InputStreamReader(inputStream);
        try {
            int c;
            while ((c = reader.read()) != 13) {
                firstRow += (char) c;
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        int pathBeginning = firstRow.indexOf("/") + 1;
        int pathEnd = firstRow.indexOf(" ", pathBeginning);
        String requestedFile = firstRow.substring(pathBeginning, pathEnd);
        if (requestedFile.contains("/")) {
            return requestedFile.substring(requestedFile.lastIndexOf("/"));
        }
        return requestedFile;
    }
}
