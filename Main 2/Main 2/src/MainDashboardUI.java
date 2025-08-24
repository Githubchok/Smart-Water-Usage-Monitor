import java.awt.*;
import javax.swing.*;

/**
 * Main application dashboard window shown after a user signs in with a meter ID.
 *
 * <p>The dashboard displays:</p>
 * <ul>
 *   <li>A themed header with the current Water Meter ID and a Logout button,</li>
 *   <li>a "Quick Actions" card with buttons to open feature windows, and</li>
 *   <li>a "User Information" card populated from the provided meter ID.</li>
 * </ul>
 *
 * <p><strong>Navigation:</strong> Buttons open the following windows:
 * <ul>
 *   <li>{@code RecordDailyUsageUI} – record daily water usage,</li>
 *   <li>{@link GenerateReportUI} – generate a usage report,</li>
 *   <li>{@link AbnormalUsageAlertUI} – view abnormal usage alerts.</li>
 * </ul>
 *
 * <p><strong>Styling:</strong> This class relies on {@code WaterTheme} for layout and component styling
 * (gradient backgrounds, cards, fonts, button styles, etc.).</p>
 *
 * <p><strong>Threading:</strong> Construct and show this frame on the Swing EDT
 * (e.g., via {@code SwingUtilities.invokeLater}).</p>
 */
public class MainDashboardUI extends JFrame {

    /**
     * The authenticated user's water meter identifier used to personalize the dashboard.
     */
    private String meterId;

    /**
     * Creates the main dashboard for the given meter ID, builds the layout, and wires actions.
     *
     * <p>The frame is sized to 720x560 pixels, centered on screen, and configured to exit the JVM when closed.</p>
     *
     * @param meterId the water meter ID associated with the signed-in user
     */
    public MainDashboardUI(String meterId) {
        this.meterId = meterId;

        setTitle("Main Dashboard");
        setSize(720, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = WaterTheme.createGradientBackground(new BorderLayout(16, 16));
        setContentPane(root);
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Create header with logout button
        JPanel headerPanel = createHeaderWithLogout();
        root.add(headerPanel, BorderLayout.NORTH);

        // Center stack: user info card + actions row
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        root.add(center, BorderLayout.CENTER);

        // User information based on meter ID
        JPanel infoCard = WaterTheme.createCardPanel("User Information", new BorderLayout(10, 10));
        JPanel infoGrid = new JPanel(new GridLayout(3, 2, 10, 8));
        infoGrid.setOpaque(false);

        // Get user info based on meter ID
        UserInfo userInfo = getUserInfoByMeterId(meterId);

        JLabel nameLabel = new JLabel("Name:");
        JLabel nameValue = new JLabel(userInfo.getName());
        JLabel addressLabel = new JLabel("Address:");
        JLabel addressValue = new JLabel(userInfo.getAddress());
        JLabel meterLabel = new JLabel("Meter ID:");
        JLabel meterValue = new JLabel(meterId);

        nameLabel.setFont(WaterTheme.bodyFont());
        addressLabel.setFont(WaterTheme.bodyFont());
        meterLabel.setFont(WaterTheme.bodyFont());
        nameValue.setFont(WaterTheme.bodyFont());
        addressValue.setFont(WaterTheme.bodyFont());
        meterValue.setFont(WaterTheme.bodyFont());
        nameValue.setForeground(WaterTheme.PRIMARY_BLUE);
        addressValue.setForeground(WaterTheme.PRIMARY_BLUE);
        meterValue.setForeground(WaterTheme.PRIMARY_BLUE);

        infoGrid.add(nameLabel);
        infoGrid.add(nameValue);
        infoGrid.add(addressLabel);
        infoGrid.add(addressValue);
        infoGrid.add(meterLabel);
        infoGrid.add(meterValue);
        infoCard.add(infoGrid, BorderLayout.CENTER);

        // Actions
        JPanel actionsCard = WaterTheme.createCardPanel("Quick Actions", new BorderLayout(10, 10));
        JPanel actionsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 6));
        actionsRow.setOpaque(false);

        JButton recordUsageBtn = new JButton("Record Daily Water Usage");
        JButton generateReportBtn = new JButton("Generate Usage Report");
        JButton alertBtn = new JButton("Abnormal Usage Alerts");

        Dimension btnSize = new Dimension(200, 46);
        recordUsageBtn.setPreferredSize(btnSize);
        generateReportBtn.setPreferredSize(btnSize);
        alertBtn.setPreferredSize(btnSize);

        // Apply theme styling
        WaterTheme.stylePrimaryButton(recordUsageBtn);
        WaterTheme.stylePrimaryButton(generateReportBtn);
        WaterTheme.stylePrimaryButton(alertBtn);
        WaterTheme.installButtonHoverEffect(recordUsageBtn);
        WaterTheme.installButtonHoverEffect(generateReportBtn);
        WaterTheme.installButtonHoverEffect(alertBtn);

        // Ensure readable text (high contrast)
        recordUsageBtn.setForeground(Color.WHITE);
        generateReportBtn.setForeground(Color.WHITE);
        alertBtn.setForeground(Color.WHITE);

        actionsRow.add(recordUsageBtn);
        actionsRow.add(generateReportBtn);
        actionsRow.add(alertBtn);
        actionsCard.add(actionsRow, BorderLayout.NORTH);

        // Place actions above, user info below
        center.add(actionsCard);
        center.add(Box.createVerticalStrut(12));
        center.add(infoCard);

        // Action listeners
        recordUsageBtn.addActionListener(e -> new RecordDailyUsageUI(meterId).setVisible(true));
        generateReportBtn.addActionListener(e -> new GenerateReportUI().setVisible(true));
        alertBtn.addActionListener(e -> new AbnormalUsageAlertUI().setVisible(true));
    }

    /**
     * Creates the header panel consisting of a wave-styled title area and a Logout button.
     *
     * <p>The Logout button prompts the user for confirmation, disposes this dashboard on confirmation,
     * and reopens the {@code WaterMeterIDUI} sign-in screen.</p>
     *
     * @return a transparent {@link JPanel} configured for the window's header
     */
    private JPanel createHeaderWithLogout() {
        JPanel headerContainer = new JPanel(new BorderLayout());
        headerContainer.setOpaque(false);

        // Create the wave header as before
        JComponent waveHeader = WaterTheme.createWaveHeader("Dashboard", "Water Meter ID: " + meterId);

        // Create logout button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setPreferredSize(new Dimension(100, 35));
        WaterTheme.styleSecondaryButton(logoutBtn);
        WaterTheme.installButtonHoverEffect(logoutBtn);

        // Set logout button colors for better visibility on the wave header
        logoutBtn.setBackground(new Color(255, 255, 255, 200));
        logoutBtn.setForeground(WaterTheme.PRIMARY_BLUE);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 12));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add logout functionality
        logoutBtn.addActionListener(e -> {
            // Show confirmation dialog
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                // Close current dashboard
                this.dispose();

                // Open water meter access screen
                SwingUtilities.invokeLater(() -> {
                    new WaterMeterIDUI().setVisible(true);
                });
            }
        });

        // Create a panel to hold the logout button with proper positioning
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        logoutPanel.setOpaque(false);
        logoutPanel.add(logoutBtn);

        // Add components to header container
        headerContainer.add(waveHeader, BorderLayout.CENTER);
        headerContainer.add(logoutPanel, BorderLayout.EAST);

        return headerContainer;
    }

    /**
     * Simple immutable value object holding a user's display name and address.
     */
    private static class UserInfo {
        private String name;
        private String address;

        /**
         * Constructs a user info record.
         *
         * @param name    user's display name
         * @param address user's address string
         */
        public UserInfo(String name, String address) {
            this.name = name;
            this.address = address;
        }

        /** @return the user's display name */
        public String getName() { return name; }

        /** @return the user's address */
        public String getAddress() { return address; }
    }

    /**
     * Resolves user display information from a known set of demo meter IDs.
     *
     * <p>Supported IDs: {@code WM001}, {@code WM002}, {@code WM003}. Any other value
     * yields a default "Unknown User" entry.</p>
     *
     * @param meterId the water meter ID to look up (case-insensitive)
     * @return a {@link UserInfo} instance with the name and address for the meter ID
     */
    private UserInfo getUserInfoByMeterId(String meterId) {
        switch (meterId.toUpperCase()) {
            case "WM001":
                return new UserInfo("Alex Johnson", "123 Bluewave Ave, Water City");
            case "WM002":
                return new UserInfo("Sarah Chen", "456 Ocean Drive, Aqua Town");
            case "WM003":
                return new UserInfo("Michael Rodriguez", "789 River Road, H2O Heights");
            default:
                return new UserInfo("Unknown User", "Address Not Found");
        }
    }
}
