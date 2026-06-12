import ports.TerminalPort;
import storage.json.JsonTerminalStore;

public class AtmTest {
    public static void main(String[] args) {
        TerminalPort terminalPort = new JsonTerminalStore();
        var terminal = terminalPort.loadTerminal();

        assertTrue(terminal.getCash() >= 0, "Cash should be non-negative");
        assertTrue(terminal.getReceiptPaper() >= 0, "Receipt paper should be non-negative");

        System.out.println("AtmTest passed");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
