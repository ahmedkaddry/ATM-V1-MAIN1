package services;

import domain.Client;
import domain.Movement;
import domain.MovementKind;
import domain.Terminal;
import ports.ClientPort;
import ports.MovementPort;
import ports.TerminalPort;

import java.time.LocalDateTime;

public class TellerService {

    private final ClientPort clientPort;
    private final TerminalPort terminalPort;
    private final MovementPort movementPort;

    public TellerService(
            ClientPort clientPort,
            TerminalPort terminalPort,
            MovementPort movementPort
    ) {
        this.clientPort = clientPort;
        this.terminalPort = terminalPort;
        this.movementPort = movementPort;
    }

    public boolean deposit(Client client, double amount, boolean printReceipt) {

        if (amount <= 0) {
            System.out.println("❌ Invalid amount");
            return false;
        }

        if (amount != (int) amount) {
            System.out.println("❌ Amount must be a whole number");
            return false;
        }

        int intAmount = (int) amount;

        client.setBalance(client.getBalance() + intAmount);
        clientPort.update(client);

        movementPort.save(
                new Movement(
                        client.getUsername(),
                        intAmount,
                        MovementKind.DEPOSIT,
                        printReceipt,
                        LocalDateTime.now()
                )
        );

        Terminal terminal = terminalPort.loadTerminal();
        if (printReceipt) {
            decrementReceiptPaper(terminal);
        }

        terminalPort.saveTerminal(terminal);

        printReceiptSummary("DEPOSIT", intAmount, printReceipt);
        System.out.println("✅ Deposit successful");
        return true;
    }

    public boolean withdraw(Client client, double amount, boolean printReceipt) {

        if (amount <= 0) {
            System.out.println("❌ Invalid amount");
            return false;
        }

        if (amount != (int) amount) {
            System.out.println("❌ Amount must be a whole number");
            return false;
        }

        int intAmount = (int) amount;
        Terminal terminal = terminalPort.loadTerminal();

        if (client.getBalance() < intAmount) {
            System.out.println("❌ Insufficient balance");
            return false;
        }

        if (terminal.getCash() < intAmount) {
            System.out.println("❌ ATM has insufficient cash");
            return false;
        }

        if (!dispenseCash(terminal, intAmount)) {
            System.out.println("❌ ATM cannot dispense this amount with available bills");
            return false;
        }

        client.setBalance(client.getBalance() - intAmount);
        clientPort.update(client);

        movementPort.save(
                new Movement(
                        client.getUsername(),
                        intAmount,
                        MovementKind.WITHDRAW,
                        printReceipt,
                        LocalDateTime.now()
                )
        );

        if (printReceipt) {
            decrementReceiptPaper(terminal);
        }

        terminalPort.saveTerminal(terminal);

        printReceiptSummary("WITHDRAW", intAmount, printReceipt);
        System.out.println("💵 Cash withdrawn");
        return true;
    }

    public boolean transfer(Client sender, String receiverUsername, double amount) {

        if (amount <= 0) {
            System.out.println("❌ Invalid amount");
            return false;
        }

        if (amount != (int) amount) {
            System.out.println("❌ Amount must be a whole number");
            return false;
        }

        int intAmount = (int) amount;

        if (sender.getBalance() < intAmount) {
            System.out.println("❌ Insufficient balance");
            return false;
        }

        Client receiver = clientPort.findByUsername(receiverUsername);

        if (receiver == null) {
            System.out.println("❌ Receiver not found");
            return false;
        }

        if (receiver.getUsername().equals(sender.getUsername())) {
            System.out.println("❌ Cannot transfer to yourself");
            return false;
        }

        sender.setBalance(sender.getBalance() - intAmount);
        receiver.setBalance(receiver.getBalance() + intAmount);

        clientPort.update(sender);
        clientPort.update(receiver);

        movementPort.save(
                new Movement(
                        sender.getUsername(),
                        intAmount,
                        MovementKind.TRANSFER,
                        false,
                        LocalDateTime.now()
                )
        );

        printReceiptSummary("TRANSFER", intAmount, false);
        System.out.println("✅ Transfer successful");
        return true;
    }

    private boolean dispenseCash(Terminal terminal, int amount) {
        int[] available = {
                terminal.getCash200(),
                terminal.getCash100(),
                terminal.getCash50(),
                terminal.getCash20()
        };
        int[] values = {200, 100, 50, 20};
        int[] used = new int[4];

        if (!findCombination(amount, 0, available, values, used)) {
            return false;
        }

        terminal.setCash200(terminal.getCash200() - used[0]);
        terminal.setCash100(terminal.getCash100() - used[1]);
        terminal.setCash50(terminal.getCash50() - used[2]);
        terminal.setCash20(terminal.getCash20() - used[3]);
        return true;
    }

    private boolean findCombination(int amount, int index, int[] available, int[] values, int[] used) {
        if (amount == 0) {
            return true;
        }
        if (index >= values.length) {
            return false;
        }

        int maxNotes = Math.min(available[index], amount / values[index]);
        for (int count = maxNotes; count >= 0; count--) {
            used[index] = count;
            if (findCombination(amount - count * values[index], index + 1, available, values, used)) {
                return true;
            }
        }

        used[index] = 0;
        return false;
    }

    private void decrementReceiptPaper(Terminal terminal) {
        if (terminal.getReceiptPaper() <= 0) {
            System.out.println("⚠️ No receipt paper available");
            return;
        }
        terminal.setReceiptPaper(terminal.getReceiptPaper() - 1);
    }

    private void printReceiptSummary(String type, int amount, boolean printed) {
        System.out.println("----- RECEIPT -----");
        System.out.println("Transaction: " + type);
        System.out.println("Amount: " + amount);
        System.out.println("Printed: " + (printed ? "Yes" : "No"));
        System.out.println("-------------------");
    }
}
