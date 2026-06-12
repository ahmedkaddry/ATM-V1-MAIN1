import services.TechService;

public class TechnicianTest {
    public static void main(String[] args) {
        TechService tech = new TechService();
        boolean ok = tech.login("tech", "9999");
        assertTrue(ok, "Technician should login with correct credentials");
        System.out.println("TechnicianTest passed");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
