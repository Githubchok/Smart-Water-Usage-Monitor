import javax.swing.*;
import java.awt.*;

/**
 * A themed Swing window for recording a single day's water usage (in liters).
 *
 * <p><strong>Features:</strong></p>
 * <ul>
 *   <li>Monospace-labeled text field for numeric input of daily usage.</li>
 *   <li>Submit button that validates input and displays success/error feedback.</li>
 *   <li>Automatic alerting via {@link AlertCenter} if usage crosses thresholds:
 *       <ul>
 *         <li>High usage: &gt; 180&nbsp;L &rarr; {@link AlertCenter#addHighUsage(String, double, double)}</li>
 *         <li>Low usage:  &lt; 50&nbsp;L  &rarr; {@link AlertCenter#addLowUsage(String, double, double)}</li>
 *       </ul>
 *   </li>
 * </ul>
 *
 * <p><strong>Threading:</strong> Construct and show instances on the Swing EDT
 * (e.g., via {@code SwingUtilities.invokeLater}).</p>
 *
 * <p><strong>Styling/Dependencies:</strong> Uses {@code WaterTheme} helpers for gradients, cards,
 * fonts, text fields, and buttons. Alerts are recorded through {@link AlertCenter}.</p>
 *
 * <p><strong>Null meter ID:</strong> If {@code meterId} is {@code null}, {@link AlertCenter}
 * will store {@code "UNKNOWN"} for the meter identifier.</p>
 *
 * @since 1.0
 */
public class RecordDailyUsageUI extends JFrame {

    /**
     * Text field for entering today's water usage in liters.
     */
    private JTextField usageField;

    /**
     * Inline feedback label that communicates validation errors or success.
     */
    private JLabel messageLabel;

    /**
     * The water meter identifier associated with the current user/session.
     * May be {@code null}, in which case alerts use {@code "UNKNOWN"}.
     */
    private final String meterId;

    /**
     * Convenience constructor that creates the UI without a meter ID.
     * <p>Alerts raised from this instance will use {@code "UNKNOWN"} as the meter ID.</p>
     */
    public RecordDailyUsageUI() {
        this(null);
    }

    /**
     * Creates the "Record Daily Usage" window for the specified meter ID,
     * builds the layout, and wires the submit action.
     *
     * <p><strong>Validation:</strong> Input must be a non-negative number; otherwise an
     * error message is displayed. On success, the label turns green and shows the
     * recorded value.</p>
     *
     * <p><strong>Alert thresholds:</strong> &lt; 50&nbsp;L → low-usage alert; &gt; 180&nbsp;L → high-usage alert.</p>
     *
     * <p>The frame is packed, given a minimum size, centered, and uses
     * {@link JFrame#DISPOSE_ON_CLOSE} so closing it does not exit the JVM.</p>
     *
     * @param meterId the associated meter ID (nullable)
     */
    public RecordDailyUsageUI(String meterId) {
        this.meterId = meterId;
        setTitle("Record Daily Water Usage");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = WaterTheme.createGradientBackground(new BorderLayout(12, 12));
        setContentPane(root);

        root.add(WaterTheme.createWaveHeader(
                "Record Daily Usage",
                "Log your daily water consumption in liters"
        ), BorderLayout.NORTH);

        JPanel card = WaterTheme.createCardPanel("Record Daily Usage", new BorderLayout(10, 10));

        JPanel formRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        formRow.setOpaque(false);
        JLabel label = new JLabel("Water Usage (Liters):");
        label.setFont(WaterTheme.bodyFont());

        usageField = new JTextField(12);
        WaterTheme.styleTextField(usageField);

        JButton submitBtn = new JButton("Submit");
        WaterTheme.stylePrimaryButton(submitBtn);
        WaterTheme.installButtonHoverEffect(submitBtn);

        formRow.add(label);
        formRow.add(usageField);
        formRow.add(submitBtn);

        messageLabel = new JLabel("Enter today's water usage in liters");
        messageLabel.setFont(WaterTheme.bodyFont());
        JPanel msgRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        msgRow.setOpaque(false);
        msgRow.add(messageLabel);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        contentPanel.add(formRow);
        contentPanel.add(msgRow);

        card.add(contentPanel, BorderLayout.CENTER);

        root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        root.add(card, BorderLayout.CENTER);

        submitBtn.addActionListener(e -> {
            String input = usageField.getText().trim();
            try {
                double usage = Double.parseDouble(input);
                if (usage < 0) throw new NumberFormatException();

                // thresholds: too low < 50L, too high > 180L
                if (usage > 180) {
                    AlertCenter.addHighUsage(meterId, usage, 180);
                } else if (usage < 50) {
                    AlertCenter.addLowUsage(meterId, usage, 50);
                }

                messageLabel.setForeground(new Color(0, 128, 0));
                messageLabel.setText("Recorded successfully: " + usage + " liters");
            } catch (NumberFormatException ex) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Invalid input, please enter a positive number");
            }
        });

        pack();
        setMinimumSize(new Dimension(480, 260));
        setLocationRelativeTo(null);
    }
}
