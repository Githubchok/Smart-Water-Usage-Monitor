import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A rounded-corner text field supporting placeholder text and inline error display.
 *
 * <p><strong>Features:</strong></p>
 * <ul>
 *   <li>Customizable corner radius via constructor parameter {@code arc}.</li>
 *   <li>Placeholder text shown when empty and unfocused.</li>
 *   <li>Error mode that clears current text, shows a red inline message, and renders a red border.</li>
 * </ul>
 *
 * <p><strong>Painting:</strong> Overrides {@link #paintComponent(Graphics)} and
 * {@link #paintBorder(Graphics)} to render rounded background and border.</p>
 *
 * <p><strong>Threading:</strong> Create and interact with instances on the Swing EDT.</p>
 *
 * @since 1.0
 */
class RoundedTextField extends JTextField {
    /** Corner arc radius for rounded rendering. */
    private int arc;
    /** Placeholder text shown when field is empty and unfocused. */
    private String placeholder = "";
    /** Whether the field is currently showing an error. */
    private boolean errorMode = false;
    /** Error message to paint when in error mode. */
    private String errorText = "";
    /** Current border color. */
    private Color borderColor = Color.LIGHT_GRAY;

    /**
     * Constructs a rounded text field with the given corner radius.
     *
     * @param arc corner arc diameter used for rounded painting
     */
    public RoundedTextField(int arc) {
        super("");
        this.arc = arc;
        setOpaque(false);
        setBorder(null);
    }

    /**
     * Sets the placeholder text that appears when the field is empty and unfocused.
     *
     * @param text placeholder string (may be {@code null} to suppress)
     */
    public void setPlaceholder(String text) {
        this.placeholder = text;
        repaint();
    }

    /**
     * Puts the field into error mode: clears text, sets colors to red, and shows {@code errorMsg}.
     *
     * @param errorMsg message to display as inline error text
     */
    public void setError(String errorMsg) {
        this.errorMode = true;
        this.errorText = errorMsg;
        setForeground(Color.RED);
        setBorderColor(Color.RED);
        setText("");
        repaint();
    }

    /**
     * Clears error mode and restores default colors.
     */
    public void clearError() {
        this.errorMode = false;
        this.errorText = "";
        setForeground(Color.BLACK);
        setBorderColor(Color.LIGHT_GRAY);
        repaint();
    }

    /**
     * Indicates whether the field is currently in error mode.
     *
     * @return {@code true} if in error mode; {@code false} otherwise
     */
    public boolean isErrorMode() {
        return errorMode;
    }

    /**
     * Sets the border color used when painting the rounded border.
     *
     * @param c the new border color
     */
    public void setBorderColor(Color c) {
        this.borderColor = c;
    }

    /**
     * Paints the rounded background and placeholder/error text, then delegates to super.
     *
     * @param g the graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
        super.paintComponent(g);

        // 错误时优先显示红色普通字体
        if (errorMode && getText().isEmpty() && errorText != null) {
            g2.setColor(Color.RED);
            Font prevFont = getFont();
            g2.setFont(prevFont.deriveFont(Font.PLAIN)); // 普通字体
            Insets insets = getInsets();
            g2.drawString(errorText, insets.left + 8, getHeight() / 2 + prevFont.getSize() / 2 - 2);
        } else if (!errorMode && getText().isEmpty() && placeholder != null && !isFocusOwner()) {
            // 普通placeholder，灰色
            g2.setColor(new Color(160, 160, 160));
            Font prevFont = getFont();
            g2.setFont(prevFont.deriveFont(Font.PLAIN));
            Insets insets = getInsets();
            g2.drawString(placeholder, insets.left + 8, getHeight() / 2 + prevFont.getSize() / 2 - 2);
        }
        g2.dispose();
    }

    /**
     * Paints the rounded border using the current {@link #borderColor}.
     *
     * @param g the graphics context
     */
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
        g2.dispose();
    }
}

/**
 * A rounded-corner button with custom-painted background and border.
 *
 * <p>Disables default L&amp;F content/border painting to allow custom drawing.</p>
 *
 * @since 1.0
 */
class RoundedButton extends JButton {
    /** Corner arc radius for rounded rendering. */
    private int arc;

    /**
     * Constructs a rounded button with the given label and corner radius.
     *
     * @param text button text
     * @param arc  corner arc diameter used for rounded painting
     */
    public RoundedButton(String text, int arc) {
        super(text);
        this.arc = arc;
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
    }

    /**
     * Paints the rounded background and delegates to the superclass to render the label.
     *
     * @param g the graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
        super.paintComponent(g);
        g2.dispose();
    }

    /**
     * Paints a rounded border that uses a darker shade of the background color.
     *
     * @param g the graphics context
     */
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getBackground().darker());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
        g2.dispose();
    }
}

/**
 * Sign-in window for the WaterSpace application.
 *
 * <p>Prompts for a <em>Water Meter ID</em> using a glass-style card over a full-bleed
 * background image. Accepts known demo IDs and launches {@link MainDashboardUI}
 * on success; otherwise shows an inline error in the text field.</p>
 *
 * <p><strong>Resources:</strong> Expects {@code /logoWhiteWords.jpg} and
 * {@code /waterIDaccess.jpg} to be present on the classpath.</p>
 *
 * <p><strong>Threading:</strong> Construct and show instances on the Swing EDT
 * (see {@link #main(String[])}).</p>
 *
 * @since 1.0
 */
public class WaterMeterIDUI extends JFrame {
    /** Glass-like foreground panel containing logo, title, input and button. */
    private JPanel glassPanel;
    /** Input field for the water meter ID with placeholder and error states. */
    private RoundedTextField meterIdField;
    /** Header/branding labels. */
    private JLabel logoLabel, titleLabel, meterIdLabel;
    /** Primary action button to validate ID and proceed. */
    private RoundedButton enterBtn;

    /**
     * Builds the sign-in UI, wires validation logic, and displays the frame.
     * <p>The frame is initialized to 1100x700, centered, and uses
     * {@link JFrame#EXIT_ON_CLOSE} as its close operation.</p>
     */
    public WaterMeterIDUI() {
        setTitle("WaterSpace - Water Meter Access");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        BackgroundPanel bgPanel = new BackgroundPanel();
        setContentPane(bgPanel);
        bgPanel.setLayout(null);

        glassPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(161, 161, 161, 119));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        glassPanel.setOpaque(false);
        glassPanel.setLayout(null);

        // LOGO
        int logoWidth = 126, logoHeight = 170;
        ImageIcon icon = new ImageIcon(getClass().getResource("/logoWhiteWords.jpg"));
        Image scaledLogo = icon.getImage().getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH);
        logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setBounds(145, 25, logoWidth, logoHeight);

        titleLabel = new JLabel("WELCOME TO WATERSPACE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(255, 255, 255));
        titleLabel.setBounds(48, 130, 320, 32);

        // MeterID
        meterIdLabel = new JLabel("Water Meter ID");
        meterIdLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        meterIdLabel.setForeground(new Color(255, 255, 255));
        meterIdLabel.setBounds(48, 170, 320, 22);

        // placeholder
        meterIdField = new RoundedTextField(22);
        meterIdField.setFont(new Font("Arial", Font.PLAIN, 15));
        meterIdField.setPlaceholder("e.g.WM001");
        meterIdField.setBounds(48, 200, 320, 38);
        meterIdField.setHorizontalAlignment(JTextField.CENTER);
        meterIdField.setBackground(Color.WHITE);

        enterBtn = new RoundedButton("Enter", 22);
        enterBtn.setFont(new Font("Arial", Font.BOLD, 18));
        enterBtn.setBackground(new Color(14, 56, 140));
        enterBtn.setForeground(Color.WHITE);
        enterBtn.setBounds(48, 260, 320, 42);
        enterBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        glassPanel.add(logoLabel);
        glassPanel.add(titleLabel);
        glassPanel.add(meterIdLabel);
        glassPanel.add(meterIdField);
        glassPanel.add(enterBtn);

        resizeGlassPanel();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeGlassPanel();
            }
        });
        bgPanel.add(glassPanel);

        meterIdField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (meterIdField.isErrorMode()) {
                    meterIdField.clearError();
                }
                meterIdField.setBackground(Color.WHITE);
                meterIdField.setForeground(Color.BLACK);
            }
        });

        enterBtn.addActionListener(e -> {
            String id = meterIdField.getText().trim();
            if (isValidMeterId(id)) {
                meterIdField.clearError();
                new MainDashboardUI(id).setVisible(true);
                dispose();
            } else {
                meterIdField.setError("Invalid Water meter ID. Please Try Again");
            }
        });

        setVisible(true);
    }

    /**
     * Recomputes and applies layout bounds for the glass card and its child components
     * based on the current frame size.
     */
    private void resizeGlassPanel() {
        int frameW = getContentPane().getWidth();
        int frameH = getContentPane().getHeight();
        int cardW = Math.max((int)(frameW * 0.36), 430);
        int cardH = Math.max((int)(frameH * 0.65), 380);
        int cardX = (frameW - cardW) / 2;
        int cardY = (frameH - cardH) / 2;
        glassPanel.setBounds(cardX, cardY, cardW, cardH);

        int baseX = (cardW - 320) / 2;
        logoLabel.setBounds(baseX + 80, 0, 170, 200);
        titleLabel.setBounds(baseX + 20, 190, 320, 32);
        meterIdLabel.setBounds(baseX + 13, 240, 320, 22);
        meterIdField.setBounds(baseX + 10, 270, 320, 38);
        enterBtn.setBounds(baseX + 10, 328, 320, 42);

        glassPanel.revalidate();
        glassPanel.repaint();
    }

    /**
     * Validates the provided meter ID against a small built-in whitelist.
     * Comparison is case-insensitive.
     *
     * @param id candidate meter ID
     * @return {@code true} if {@code id} matches one of the known IDs; {@code false} otherwise
     */
    private boolean isValidMeterId(String id) {
        String[] validIds = {"WM001", "WM002", "WM003"};
        for (String validId : validIds) {
            if (validId.equalsIgnoreCase(id)) return true;
        }
        return false;
    }

    /**
     * Background panel that draws a scaled image to fill the frame.
     */
    class BackgroundPanel extends JPanel {
        /** Background image loaded from the classpath. */
        Image bg = new ImageIcon(getClass().getResource("/waterIDaccess.jpg")).getImage();

        /**
         * Paints the background image stretched to the current size.
         *
         * @param g the graphics context
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Launches the WaterSpace sign-in window on the Swing Event Dispatch Thread (EDT).
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(WaterMeterIDUI::new);
    }
}
