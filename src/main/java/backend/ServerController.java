package backend;

import java.io.IOException;
import java.util.Map;

import static backend.ServerService.createPictureListJson;
import static backend.ServerService.createSalaryResponseJson;

public class ServerController {

    protected static String controller(String requestPath, Map<String, String> params) throws IOException {
        if (requestPath.contains(".jpg") || requestPath.contains(".jpeg") || requestPath.contains(".png") || requestPath.contains(".ico")) {
            return "./resources/" + requestPath;
        } else if (requestPath.contains(".json")) {
            if (requestPath.contains("getpicturelist")) {
                createPictureListJson();
                return "./src/main/java/backend/json/getpicturelist.json";
            }
        } else if (requestPath.contains(".js") || requestPath.contains(".css")) {
            return "./src/main/java/frontend/" + requestPath;
        } else if (requestPath.equals("")) {
            return "./src/main/java/frontend/index.html";
        } else if (requestPath.contains("?")) {
            if (requestPath.contains("salarycalculator?")) {
                createSalaryResponseJson(params);
                return "./src/main/java/backend/json/salaryResponse.json";
            }
        } else {
            return "./src/main/java/frontend/" + requestPath + ".html";
        }
        return "File not found.";
    }

}
