package storage.json;

import domain.Client;
import ports.ClientPort;
import utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonClientStore implements ClientPort {

    private static final String FILE = "data/users.json";

    @Override
    public Client findByUsername(String username) {

        String content = JsonUtils.readFile(FILE);
        if (content == null || content.isEmpty()) return null;

        JSONArray users = new JSONArray(content);

        for (int i = 0; i < users.length(); i++) {
            JSONObject o = users.getJSONObject(i);

            if (o.getString("username").equals(username)) {
                return new Client(
                        o.getString("username"),
                        o.getString("pin"),
                        o.getDouble("balance"),
                        o.getInt("failedAttempts"),
                        o.getBoolean("locked")
                );
            }
        }
        return null;
    }

    @Override
    public void update(Client client) {

        JSONArray users = new JSONArray(JsonUtils.readFile(FILE));

        for (int i = 0; i < users.length(); i++) {
            JSONObject o = users.getJSONObject(i);

            if (o.getString("username").equals(client.getUsername())) {
                o.put("balance", client.getBalance());
                o.put("failedAttempts", client.getFailedAttempts());
                o.put("locked", client.isLocked());
                break;
            }
        }

        JsonUtils.writeFile(FILE, users.toString(2));
    }

    @Override
    public List<Client> findAll() {

        List<Client> result = new ArrayList<>();

        String content = JsonUtils.readFile(FILE);
        if (content == null || content.isEmpty()) return result;

        JSONArray users = new JSONArray(content);

        for (int i = 0; i < users.length(); i++) {
            JSONObject o = users.getJSONObject(i);

            result.add(new Client(
                    o.getString("username"),
                    o.getString("pin"),
                    o.getDouble("balance"),
                    o.getInt("failedAttempts"),
                    o.getBoolean("locked")
            ));
        }

        return result;
    }
}
