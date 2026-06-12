import ports.ClientPort;
import storage.json.JsonClientStore;

public class UserTest {
    public static void main(String[] args) {
        ClientPort clientPort = new JsonClientStore();

        var client = clientPort.findByUsername("moha");
        assertTrue(client != null, "User should exist");
        assertTrue(client.getBalance() >= 0, "Balance should be non-negative");

        System.out.println("UserTest passed");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
