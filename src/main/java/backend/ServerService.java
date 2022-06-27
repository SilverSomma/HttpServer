package backend;

import backend.salarycalculatorjava.GrossSalary;
import backend.salarycalculatorjava.SalaryCalculatorService;
import backend.salarycalculatorjava.SalaryInformationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerService {

    protected static Map<String, String> getParams(String request) {
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


    protected static void createPictureListJson() throws IOException {
        File[] resourcesFiles = new File("./resources").listFiles();
        List<String> names = new ArrayList<>();
        for (File resourcesFile : resourcesFiles) {
            names.add(resourcesFile.getName());
        }

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(names);

        OutputStream out = new FileOutputStream("./src/main/java/backend/json/getpicturelist.json");
        out.write(json.getBytes(StandardCharsets.UTF_8));
        out.close();
    }

    protected static void createSalaryResponseJson(Map<String, String> params) throws IOException {
        SalaryInformationResponse response = SalaryCalculatorService.getSalaryInformation(new GrossSalary(new BigDecimal(params.get("salary"))));

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(response);

        OutputStream out = new FileOutputStream("./src/main/java/backend/json/salaryResponse.json");
        out.write(json.getBytes(StandardCharsets.UTF_8));
        out.close();
    }

    protected static byte[] getHtmlBytes(String pathname) throws IOException {
        File file = new File(pathname);
        InputStream in = new FileInputStream(file);
        byte[] fileBytes = in.readAllBytes();
        in.close();
        return fileBytes;
    }

    protected static String getRequestPath(InputStream inputStream) {
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
        if (requestedFile.contains("/")&& !(requestedFile.contains("./"))) {
            return requestedFile.substring(requestedFile.lastIndexOf("/"));
        }
        return requestedFile;
    }

    protected static void getProjectFiles(Map<String, String> params) throws IOException {
        String[] files = new File(params.get("path")).list();
        ObjectMapper obj = new ObjectMapper();
        try {
            String json = obj.writeValueAsString(files);
            OutputStream out = new FileOutputStream("./src/main/java/backend/json/getProjectFiles.json");
            out.write(json.getBytes(StandardCharsets.UTF_8));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
