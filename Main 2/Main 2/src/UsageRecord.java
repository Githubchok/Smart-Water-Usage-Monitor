// UsageRecord.java
import java.time.LocalDate;

/**
 * Represents a water usage record with date and amount information.
 *
 * <p><strong>Semantics:</strong> Each instance captures a single day's (or reading's)
 * usage in liters associated with a specific meter and date. The {@code recordID}
 * is assigned from a class-level counter to provide a simple, increasing identifier.</p>
 *
 * <p><strong>Thread-safety:</strong> The static counter used to assign {@code recordID}
 * ({@link #nextRecordID}) is not synchronized. If records may be created from multiple
 * threads, consider external synchronization or replacing the counter with an
 * {@code AtomicInteger} to avoid duplicate IDs.</p>
 *
 * <p><strong>Immutability:</strong> {@code recordID} and {@code meterID} are set at construction
 * and not exposed for mutation. The {@code date} and {@code usageAmount} fields can be updated
 * via setters.</p>
 *
 * @author YourName
 * @version 1.0
 * @since 1.0
 */
public class UsageRecord {

    /**
     * Monotonically increasing identifier assigned at construction time.
     */
    private int recordID;

    /**
     * Identifier of the water meter this record belongs to.
     */
    private String meterID;

    /**
     * Calendar date for which the usage was measured.
     */
    private LocalDate date;

    /**
     * Amount of water used, measured in liters.
     */
    private double usageAmount;

    /**
     * Class-wide counter used to generate {@link #recordID} values.
     * <p><em>Note:</em> Not thread-safe as-is.</p>
     */
    private static int nextRecordID = 1;

    /**
     * Creates a new usage record with an auto-assigned ID.
     *
     * @param meterID      the water meter ID (must not be {@code null} in typical usage)
     * @param date         the date of usage (e.g., the reading date)
     * @param usageAmount  the amount of water used in liters
     */
    public UsageRecord(String meterID, LocalDate date, double usageAmount) {
        this.recordID = nextRecordID++;
        this.meterID = meterID;
        this.date = date;
        this.usageAmount = usageAmount;
    }

    // Getters and Setters

    /**
     * Returns the unique identifier for this record.
     * @return the record ID
     */
    public int getRecordID() { return recordID; }

    /**
     * Returns the associated water meter ID.
     * @return the meter ID
     */
    public String getMeterID() { return meterID; }

    /**
     * Returns the calendar date for this usage record.
     * @return the usage date
     */
    public LocalDate getDate() { return date; }

    /**
     * Returns the water usage amount in liters.
     * @return usage amount in liters
     */
    public double getUsageAmount() { return usageAmount; }

    /**
     * Updates the usage amount in liters.
     * @param usageAmount new usage amount
     */
    public void setUsageAmount(double usageAmount) { this.usageAmount = usageAmount; }

    /**
     * Updates the date for this record.
     * @param date new usage date
     */
    public void setDate(LocalDate date) { this.date = date; }

    /**
     * Returns a concise, human-readable representation of the record, including
     * ID, meter ID, amount (two decimal places), and date.
     *
     * @return formatted string describing this record
     */
    @Override
    public String toString() {
        return String.format("Record[%d]: %s - %.2f liters on %s",
                recordID, meterID, usageAmount, date);
    }
}
