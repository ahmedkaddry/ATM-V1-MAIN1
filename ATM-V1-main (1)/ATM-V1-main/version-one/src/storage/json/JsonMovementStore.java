package storage.json;

import domain.Movement;
import domain.MovementKind;
import ports.MovementPort;
import utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonMovementStore implements MovementPort {

    private static final String FILE = "data/transactions.json";

    @Override
    public void save(Movement movement) {
        JSONArray arr;
        String content = JsonUtils.readFile(FILE);
        if (content == null || content.isEmpty()) {
            arr = new JSONArray();
        } else {
            arr = new JSONArray(content);
        }

        JSONObject obj = new JSONObject();
        obj.put("username", movement.getUsername());
        obj.put("amount", movement.getAmount());
        obj.put("type", movement.getKind().name());
        obj.put("receipt", movement.isReceiptPrinted());
        obj.put("time", movement.getTime().toString());

        arr.put(obj);
        JsonUtils.writeFile(FILE, arr.toString(2));
    }

    @Override
    public List<Movement> findAll() {
        List<Movement> result = new ArrayList<>();

        String content = JsonUtils.readFile(FILE);
        if (content == null || content.isEmpty()) return result;

        JSONArray arr = new JSONArray(content);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.getJSONObject(i);
            result.add(new Movement(
                    o.getString("username"),
                    o.getInt("amount"),
                    MovementKind.valueOf(o.getString("type")),
                    o.getBoolean("receipt"),
                    LocalDateTime.parse(o.getString("time"))
            ));
        }

        return result;
    }
}
