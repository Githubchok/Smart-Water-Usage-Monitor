import java.util.Scanner;

/**
 * Console-based access gate for the water meter system.
 *
 * <p><strong>Behavior:</strong> Prompts the user for a Water Meter ID and verifies it
 * against a small in-memory whitelist. Matching is case-insensitive (e.g., {@code wm001}
 * is accepted for {@code WM001}).</p>
 *
 * <p><strong>Demo limitations:</strong> This example does not loop on invalid input,
 * persist users, or consult a real database. It prints a status message and returns.</p>
 *
 * <p><strong>I/O note:</strong> The {@link Scanner} on {@code System.in} is not closed
 * here to avoid closing the global input stream for the JVM.</p>
 *
 * @since 1.0
 */
public class WaterMeterAccess {

    /**
     * Demo whitelist of valid meter IDs.
     * <p>Used by {@link #isMeterIdValid(String)} for a case-insensitive check.</p>
     */
    // 模拟数据库，存放已有的水表ID
    private static final String[] validMeterIds = {"WM001", "WM002", "WM003"};

    /**
     * Prompts for a Water Meter ID, validates it, and prints an access result.
     *
     * <p>If the ID fails validation, prints an error and returns immediately.
     * On success, prints a welcome message; this is where you would branch to
     * subsequent application features.</p>
     */
    public static void accessSystem() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter Water Meter ID: ");
        String inputId = scanner.nextLine();

        if (!isMeterIdValid(inputId)) {
            System.out.println("Error: Invalid Water Meter ID!");
            return; // 访问失败，退出或重试逻辑可加
        }

        System.out.println("Access granted. Welcome!");
        // 这里可以调用后续功能入口
    }

    /**
     * Checks whether the provided meter ID matches a known ID (case-insensitive).
     *
     * @param meterId the meter ID to verify; may be {@code null}
     * @return {@code true} if {@code meterId} equals any entry in {@link #validMeterIds}
     *         ignoring case; {@code false} otherwise
     */
    private static boolean isMeterIdValid(String meterId) {
        for (String id : validMeterIds) {
            if (id.equalsIgnoreCase(meterId)) {
                return true;
            }
        }
        return false;
    }
}
