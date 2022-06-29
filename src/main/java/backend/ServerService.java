package backend;

import backend.salarycalculatorjava.GrossSalary;
import backend.salarycalculatorjava.SalaryCalculatorService;
import backend.salarycalculatorjava.SalaryInformationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ServerService {

    protected static Map<String, String> getParams(String request) {
        if(!request.contains("?")){
            return null;
        }
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
        String[] names = new File("./resources").list();
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

    protected static String getRequestPath(InputStream inputStream) throws IOException {
        StringBuilder firstRow = new StringBuilder();
        Reader reader = new InputStreamReader(inputStream);
        int c;
        while ((c = reader.read()) != 13) {
            firstRow.append((char) c);
        }
        return getRequestPathFromHeaderFirstRow(firstRow);
    }

    protected static void writeProjectFiles(Map<String, String> params) throws IOException {
            String path = params.get("path");
            OutputStream out = new FileOutputStream("./src/main/java/backend/json/getProjectFiles.json");
            if (isFile(path)) {
                if (isImage(path)) {
                    encodeAndWriteImageBytes(path, out);
                } else {
                    writeFileBytes(path, out);
                }
            } else {
                writeNewDirectoryList(path, out);
            }
    }

    private static void writeNewDirectoryList(String path, OutputStream out) throws IOException {
        FilenameFilter filenameFilter = getFileNameFilter();
        File [] fileArr = new File(path).listFiles(filenameFilter);
        String[] files = sortFilesByDirectory(fileArr);
        String json = new ObjectMapper().writeValueAsString(files);
        out.write(json.getBytes(StandardCharsets.UTF_8));
        out.close();
    }

    private static String[] sortFilesByDirectory(File[] files) {
        Arrays.sort(files,Comparator.comparing(File::isDirectory).reversed().thenComparing(Comparator.naturalOrder()));
        String [] fileStrings = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            fileStrings[i] = files[i].getName();
        }
        return fileStrings;
    }

    private static void writeFileBytes(String path, OutputStream out) throws IOException {
        File file = new File(path);
        InputStream in = new FileInputStream(file);
        out.write(in.readAllBytes());
        out.close();
    }

    private static boolean isFile(String path) {
        if (path.length() < 5) {
            return false;
        }
        String extension = path.substring(path.length() - 5);
        return extension.contains(".");
    }

    private static String getRequestPathFromHeaderFirstRow(StringBuilder firstRow) {
        int pathBeginning = firstRow.indexOf("/") + 1;
        int pathEnd = firstRow.indexOf(" ", pathBeginning);
        String requestPath = firstRow.substring(pathBeginning, pathEnd);
        if (requestPath.contains("/") && !(requestPath.contains("./"))) {
            return requestPath.substring(requestPath.lastIndexOf("/"));
        }
        return requestPath;
    }

    private static boolean isImage(String path) {
        return path.contains(".jpg") || path.contains(".ico") || path.contains(".png") || path.contains(".jpeg");
    }


    private static void encodeAndWriteImageBytes(String path, OutputStream out) throws IOException {
        File file = new File(path);
        byte[] bytes = new FileInputStream(file).readAllBytes();
        byte[] encoded = Base64.getEncoder().encode(bytes);
        out.write(encoded);
        out.close();
    }

    private static FilenameFilter getFileNameFilter() {
        return (dir, name) -> {
            if (Objects.equals(dir, new File("./"))) {
                return !Objects.equals(name, ".git") && !Objects.equals(name, ".idea") && !Objects.equals(name, "target");
            } else return true;
        };
    }


}
