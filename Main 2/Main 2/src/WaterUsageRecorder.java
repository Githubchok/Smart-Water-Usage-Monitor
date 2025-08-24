/**
 * Console utility for recording a single day's water usage.
 *
 * <p><strong>Flow:</strong> {@link #recordDailyWaterUsage()} prompts for input,
 * validates it, simulates persistence, and prints a success or error message.</p>
 *
 * <p><strong>I/O:</strong> Reads from {@code System.in} using a {@link java.util.Scanner}.
 * The scanner is intentionally not closed to avoid closing the global input stream.</p>
 *
 * @since 1.0
 */
public class WaterUsageRecorder {

    /**
     * Orchestrates the end-to-end recording process:
     * <ol>
     *   <li>Prompt and read a usage value from standard input,</li>
     *   <li>Validate the value,</li>
     *   <li>If invalid, print an error and return,</li>
     *   <li>If valid, simulate saving and print a success message.</li>
     * </ol>
     */
    public static void recordDailyWaterUsage() {
        // Start
        double dailyUsage = inputDailyUsage();

        if (!isInputValid(dailyUsage)) {
            showError();
            return;
        }

        saveUsageData(dailyUsage);
        showSuccess();
        // End
    }

    /**
     * Prompts the user and reads today's water usage in liters from {@code System.in}.
     *
     * <p>If parsing fails, returns {@code -1}, which will be rejected by validation.</p>
     *
     * @return the entered usage value, or {@code -1} on invalid input
     */
    private static double inputDailyUsage() {
        // Simulate user input (replace with actual input logic as needed)
        System.out.print("Enter today's water usage in liters: ");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        double usage = -1;
        try {
            usage = scanner.nextDouble();
        } catch (Exception e) {
            // Invalid input, will be handled in validation
        }
        return usage;
    }

    /**
     * Validates the usage amount.
     *
     * <p>Rule: usage must be in the range {@code [0, 10000)} liters.</p>
     *
     * @param usage the usage value to check
     * @return {@code true} if valid; {@code false} otherwise
     */
    private static boolean isInputValid(double usage) {
        // Example validation: usage must be non-negative and reasonable
        return usage >= 0 && usage < 10000;
    }

    /**
     * Prints a standardized validation error message to {@code System.out}.
     */
    private static void showError() {
        System.out.println("Error: Invalid input. Please enter a valid water usage amount.");
    }

    /**
     * Simulates persisting the provided usage value.
     *
     * <p>Replace this stub with real persistence logic (e.g., database write, API call).</p>
     *
     * @param usage the validated usage amount to save
     */
    private static void saveUsageData(double usage) {
        // Simulate saving data (replace with actual data persistence logic)
        System.out.println("Saving usage data: " + usage + " liters.");
    }

    /**
     * Prints a success confirmation to {@code System.out}.
     */
    private static void showSuccess() {
        System.out.println("Water usage recorded successfully!");
    }

    /**
     * CLI entry point that invokes {@link #recordDailyWaterUsage()}.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        recordDailyWaterUsage();
    }
}
