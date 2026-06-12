package storage.json;

import domain.Terminal;
import ports.TerminalPort;
import utils.JsonUtils;

import org.json.JSONObject;

public class JsonTerminalStore implements TerminalPort {

    private static final String FILE = "data/atm.json";

    @Override
    public Terminal loadTerminal() {
        JSONObject obj = JsonUtils.readJSON(FILE);
        if (obj == null) {
            return new Terminal(0, 0, 0, 0, 0);
        }
        return new Terminal(
                obj.getInt("cash20"),
                obj.getInt("cash50"),
                obj.getInt("cash100"),
                obj.getInt("cash200"),
                obj.getInt("receiptPaper")
        );
    }

    @Override
    public void saveTerminal(Terminal terminal) {
        JSONObject obj = new JSONObject();
        obj.put("cash20", terminal.getCash20());
        obj.put("cash50", terminal.getCash50());
        obj.put("cash100", terminal.getCash100());
        obj.put("cash200", terminal.getCash200());
        obj.put("receiptPaper", terminal.getReceiptPaper());
        JsonUtils.writeFile(FILE, obj.toString(2));
    }
}
