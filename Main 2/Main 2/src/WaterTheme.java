import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Theme utilities for building a cohesive "water" look & feel across Swing windows.
 *
 * <p>This final class provides:</p>
 * <ul>
 *   <li>Brand colors and fonts,</li>
 *   <li>Factory methods for gradient backgrounds, card panels, and wave headers,</li>
 *   <li>Styling helpers for buttons, text fields, and scroll panes, and</li>
 *   <li>A simple hover effect installer for buttons.</li>
 * </ul>
 *
 * <p><strong>Painting:</strong> Custom components created here override
 * {@code paintComponent} (and sometimes {@code paintBorder}) to draw gradients,
 * waves, bubbles, and rounded outlines with antialiasing.</p>
 *
 * <p><strong>Threading:</strong> As with all Swing UI code, construct and use
 * the returned components on the Event Dispatch Thread (EDT).</p>
 *
 * @since 1.0
 */
public final class WaterTheme {

	/** Primary brand blue used for key accents and primary buttons. */
	public static final Color PRIMARY_BLUE = new Color(14, 56, 140);
	/** Lighter accent blue used in gradients and highlights. */
	public static final Color ACCENT_BLUE = new Color(0, 153, 255);
	/** Top color of the background gradient (light aqua). */
	public static final Color BG_TOP = new Color(199, 235, 255);     // light aqua
	/** Middle tint of the background gradient (sky blue). */
	public static final Color BG_MID = new Color(141, 207, 255);     // sky blue
	/** Bottom color of the background gradient (near-white blue). */
	public static final Color BG_BOTTOM = new Color(230, 246, 255);  // near white blue

	/**
	 * Prevents instantiation. Use static members and factory methods.
	 */
	private WaterTheme() {}

	/**
	 * Returns the default title font used by headers and card titles.
	 *
	 * @return a bold {@code Arial} font sized 18pt
	 */
	public static Font titleFont() {
		return new Font("Arial", Font.BOLD, 18);
	}

	/**
	 * Returns the default body font used by labels and inputs.
	 *
	 * @return a plain {@code Arial} font sized 14pt
	 */
	public static Font bodyFont() {
		return new Font("Arial", Font.PLAIN, 14);
	}

	/**
	 * Creates a panel with a vertically blended ocean-like gradient, layered soft waves,
	 * and subtle "bubbles". The provided layout manager is applied to the panel.
	 *
	 * <p>The panel paints with antialiasing and multiple translucent overlays to achieve depth.</p>
	 *
	 * @param layout layout manager to apply to the returned panel
	 * @return a non-opaque {@link JPanel} that repaints a themed gradient background
	 */
	public static JPanel createGradientBackground(LayoutManager layout) {
		return new JPanel(layout) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				int w = getWidth();
				int h = getHeight();
				// Vertical ocean gradient
				GradientPaint vertical = new GradientPaint(0, 0, BG_TOP, 0, h, BG_BOTTOM);
				g2.setPaint(vertical);
				g2.fillRect(0, 0, w, h);

				// Soft mid overlay for depth
				GradientPaint mid = new GradientPaint(0, h * 0 / 10f, new Color(BG_MID.getRed(), BG_MID.getGreen(), BG_MID.getBlue(), 120),
						0, h * 6 / 10f, new Color(BG_BOTTOM.getRed(), BG_BOTTOM.getGreen(), BG_BOTTOM.getBlue(), 0));
				g2.setPaint(mid);
				g2.fillRect(0, 0, w, h);

				// Layered translucent waves near the bottom
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
				g2.setColor(Color.WHITE);
				Path2D wave1 = new Path2D.Double();
				wave1.moveTo(0, h * 0.82);
				wave1.curveTo(w * 0.20, h * 0.78, w * 0.30, h * 0.90, w * 0.52, h * 0.86);
				wave1.curveTo(w * 0.72, h * 0.81, w * 0.88, h * 0.95, w, h * 0.90);
				wave1.lineTo(w, h);
				wave1.lineTo(0, h);
				wave1.closePath();
				g2.fill(wave1);

				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.18f));
				Path2D wave2 = new Path2D.Double();
				wave2.moveTo(0, h * 0.88);
				wave2.curveTo(w * 0.25, h * 0.84, w * 0.35, h * 0.97, w * 0.60, h * 0.93);
				wave2.curveTo(w * 0.80, h * 0.90, w * 0.95, h * 0.99, w, h * 0.97);
				wave2.lineTo(w, h);
				wave2.lineTo(0, h);
				wave2.closePath();
				g2.fill(wave2);

				// Subtle bubbles
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.10f));
				g2.setColor(Color.WHITE);
				int[][] bubbles = new int[][]{
						{(int)(w * 0.12), (int)(h * 0.62), 14},
						{(int)(w * 0.18), (int)(h * 0.58), 8},
						{(int)(w * 0.70), (int)(h * 0.55), 10},
						{(int)(w * 0.78), (int)(h * 0.60), 16},
						{(int)(w * 0.32), (int)(h * 0.50), 12}
				};
				for (int[] b : bubbles) {
					int x = Math.max(4, Math.min(w - b[2] - 4, b[0]));
					int y = Math.max(4, Math.min(h - b[2] - 4, b[1]));
					g2.fillOval(x, y, b[2], b[2]);
				}
				g2.dispose();
			}
		};
	}

	/**
	 * Creates a semi-transparent rounded "card" panel with an optional title header and the
	 * specified layout manager. The card renders a light outline and padding suitable for content.
	 *
	 * @param title  optional title to display in the card's header (left-aligned)
	 * @param layout layout manager for the card's content area
	 * @return a non-opaque {@link JPanel} styled as an information card
	 */
	public static JPanel createCardPanel(String title, LayoutManager layout) {
		JPanel panel = new JPanel(layout) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				int arc = 18;
				g2.setColor(new Color(255, 255, 255, 230));
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
				g2.setColor(new Color(180, 200, 230));
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
				g2.dispose();
			}
		};
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(12, 14, 14, 14));
		if (title != null && !title.isEmpty()) {
			JLabel titleLabel = new JLabel(title);
			titleLabel.setFont(titleFont());
			titleLabel.setForeground(PRIMARY_BLUE);
			JPanel header = new JPanel(new BorderLayout());
			header.setOpaque(false);
			header.add(titleLabel, BorderLayout.WEST);
			header.setBorder(new EmptyBorder(0, 2, 8, 2));
			panel.add(header, BorderLayout.NORTH);
		}
		return panel;
	}

	/**
	 * Creates a horizontal header component with a vertical gradient (accent → primary),
	 * a translucent wave overlay, and stacked title/subtitle labels.
	 *
	 * @param title    main heading text (left-aligned; may be {@code null} or empty)
	 * @param subtitle smaller descriptive text beneath the title (optional)
	 * @return a non-opaque {@link JComponent} sized to header height
	 */
	public static JComponent createWaveHeader(String title, String subtitle) {
		JPanel header = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				int w = getWidth();
				int h = getHeight();
				GradientPaint gp = new GradientPaint(0, 0, ACCENT_BLUE, 0, h, PRIMARY_BLUE);
				g2.setPaint(gp);
				g2.fillRect(0, 0, w, h);

				// top wave overlay
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
				g2.setColor(Color.WHITE);
				Path2D wave = new Path2D.Double();
				wave.moveTo(0, h * 0.65);
				wave.curveTo(w * 0.25, h * 0.55, w * 0.35, h * 0.95, w * 0.6, h * 0.85);
				wave.curveTo(w * 0.8, h * 0.78, w * 0.95, h * 0.98, w, h * 0.9);
				wave.lineTo(w, 0);
				wave.lineTo(0, 0);
				wave.closePath();
				g2.fill(wave);
				g2.dispose();
			}
		};
		header.setPreferredSize(new Dimension(10, 90));
		header.setBorder(new EmptyBorder(12, 16, 12, 16));

		JLabel titleLabel = new JLabel(title == null ? "" : title);
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

		JLabel subLabel = new JLabel(subtitle == null ? "" : subtitle);
		subLabel.setForeground(new Color(240, 248, 255));
		subLabel.setFont(new Font("Arial", Font.PLAIN, 13));

		JPanel text = new JPanel();
		text.setOpaque(false);
		text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
		text.add(titleLabel);
		text.add(Box.createVerticalStrut(4));
		text.add(subLabel);

		header.add(text, BorderLayout.WEST);
		return header;
	}

	/**
	 * Applies "primary" styling to a button: solid brand background, white text, rounded border,
	 * hand cursor, and no focus painting (Basic L&amp;F).
	 *
	 * @param button the button to style (non-null)
	 */
	public static void stylePrimaryButton(AbstractButton button) {
		button.setUI(new BasicButtonUI());
		button.setOpaque(true);
		button.setContentAreaFilled(true);
		button.setBackground(PRIMARY_BLUE);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setBorder(new LineBorder(PRIMARY_BLUE.darker(), 1, true));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	/**
	 * Applies "secondary" styling to a button: light background, dark blue text, subtle rounded border,
	 * hand cursor, and no focus painting (Basic L&amp;F).
	 *
	 * @param button the button to style (non-null)
	 */
	public static void styleSecondaryButton(AbstractButton button) {
		button.setUI(new BasicButtonUI());
		button.setOpaque(true);
		button.setContentAreaFilled(true);
		button.setBackground(new Color(235, 242, 250));
		button.setForeground(PRIMARY_BLUE.darker());
		button.setFocusPainted(false);
		button.setFont(new Font("Arial", Font.PLAIN, 14));
		button.setBorder(new LineBorder(new Color(190, 210, 235), 1, true));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	/**
	 * Installs a simple hover effect that brightens the button's background when the pointer enters,
	 * and restores it on exit.
	 *
	 * @param button the button to decorate
	 */
	public static void installButtonHoverEffect(final AbstractButton button) {
		final Color base = button.getBackground();
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(base.brighter());
			}
			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(base);
			}
		});
	}

	/**
	 * Applies a consistent border and font styling to a text field.
	 *
	 * @param field the {@link JTextField} to style
	 */
	public static void styleTextField(JTextField field) {
		field.setFont(bodyFont());
		field.setBorder(new CompoundBorder(new LineBorder(new Color(190, 210, 235), 1, true), new EmptyBorder(6, 8, 6, 8)));
	}

	/**
	 * Applies theme styling to a scroll pane by setting a white viewport background
	 * and a rounded, light-blue border.
	 *
	 * @param pane the {@link JScrollPane} to style
	 */
	public static void styleScrollPane(JScrollPane pane) {
		pane.getViewport().setBackground(Color.WHITE);
		pane.setBorder(new LineBorder(new Color(190, 210, 235), 1, true));
	}
}
