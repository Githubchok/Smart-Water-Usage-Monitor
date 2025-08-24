/**
 * Abstract base class for all types of reports in the water management system.
 *
 * <p><strong>Responsibilities:</strong> Captures common metadata (report ID, meter ID,
 * generation date) and defines the contract for generating full report content and
 * a concise summary.</p>
 *
 * <p><strong>ID generation:</strong> {@code reportID} is created using the prefix
 * {@code "RPT"} plus {@link System#currentTimeMillis()}. If strict uniqueness is required
 * across distributed systems or concurrent creators, consider overriding or extending the
 * ID strategy.</p>
 *
 * @author YourName
 * @version 1.0
 */
import java.time.LocalDate;
public abstract class Report {

    /**
     * Opaque identifier for this report instance, generated at construction time.
     * <p>Default format: {@code "RPT"} + epoch milliseconds.</p>
     */
    protected String reportID;

    /**
     * The meter identifier this report pertains to.
     */
    protected String meterID;

    /**
     * The local calendar date on which this report instance was created.
     * <p>Initialized with {@link LocalDate#now()} using the system default clock.</p>
     */
    protected LocalDate generatedDate;

    /**
     * Creates a new report bound to the given meter ID and stamps metadata fields.
     *
     * @param meterID the meter ID for this report (must not be {@code null} for most implementations)
     */
    public Report(String meterID) {
        this.meterID = meterID;
        this.generatedDate = LocalDate.now();
        this.reportID = "RPT" + System.currentTimeMillis();
    }

    /**
     * Generates the full report content.
     * <p>Implementations should return a complete, user-readable representation (e.g., a
     * multi-line textual report). Implementations are free to pull data from services,
     * caches, or in-memory state as appropriate.</p>
     *
     * @return the generated report as a string
     */
    public abstract String generateReport();

    /**
     * Provides a concise, single-paragraph (or single-line) summary of the report.
     * <p>Suitable for dashboards, notifications, and list views.</p>
     *
     * @return report summary
     */
    public abstract String getSummary();

    // Getters

    /**
     * Returns this report's identifier.
     *
     * @return the report ID string
     */
    public String getReportID() { return reportID; }

    /**
     * Returns the meter ID that this report references.
     *
     * @return the meter ID
     */
    public String getMeterID() { return meterID; }

    /**
     * Returns the local date when this report instance was created.
     *
     * @return the generated date
     */
    public LocalDate getGeneratedDate() { return generatedDate; }
}
