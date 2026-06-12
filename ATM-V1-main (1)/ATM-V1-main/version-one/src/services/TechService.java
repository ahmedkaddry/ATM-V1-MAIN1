package services;

import org.json.JSONObject;
import utils.JsonUtils;

public class TechService {

    private static final String FILE = "data/technician.json";

    public boolean login(String username, String pin) {

        String content = JsonUtils.readFile(FILE);

        if (content == null || content.isEmpty()) {
            System.out.println("❌ Technician data missing");
            return false;
        }

        JSONObject obj = new JSONObject(content);

        String storedUsername = obj.getString("username");
        String storedPin = obj.getString("pin");

        if (storedUsername.equals(username) && storedPin.equals(pin)) {
            System.out.println("✅ Technician logged in");
            return true;
        }

        System.out.println("❌ Invalid technician credentials");
        return false;
    }
}
