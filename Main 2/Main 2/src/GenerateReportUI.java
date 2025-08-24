import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * A themed Swing window for generating and displaying a usage report over a user-selected date range.
 *
 * <p>The UI includes:</p>
 * <ul>
 *   <li>Two text fields for start and end dates (YYYY-MM-DD),</li>
 *   <li>a scrollable, read-only text area where the report is rendered, and</li>
 *   <li>"Generate Report" and "Clear" buttons styled via {@link WaterTheme}.</li>
 * </ul>
 *
 * <p><strong>Behavior:</strong> Clicking <em>Generate Report</em> validates input and then uses a
 * {@link SwingWorker} to simulate background processing before populating the text area with a mock report.
 * The mock report currently assumes 31 days (January 2024) and generates random daily usage values.</p>
 *
 * <p><strong>Threading:</strong> All UI updates occur on the Event Dispatch Thread (EDT). Background work is
 * performed in {@code SwingWorker#doInBackground()} to keep the UI responsive.</p>
 *
 * <p><strong>Close behavior:</strong> The frame uses {@link JFrame#DISPOSE_ON_CLOSE} so closing this window
 * disposes only this frame without terminating the application.</p>
 *
 * <p><strong>Styling:</strong> Relies on {@code WaterTheme} helpers for consistent look-and-feel.</p>
 *
 * <p><strong>Note:</strong> This class generates mock data and does not parse or validate date formats beyond
 * non-empty checks. Integrate real data loading and date validation as needed.</p>
 *
 * @since 1.0
 */
public class GenerateReportUI extends JFrame {

    /**
     * Text field for the report start date (expected format: {@code yyyy-MM-dd}).
     */
    private JTextField startDateField;

    /**
     * Text field for the report end date (expected format: {@code yyyy-MM-dd}).
     */
    private JTextField endDateField;

    /**
     * Read-only area displaying the generated report text.
     */
    private JTextArea reportArea;

    /**
     * Button to trigger report generation.
     */
    private JButton generateBtn;

    /**
     * Button to clear the report output and reset date fields to defaults.
     */
    private JButton clearBtn;

    /**
     * Constructs the usage report window, initializes components, lays out the UI, and wires event handlers.
     * <p>
     * The frame is sized to 720x560 pixels, centered on screen, and is not shown until
     * {@link #setVisible(boolean)} is invoked by the caller.
     * </p>
     */
    public GenerateReportUI() {
        setTitle("Generate Usage Report");
        setSize(720, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeComponents();
        setupLayout();
        setupEventListeners();
    }

    /**
     * Initializes Swing components (fields, text area, buttons) and applies {@link WaterTheme} styles
     * to interactive controls.
     */
    private void initializeComponents() {
        // Date input fields
        startDateField = new JTextField("2024-01-01", 10);
        endDateField = new JTextField("2024-01-31", 10);

        // Report display area
        reportArea = new JTextArea(20, 50);
        reportArea.setEditable(false);
        reportArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        reportArea.setText("Please select a date range and click 'Generate Report' to view usage data.");

        // Buttons
        generateBtn = new JButton("Generate Report");
        clearBtn = new JButton("Clear");
        WaterTheme.stylePrimaryButton(generateBtn);
        WaterTheme.styleSecondaryButton(clearBtn);
    }

    /**
     * Builds the window layout:
     * <ul>
     *   <li>Applies a gradient background root panel,</li>
     *   <li>adds a wave-styled header,</li>
     *   <li>creates a card panel that hosts the filter row and the scrollable report area.</li>
     * </ul>
     * Uses {@link WaterTheme} helpers for consistent styling and spacing.
     */
    private void setupLayout() {
        JPanel root = WaterTheme.createGradientBackground(new BorderLayout(12, 12));
        setContentPane(root);

        root.add(WaterTheme.createWaveHeader("Usage Report", "Generate insights from a selected period"), BorderLayout.NORTH);

        JPanel card = WaterTheme.createCardPanel("Usage Report", new BorderLayout(10, 10));

        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        filterRow.setOpaque(false);
        JLabel sLabel = new JLabel("Start Date:");
        JLabel eLabel = new JLabel("End Date:");
        sLabel.setFont(WaterTheme.bodyFont());
        eLabel.setFont(WaterTheme.bodyFont());
        WaterTheme.styleTextField(startDateField);
        WaterTheme.styleTextField(endDateField);
        filterRow.add(sLabel);
        filterRow.add(startDateField);
        filterRow.add(eLabel);
        filterRow.add(endDateField);
        filterRow.add(generateBtn);
        filterRow.add(clearBtn);

        card.add(filterRow, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(reportArea);
        WaterTheme.styleScrollPane(scroll);
        card.add(scroll, BorderLayout.CENTER);

        root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        root.add(card, BorderLayout.CENTER);
    }

    /**
     * Attaches action listeners:
     * <ul>
     *   <li><strong>Generate:</strong> Validates input and starts a {@link SwingWorker} to simulate report creation.</li>
     *   <li><strong>Clear:</strong> Resets the report and date fields to their defaults.</li>
     * </ul>
     */
    private void setupEventListeners() {
        generateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });

        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearReport();
            }
        });
    }

    /**
     * Validates date inputs and runs a background task to build a report string.
     * <p>While running, the Generate button shows a busy label and is disabled to prevent duplicate work.</p>
     * <p><strong>Note:</strong> This method checks only for non-empty fields; it does not parse or validate date formats.</p>
     */
    private void generateReport() {
        String startDate = startDateField.getText().trim();
        String endDate = endDateField.getText().trim();

        if (startDate.isEmpty() || endDate.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both start and end dates.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Simulate report generation
        generateBtn.setText("Generating...");
        generateBtn.setEnabled(false);

        // Use SwingWorker for background processing (simulated delay)
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                Thread.sleep(1000); // Simulate processing time
                return createReport(startDate, endDate);
            }

            @Override
            protected void done() {
                try {
                    String report = get();
                    reportArea.setText(report);
                } catch (Exception e) {
                    reportArea.setText("Error generating report: " + e.getMessage());
                }
                generateBtn.setText("Generate Report");
                generateBtn.setEnabled(true);
            }
        };

        worker.execute();
    }

    /**
     * Builds a mock usage report for the given period.
     *
     * <p>The current implementation:</p>
     * <ul>
     *   <li>Assumes a fixed period of 31 days (January 2024) for the daily breakdown,</li>
     *   <li>generates random daily usage between 80 and 200 liters,</li>
     *   <li>computes total, average, max, and min statistics, and</li>
     *   <li>classifies average daily usage as HIGH, NORMAL, or LOW with recommendations.</li>
     * </ul>
     *
     * @param startDate the start date text (not parsed; used for display only)
     * @param endDate   the end date text (not parsed; used for display only)
     * @return a formatted, multi-section report string
     */
    private String createReport(String startDate, String endDate) {
        StringBuilder report = new StringBuilder();
        Random random = new Random();

        report.append("=====================================\n");
        report.append("        WATER USAGE REPORT\n");
        report.append("=====================================\n\n");

        report.append("Report Generated: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n");
        report.append("Period: ").append(startDate).append(" to ").append(endDate).append("\n\n");

        // Generate mock data
        int totalDays = 31; // Simplified
        double totalUsage = 0;
        double maxUsage = 0;
        double minUsage = Double.MAX_VALUE;

        report.append("DAILY USAGE BREAKDOWN:\n");
        report.append("Date          Usage (Liters)   Status\n");
        report.append("-------------------------------------\n");

        for (int day = 1; day <= totalDays; day++) {
            double dailyUsage = 80 + random.nextDouble() * 120; // 80-200 liters
            totalUsage += dailyUsage;
            maxUsage = Math.max(maxUsage, dailyUsage);
            minUsage = Math.min(minUsage, dailyUsage);

            String status = dailyUsage > 180 ? "High" : dailyUsage < 100 ? "Low" : "Normal";

            report.append(String.format("2024-01-%02d    %8.1f         %s\n",
                    day, dailyUsage, status));
        }

// Low Usage: < 100L/day (displays "Low")
// Normal Usage: 100-180L/day (displays "Normal")
// High Usage: > 180L/day (displays "High")


        report.append("\n");
        report.append("SUMMARY STATISTICS:\n");
        report.append("-------------------\n");
        report.append(String.format("Total Usage:      %.1f liters\n", totalUsage));
        report.append(String.format("Average Daily:    %.1f liters\n", totalUsage / totalDays));
        report.append(String.format("Maximum Daily:    %.1f liters\n", maxUsage));
        report.append(String.format("Minimum Daily:    %.1f liters\n", minUsage));

        // Usage categorization
        double avgDaily = totalUsage / totalDays;
        String usageCategory;
        if (avgDaily > 150) {
            usageCategory = "HIGH USAGE - Consider conservation measures";
        } else if (avgDaily < 100) {
            usageCategory = "LOW USAGE - Efficient consumption";
        } else {
            usageCategory = "NORMAL USAGE - Within expected range";
        }

        report.append(String.format("Usage Category:   %s\n", usageCategory));

        report.append("\n");
        report.append("RECOMMENDATIONS:\n");
        report.append("----------------\n");
        if (avgDaily > 150) {
            report.append("• Check for leaks in pipes and fixtures\n");
            report.append("• Consider installing water-efficient appliances\n");
            report.append("• Monitor usage during peak consumption times\n");
        } else if (avgDaily < 100) {
            report.append("• Excellent water conservation practices\n");
            report.append("• Continue current usage patterns\n");
        } else {
            report.append("• Maintain current usage levels\n");
            report.append("• Regular monitoring recommended\n");
        }

        report.append("\n=====================================\n");
        report.append("          END OF REPORT\n");
        report.append("=====================================");

        return report.toString();
    }

    /**
     * Clears the report text and resets both date fields to their default values (January 2024).
     */
    private void clearReport() {
        reportArea.setText("Please select a date range and click 'Generate Report' to view usage data.");
        startDateField.setText("2024-01-01");
        endDateField.setText("2024-01-31");
    }
}
