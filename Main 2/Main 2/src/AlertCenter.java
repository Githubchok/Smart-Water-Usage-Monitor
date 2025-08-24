import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Centralized utility for recording, formatting, and clearing abnormal usage alerts
 * (e.g., water consumption) for smart meters.
 *
 * <p><strong>Thread-safety:</strong> The internal alert list is a
 * {@link Collections#synchronizedList(List) synchronized list}. Individual add/clear
 * operations are thread-safe. Methods that iterate over the list (e.g., {@link #formatAlerts()})
 * obtain a snapshot via {@link #getAlerts()} to avoid holding the list lock while formatting.</p>
 *
 * <p>This class is {@code final} and cannot be instantiated; use its static methods.</p>
 */
public final class AlertCenter {

	/**
	 * Thread-safe backing store for all recorded alerts, in insertion order.
	 * <p>Use {@link #getAlerts()} to obtain a defensive copy and {@link #clear()} to reset.</p>
	 */
	private static final List<Alert> ALERTS = Collections.synchronizedList(new ArrayList<>());

	/**
	 * Formatter for alert timestamps using the pattern {@code yyyy-MM-dd HH:mm}.
	 * <p>Timestamps are generated with {@link LocalDateTime#now()} in the system default time zone.</p>
	 */
	private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	/**
	 * Prevents instantiation. Use static methods only.
	 */
	private AlertCenter() {}

	/**
	 * Adds a {@code HIGH_USAGE} alert for the given meter.
	 *
	 * <p>The generated message has the form:
	 * {@code "Today's usage <usage> L exceeds threshold <threshold> L at <timestamp>"}.</p>
	 *
	 * @param meterId   the unique meter identifier; if {@code null}, the value {@code "UNKNOWN"} is used
	 * @param usage     the measured usage for today, in liters
	 * @param threshold the configured upper threshold, in liters
	 */
	public static void addHighUsage(String meterId, double usage, double threshold) {
		String msg = String.format("Today's usage %.1f L exceeds threshold %.1f L at %s", usage, threshold, TS.format(LocalDateTime.now()));
		ALERTS.add(new Alert(meterId == null ? "UNKNOWN" : meterId, "HIGH_USAGE", msg));
	}

	/**
	 * Adds a {@code LOW_USAGE} alert for the given meter.
	 *
	 * <p>The generated message has the form:
	 * {@code "Today's usage <usage> L is below threshold <threshold> L at <timestamp>"}.</p>
	 *
	 * @param meterId   the unique meter identifier; if {@code null}, the value {@code "UNKNOWN"} is used
	 * @param usage     the measured usage for today, in liters
	 * @param threshold the configured lower threshold, in liters
	 */
	public static void addLowUsage(String meterId, double usage, double threshold) {
		String msg = String.format("Today's usage %.1f L is below threshold %.1f L at %s", usage, threshold, TS.format(LocalDateTime.now()));
		ALERTS.add(new Alert(meterId == null ? "UNKNOWN" : meterId, "LOW_USAGE", msg));
	}

	/**
	 * Returns a snapshot of the currently recorded alerts.
	 *
	 * <p>The returned list is a defensive copy; modifying it will not affect the internal store.</p>
	 *
	 * @return a new {@link ArrayList} containing the current alerts in insertion order
	 */
	public static List<Alert> getAlerts() {
		return new ArrayList<>(ALERTS);
	}

	/**
	 * Formats all recorded alerts into a human-readable string, one alert per line.
	 *
	 * <p>If no alerts exist, returns {@code "No abnormal usage alerts currently."}.</p>
	 *
	 * @return a newline-delimited string of alert details or a message indicating there are none
	 */
	public static String formatAlerts() {
		if (ALERTS.isEmpty()) {
			return "No abnormal usage alerts currently.";
		}
		StringBuilder sb = new StringBuilder();
		for (Alert a : getAlerts()) {
			sb.append(a.getAlertDetails()).append("\n");
		}
		return sb.toString();
	}

	/**
	 * Removes all recorded alerts.
	 *
	 * <p>This operation affects only the in-memory store represented by {@link #ALERTS}.</p>
	 */
	public static void clear() {
		ALERTS.clear();
	}
}