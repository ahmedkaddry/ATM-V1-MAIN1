package services;

import domain.Client;
import ports.ClientPort;

public class AccessService {

    private static final int MAX_ATTEMPTS = 3;
    private final ClientPort clientPort;

    public AccessService(ClientPort clientPort) {
        this.clientPort = clientPort;
    }

    public Client authenticate(String username, String pin) {

        Client client = clientPort.findByUsername(username);

        if (client == null) {
            System.out.println("❌ User not found");
            return null;
        }

        if (client.isLocked()) {
            System.out.println("🔒 Account is locked. Please contact the bank.");
            return null;
        }

        if (client.getPin().equals(pin)) {
            client.setFailedAttempts(0);
            clientPort.update(client);
            return client;
        }

        int attempts = client.getFailedAttempts() + 1;
        client.setFailedAttempts(attempts);

        if (attempts >= MAX_ATTEMPTS) {
            client.setLocked(true);
            System.out.println("🔒 Account locked after 3 failed attempts.");
        } else {
            System.out.println("❌ Wrong PIN. Attempts left: " + (MAX_ATTEMPTS - attempts));
        }

        clientPort.update(client);
        return null;
    }
}
