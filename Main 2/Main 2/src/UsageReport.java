import java.util.List;
import java.time.format.DateTimeFormatter;

/**
 * Concrete implementation of {@link Report} that renders a detailed water usage report
 * for a given meter over a specified period.
 *
 * <p><strong>Content:</strong> The generated report includes metadata (report ID, meter ID,
 * period, generation date), a tabular list of usage records, and summary statistics
 * (total and average daily usage). Dates are formatted using
 * {@link DateTimeFormatter#ISO_LOCAL_DATE}.</p>
 *
 * <p><strong>Inputs:</strong> This class consumes an externally supplied list of
 * {@link UsageRecord} instances and does not fetch data itself.</p>
 *
 * @author YourName
 * @version 1.0
 */
public class UsageReport extends Report {

    /**
     * The usage records that this report summarizes.
     */
    private List<UsageRecord> usageRecords;

    /**
     * Human-readable description of the covered period (e.g., {@code "2024-01-01 to 2024-01-31"}).
     */
    private String period;

    /**
     * Creates a new usage report for the given meter and period.
     *
     * @param meterID      the meter ID
     * @param usageRecords list of usage records (may be empty but not {@code null})
     * @param period       human-readable report period label
     */
    public UsageReport(String meterID, List<UsageRecord> usageRecords, String period) {
        super(meterID);
        this.usageRecords = usageRecords;
        this.period = period;
    }

    /**
     * Generates the full textual report, including metadata, a records table, and summary statistics.
     *
     * <p>If {@link #usageRecords} is empty, the report indicates that no data was found for the period.</p>
     *
     * @return a formatted, multi-section report string
     */
    @Override
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=====================================\n");
        report.append("        WATER USAGE REPORT\n");
        report.append("=====================================\n\n");

        report.append("Report ID: ").append(reportID).append("\n");
        report.append("Meter ID: ").append(meterID).append("\n");
        report.append("Period: ").append(period).append("\n");
        report.append("Generated: ").append(generatedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)).append("\n\n");

        if (usageRecords.isEmpty()) {
            report.append("No usage records found for this period.\n");
        } else {
            double totalUsage = 0;
            report.append("USAGE RECORDS:\n");
            report.append("Date          Usage (Liters)\n");
            report.append("-------------------------\n");

            for (UsageRecord record : usageRecords) {
                report.append(String.format("%-12s  %8.2f\n",
                        record.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        record.getUsageAmount()));
                totalUsage += record.getUsageAmount();
            }

            report.append("\n");
            report.append(String.format("Total Usage: %.2f liters\n", totalUsage));
            report.append(String.format("Average Daily: %.2f liters\n", totalUsage / usageRecords.size()));
        }

        report.append("=====================================");
        return report.toString();
    }

    /**
     * Provides a concise one-line summary suitable for dashboards or lists.
     *
     * @return a summary string containing the period, total liters, and record count,
     *         or a message indicating no data is available
     */
    @Override
    public String getSummary() {
        if (usageRecords.isEmpty()) {
            return "No usage data available for " + period;
        }

        double total = usageRecords.stream().mapToDouble(UsageRecord::getUsageAmount).sum();
        return String.format("Period: %s | Total: %.2f liters | Records: %d",
                period, total, usageRecords.size());
    }
}
