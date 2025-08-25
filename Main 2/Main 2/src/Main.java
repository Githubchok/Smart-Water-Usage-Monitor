import javax.swing.SwingUtilities;

/**
 * Application entry point for the Water Meter ID UI.
 *
 * <p>Launches the Swing user interface on the Event Dispatch Thread (EDT)
 * via {@link SwingUtilities#invokeLater(Runnable)} to ensure all UI work is
 * performed on the correct thread.</p>
 *
 * <p><strong>Dependencies:</strong> Requires {@code WaterMeterIDUI} to be present
 * on the classpath. Visibility and close behavior are determined by that class.</p>
 *
 * @since 1.0
 */
public class Main {

    /**
     * JVM entry point. Schedules construction and display of {@link WaterMeterIDUI}
     * on the EDT to keep the UI responsive and thread-safe.
     *
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WaterMeterIDUI().setVisible(true);
        });
    }
}

