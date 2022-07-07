package backend.handlers;

import backend.Authorization;
import backend.Request;
import backend.Response;
import backend.salarycalculatorjava.GrossSalary;
import backend.salarycalculatorjava.SalaryCalculatorService;
import backend.salarycalculatorjava.SalaryInformationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.Objects;

public class JsonHandler extends Handler {


    public static final String CODE404 = "HTTP/1.1 404 NOT FOUND";
    public static final String CODE401 = "HTTP/1.1 401 Unauthorized";

    @Override
    public Response getResponseBytes(Request request) throws IOException {
        if (Objects.equals(request.getPath(), "salarycalculator")) {
            return handleSalaryCalculator(request);
        } else if (Objects.equals(request.getPath(), "getprojectfiles")) {
            return getProjectFiles(request);
        } else if (Objects.equals(request.getPath(), "newprojectfile")) {
            return createNewFile(request);
        } else if (Objects.equals(request.getPath(), "getpicturelist.json")) {
            return createPictureListJson(request);
        } else if (Objects.equals(request.getPath(), "saveprojectfile")) {
            return saveFileChanges(request);
        }
        return new Response(CODE404, new byte[0]);
    }

    private Response saveFileChanges(Request request) throws IOException {
        OutputStream out = new FileOutputStream(request.getParams().get("path"));
        out.write(request.getBody().getBytes(StandardCharsets.UTF_8));
        return new Response(new byte[0]);
    }

    private Response createNewFile(Request request) throws IOException {
        if (Objects.equals(request.getBody(), "")) {
            if (Objects.equals(request.getParams().get("type"), "File")) {
                new File(request.getParams().get("path")).createNewFile();
                return new Response(new byte[0]);
            } else {
                new File(request.getParams().get("path")).mkdirs();
                return new Response(new byte[0]);
            }
        }else{
            new File(request.getParams().get("path")).createNewFile();
            OutputStream out = new FileOutputStream(request.getParams().get("path"));
            String[] strings = request.getBody().split(",");
            byte [] bytes = new byte[strings.length];
            for (int i = 0; i < strings.length; i++) {
                Integer integer = Integer.valueOf(strings[i]);
                bytes[i] = integer.byteValue();
            }
            out.write(bytes);
            out.close();
            return new Response(new byte[0]);
        }

    }

    private Response handleSalaryCalculator(Request request) throws JsonProcessingException {
        String salary = request.getParams().get("salary");
        SalaryInformationResponse response = SalaryCalculatorService.getSalaryInformation(new GrossSalary(new BigDecimal(salary)));
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(response);
        return new Response(json.getBytes(StandardCharsets.UTF_8));
    }

    private static Response getProjectFiles(Request request) throws IOException {
        if (Authorization.isValidKey(request)) {
            String path = request.getParams().get("path");
            if (isFile(path)) {
                if (isImage(path)) {
                    return getImageBase64Bytes(path);
                } else {
                    return getFileBytes(path);
                }
            } else {
                return getDirectoryListBytes(path);
            }
        }
        return new Response(CODE401, new byte[0], true);
    }

    private static Response getImageBase64Bytes(String path) throws IOException {
        File file = new File(path);
        byte[] bytes = new FileInputStream(file).readAllBytes();
        return new Response(Base64.getEncoder().encode(bytes));
    }

    private static Response getFileBytes(String path) throws IOException {
        File file = new File(path);
        InputStream in = new FileInputStream(file);
        return new Response(in.readAllBytes());
    }

    private static Response getDirectoryListBytes(String path) throws IOException {
        FilenameFilter filenameFilter = getFileNameFilter();
        File[] fileArr = new File(path).listFiles(filenameFilter);
        String[] files = sortFilesByDirectory(fileArr);
        String json = new ObjectMapper().writeValueAsString(files);
        return new Response(json.getBytes(StandardCharsets.UTF_8));
    }

    private static FilenameFilter getFileNameFilter() {
        return (dir, name) -> {
            if (Objects.equals(dir, new File("./"))) {
                return !Objects.equals(name, ".git") && !Objects.equals(name, ".idea") && !Objects.equals(name, "target") && !Objects.equals(name, "Authorization.txt");
            } else return true;
        };
    }

    private static boolean isFile(String path) {
        if ((path.length() < 5) || (path.substring(path.length() - 5).contains("./src"))) {
            return false;
        }
        String extension = path.substring(path.length() - 5);
        return extension.contains(".");
    }

    private static boolean isImage(String path) {
        return path.contains(".jpg") || path.contains(".ico") || path.contains(".png") || path.contains(".jpeg");
    }

    private static Response createPictureListJson(Request request) throws IOException {
        String[] names = new File("./resources").list();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(names);
        return new Response(json.getBytes(StandardCharsets.UTF_8));
    }

    private static String[] sortFilesByDirectory(File[] files) {
        Arrays.sort(files, Comparator.comparing(File::isDirectory).reversed().thenComparing(Comparator.naturalOrder()));
        String[] fileStrings = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            fileStrings[i] = files[i].getName();
        }
        return fileStrings;
    }
}
