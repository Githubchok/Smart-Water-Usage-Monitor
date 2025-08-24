import java.time.LocalDate;

/**
 * Represents an alert for abnormal usage patterns (e.g., high consumption, leak).
 *
 * <p><strong>Behavior:</strong>
 * Each instance is assigned a monotonically increasing {@code alertID} using a
 * class-level counter. The alert's date is captured at construction time using
 * {@link LocalDate#now()} in the system default time zone.</p>
 *
 * <p><strong>Thread-safety:</strong>
 * ID assignment via the static {@code nextAlertID} counter is not synchronized.
 * If instances are created concurrently across threads, consider adding external
 * synchronization or migrating to an atomic counter to avoid ID collisions.</p>
 *
 * <p><strong>I/O:</strong>
 * The {@link #trigger()} method prints a human-readable representation of the alert
 * to standard output. For GUI or logging frameworks, prefer {@link #getAlertDetails()}.</p>
 *
 * @author YourName
 * @version 1.0
 * @since 1.0
 */
public class Alert {
    private int alertID;
    private String meterID;
    private String alertType;
    private String alertMessage;
    private LocalDate alertDate;

    /**
     * Class-wide counter used to assign unique, incrementing alert IDs.
     * <p><em>Note:</em> This field is not thread-safe as-is.</p>
     */
    private static int nextAlertID = 1;

    /**
     * Creates a new alert with an auto-assigned ID and today's date.
     *
     * @param meterID      the meter ID that triggered the alert (e.g., device/installation ID)
     * @param alertType    the type/category of alert (e.g., {@code HIGH_USAGE}, {@code LEAK})
     * @param alertMessage a detailed, human-readable description of the condition
     */
    public Alert(String meterID, String alertType, String alertMessage) {
        this.alertID = nextAlertID++;
        this.meterID = meterID;
        this.alertType = alertType;
        this.alertMessage = alertMessage;
        this.alertDate = LocalDate.now();
    }

    /**
     * Emits the alert to standard output in a multi-line format suitable for consoles.
     * <p>Intended for simple demos or CLI tools. Applications with UI frameworks or
     * logging requirements should consider consuming {@link #getAlertDetails()} instead.</p>
     */
    public void trigger() {
        System.out.println("ALERT TRIGGERED!");
        System.out.println("Alert ID: " + alertID);
        System.out.println("Meter: " + meterID);
        System.out.println("Type: " + alertType);
        System.out.println("Message: " + alertMessage);
        System.out.println("Date: " + alertDate);
        System.out.println("-".repeat(40));
    }

    /**
     * Returns a single-line, formatted description of this alert.
     *
     * @return a string in the form
     * {@code Alert[<id>]: <type> - <message> (<meterId>) on <date>}
     */
    public String getAlertDetails() {
        return String.format("Alert[%d]: %s - %s (%s) on %s",
                alertID, alertType, alertMessage, meterID, alertDate);
    }

    // Getters

    /**
     * Gets the unique identifier assigned to this alert.
     * @return the alert ID
     */
    public int getAlertID() { return alertID; }

    /**
     * Gets the ID of the meter that triggered the alert.
     * @return the meter ID (may be {@code null} if not provided)
     */
    public String getMeterID() { return meterID; }

    /**
     * Gets the alert type/category.
     * @return the alert type string (e.g., {@code HIGH_USAGE}, {@code LEAK})
     */
    public String getAlertType() { return alertType; }

    /**
     * Gets the detailed message explaining the alert.
     * @return the alert message
     */
    public String getAlertMessage() { return alertMessage; }

    /**
     * Gets the date on which the alert was created.
     * @return the alert date (local system date)
     */
    public LocalDate getAlertDate() { return alertDate; }
}
