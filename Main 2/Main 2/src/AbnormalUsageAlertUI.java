import javax.swing.*;
import java.awt.*;

/**
 * A themed Swing window that displays abnormal usage alerts produced by {@link AlertCenter}.
 * <p>
 * The UI consists of:
 * <ul>
 *   <li>a gradient background root panel from {@link WaterTheme#createGradientBackground(LayoutManager)},</li>
 *   <li>a wave-styled header from {@link WaterTheme#createWaveHeader(String, String)}, and</li>
 *   <li>a card panel titled "Alerts" containing a scrollable, read-only, monospaced text area.</li>
 * </ul>
 * The text area is populated with the current value of {@link AlertCenter#formatAlerts()} at construction time.
 * </p>
 *
 * <h3>Usage</h3>
 * <pre>{@code
 * SwingUtilities.invokeLater(() -> {
 *     AbnormalUsageAlertUI ui = new AbnormalUsageAlertUI();
 *     ui.setVisible(true);
 * });
 * }</pre>
 *
 * <p><strong>Threading:</strong> All Swing interactions should occur on the Event Dispatch Thread (EDT).
 * Use {@link SwingUtilities#invokeLater(Runnable)} as shown above.</p>
 *
 * <p><strong>Close behavior:</strong> The frame uses {@link JFrame#DISPOSE_ON_CLOSE} so closing this window
 * disposes only this frame without terminating the application.</p>
 *
 * <p><strong>Dependencies:</strong> This class relies on {@code WaterTheme} for theming helpers and on
 * {@link AlertCenter} for alert text. Ensure both are available on the classpath.</p>
 */
public class AbnormalUsageAlertUI extends JFrame {

    /**
     * Read-only area showing the formatted list of abnormal usage alerts.
     * <p>To refresh the content later, call
     * {@code alertArea.setText(AlertCenter.formatAlerts());} on the EDT.</p>
     */
    private JTextArea alertArea;

    /**
     * Constructs the alert window, applies the WaterTheme styling, and loads the current alerts.
     * <p>
     * The frame is sized to 560x360 pixels, centered on screen, and not shown until
     * {@link #setVisible(boolean)} is invoked by the caller.
     * </p>
     */
    public AbnormalUsageAlertUI() {
        setTitle("Abnormal Usage Alerts");
        setSize(560, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = WaterTheme.createGradientBackground(new BorderLayout(12, 12));
        setContentPane(root);

        root.add(WaterTheme.createWaveHeader("Abnormal Usage Alerts", "We’ll notify you when something looks off"), BorderLayout.NORTH);

        JPanel card = WaterTheme.createCardPanel("Alerts", new BorderLayout(10, 10));

        alertArea = new JTextArea(15, 40);
        alertArea.setEditable(false);
        alertArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        alertArea.setText(AlertCenter.formatAlerts());

        JScrollPane scroll = new JScrollPane(alertArea);
        WaterTheme.styleScrollPane(scroll);
        card.add(scroll, BorderLayout.CENTER);
        root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        root.add(card, BorderLayout.CENTER);
    }
}
