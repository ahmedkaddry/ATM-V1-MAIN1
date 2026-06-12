package app;

import domain.Client;
import domain.Terminal;
import ports.ClientPort;
import ports.TerminalPort;
import ports.MovementPort;
import services.AccessService;
import services.TellerService;
import services.TechService;
import storage.json.JsonClientStore;
import storage.json.JsonTerminalStore;
import storage.json.JsonMovementStore;

import java.util.Scanner;

public class Launcher {

    private static final Scanner scanner = new Scanner(System.in);

    private static final ClientPort clientPort = new JsonClientStore();
    private static final TerminalPort terminalPort = new JsonTerminalStore();
    private static final MovementPort movementPort = new JsonMovementStore();

    private static final AccessService accessService = new AccessService(clientPort);
    private static final TellerService tellerService =
            new TellerService(clientPort, terminalPort, movementPort);

    public static void main(String[] args) {

        boolean running = true;

        while (running) {
            System.out.println("\n===== ATM SYSTEM =====");
            System.out.println("1. User Login");
            System.out.println("2. Technician");
            System.out.println("3. Exit");

            int choice = readInt("Choose option: ");

            switch (choice) {
                case 1 -> userLogin();
                case 2 -> technicianLogin();
                case 3 -> {
                    System.out.println("👋 Goodbye");
                    running = false;
                }
                default -> System.out.println("❌ Invalid option");
            }
        }
    }

    // =========================
    // USER LOGIN
    // =========================
    private static void userLogin() {

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("PIN: ");
        String pin = scanner.nextLine();

        Client client = accessService.authenticate(username, pin);

        if (client != null) {
            userMenu(client);
        }
    }

    // =========================
    // USER MENU
    // =========================
    private static void userMenu(Client client) {

        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n===== USER MENU =====");
            System.out.println("1. Check balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit to ATM menu");

            int choice = readInt("Choose option: ");

            switch (choice) {

                case 1 -> System.out.println("💰 Balance: " + client.getBalance());

                case 2 -> {
                    double amount = readDouble("Amount: ");
                    boolean receipt = readBoolean("Print receipt? (true/false): ");
                    tellerService.deposit(client, amount, receipt);
                }

                case 3 -> {
                    double amount = readDouble("Amount: ");
                    boolean receipt = readBoolean("Print receipt? (true/false): ");
                    tellerService.withdraw(client, amount, receipt);
                }

                case 4 -> {
                    System.out.print("Receiver username: ");
                    String receiver = scanner.nextLine();
                    double amount = readDouble("Amount: ");
                    tellerService.transfer(client, receiver, amount);
                }

                case 5 -> loggedIn = false;

                default -> System.out.println("❌ Invalid option");
            }
        }
    }

    // =========================
    // TECHNICIAN LOGIN
    // =========================
    private static void technicianLogin() {

        TechService techService = new TechService();

        System.out.print("Technician username: ");
        String username = scanner.nextLine();

        System.out.print("Technician password: ");
        String password = scanner.nextLine();

        if (techService.login(username, password)) {
            technicianMenu();
        } else {
            System.out.println("❌ Invalid technician credentials");
        }
    }

    // =========================
    // TECHNICIAN MENU (VIEW ONLY)
    // =========================
    private static void technicianMenu() {

        Terminal terminal = terminalPort.loadTerminal();

        boolean running = true;

        while (running) {
            System.out.println("\n===== TECHNICIAN MENU =====");
            System.out.println("1. View ATM status");
            System.out.println("2. Exit to ATM menu");

            int choice = readInt("Choose option: ");

            switch (choice) {
                case 1 -> {
                    System.out.println("💰 Cash available: " + terminal.getCash());
                    System.out.println("🧾 Receipt paper left: " + terminal.getReceiptPaper());
                }
                case 2 -> running = false;
                default -> System.out.println("❌ Invalid option");
            }
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value <= 0) {
                    System.out.println("❌ Amount must be greater than zero");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid amount");
            }
        }
    }

    private static boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true")) {
                return true;
            }
            if (input.equals("false")) {
                return false;
            }
            System.out.println("❌ Please enter true or false");
        }
    }
}
