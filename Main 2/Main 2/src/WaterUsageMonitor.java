import java.util.*;
import java.time.LocalDate;

/**
 * Central monitoring system for water usage management.
 * Coordinates all water meter operations, usage tracking, and alert generation.
 *
 * @author YourName
 * @version 1.0
 */
public class WaterUsageMonitor {
    private List<WaterMeter> meters;
    private List<UsageRecord> usageRecords;
    private List<Alert> alerts;

    /**
     * Creates a new water usage monitoring system.
     */
    public WaterUsageMonitor() {
        this.meters = new ArrayList<>();
        this.usageRecords = new ArrayList<>();
        this.alerts = new ArrayList<>();

        // Initialize with some default meters
        initializeDefaultMeters();
    }

    /**
     * Initializes the system with default water meters.
     */
    private void initializeDefaultMeters() {
        meters.add(new WaterMeter("WM001", "Building A", "John Doe"));
        meters.add(new WaterMeter("WM002", "Building B", "Jane Smith"));
        meters.add(new WaterMeter("WM003", "Building C", "Bob Johnson"));
    }

    /**
     * Adds a new usage record to the system.
     *
     * @param meterID the water meter ID
     * @param date the date of usage
     * @param amount the amount of water used
     */
    public void addUsageRecord(String meterID, LocalDate date, double amount) {
        if (Validator.isValidMeterID(meterID) &&
                Validator.isValidUsageAmount(amount) &&
                Validator.isValidDate(date)) {

            UsageRecord record = new UsageRecord(meterID, date, amount);
            usageRecords.add(record);

            // Check for abnormal usage after adding record
            checkForAbnormalUsage(meterID);
        }
    }

    /**
     * Checks for abnormal usage patterns and generates alerts if needed.
     *
     * @param meterID the meter ID to check
     * @return true if abnormal usage detected, false otherwise
     */
    public boolean checkForAbnormalUsage(String meterID) {
        List<UsageRecord> recentRecords = getUsageHistory(meterID,
                LocalDate.now().minusDays(7),
                LocalDate.now());

        if (recentRecords.size() < 2) return false;

        double totalUsage = recentRecords.stream()
                .mapToDouble(UsageRecord::getUsageAmount)
                .sum();
        double averageDaily = totalUsage / recentRecords.size();

        // Alert if average daily usage > 200 liters
        if (averageDaily > 200) {
            Alert alert = new Alert(meterID, "HIGH_USAGE",
                    String.format("High water usage detected: %.2f L/day average", averageDaily));
            alerts.add(alert);
            alert.trigger();
            return true;
        }

        return false;
    }

    /**
     * Generates a usage report for a specific meter and period.
     *
     * @param meterID the meter ID
     * @param period description of the report period
     * @return generated usage report
     */
    public Report generateReport(String meterID, String period) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30); // Default to last 30 days

        List<UsageRecord> periodRecords = getUsageHistory(meterID, startDate, endDate);
        return new UsageReport(meterID, periodRecords, period);
    }

    /**
     * Retrieves usage history for a specific meter within a date range.
     *
     * @param meterID the meter ID
     * @param startDate start date of the range
     * @param endDate end date of the range
     * @return list of usage records within the specified range
     */
    public List<UsageRecord> getUsageHistory(String meterID, LocalDate startDate, LocalDate endDate) {
        return usageRecords.stream()
                .filter(record -> record.getMeterID().equals(meterID))
                .filter(record -> !record.getDate().isBefore(startDate) &&
                        !record.getDate().isAfter(endDate))
                .sorted((r1, r2) -> r1.getDate().compareTo(r2.getDate()))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Binds a meter to a user for access control.
     *
     * @param userID the user ID
     * @param meterID the meter ID to bind
     * @return true if binding successful, false otherwise
     */
    public boolean bindMeterToUser(String userID, String meterID) {
        return meters.stream().anyMatch(meter -> meter.getMeterId().equals(meterID));
    }

    // Getters for accessing system data
    public List<WaterMeter> getMeters() { return new ArrayList<>(meters); }
    public List<UsageRecord> getUsageRecords() { return new ArrayList<>(usageRecords); }
    public List<Alert> getAlerts() { return new ArrayList<>(alerts); }
}