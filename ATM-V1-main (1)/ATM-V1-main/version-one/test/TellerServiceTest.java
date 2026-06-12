import domain.Client;
import ports.ClientPort;
import ports.MovementPort;
import ports.TerminalPort;
import services.TellerService;
import storage.json.JsonClientStore;
import storage.json.JsonMovementStore;
import storage.json.JsonTerminalStore;

public class TellerServiceTest {
    public static void main(String[] args) {
        ClientPort clientPort = new JsonClientStore();
        TerminalPort terminalPort = new JsonTerminalStore();
        MovementPort movementPort = new JsonMovementStore();

        TellerService service = new TellerService(clientPort, terminalPort, movementPort);

        Client client = clientPort.findByUsername("moha");
        double start = client.getBalance();

        service.deposit(client, 50, false);
        assertTrue(client.getBalance() == start + 50, "Deposit should add balance");

        service.withdraw(client, 20, false);
        assertTrue(client.getBalance() == start + 30, "Withdraw should reduce balance");

        System.out.println("TellerServiceTest passed");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
