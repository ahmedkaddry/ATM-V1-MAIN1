package utils;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonUtils {

    private static Path resolvePath(String relativePath) {
        Path path = Path.of(relativePath);
        if (Files.exists(path)) {
            return path;
        }

        Path alt1 = Path.of("version-one", relativePath);
        if (Files.exists(alt1)) {
            return alt1;
        }

        Path alt2 = Path.of("ATM-V1-main", "version-one", relativePath);
        if (Files.exists(alt2)) {
            return alt2;
        }

        Path current = Path.of("");
        while (current != null) {
            Path candidate = current.resolve(relativePath);
            if (Files.exists(candidate)) {
                return candidate;
            }
            current = current.getParent();
        }

        return path;
    }

    public static String readFile(String path) {
        try {
            return Files.readString(resolvePath(path));
        } catch (IOException e) {
            return null;
        }
    }

    public static void writeFile(String path, String content) {
        try {
            Files.writeString(resolvePath(path), content);
        } catch (IOException e) {
            System.out.println("❌ Failed to write file: " + path);
        }
    }

    public static JSONObject readJSON(String path) {
        String content = readFile(path);
        if (content == null || content.isEmpty()) {
            return null;
        }
        return new JSONObject(content);
    }
}
