import java.time.LocalDate;

/**
 * Utility class with simple, stateless validators for water-meter inputs.
 *
 * <p><strong>Scope:</strong> Methods validate basic format/range constraints only.
 * They do <em>not</em> check persistence or actual existence in a data store.</p>
 *
 * <p><strong>Thread-safety:</strong> All methods are pure and static; this class is thread-safe.</p>
 *
 * @author YourName
 * @version 1.0
 * @since 1.0
 */
public class Validator {

    /**
     * Checks whether a meter ID is non-blank and matches the expected format.
     *
     * <p><strong>Format:</strong> {@code WM} followed by exactly three digits (regex: {@code WM\\d{3}}),
     * e.g., {@code WM001}, {@code WM123}.</p>
     *
     * <p><em>Note:</em> This method validates <em>format only</em>; it does not verify that
     * the meter ID exists in your database.</p>
     *
     * @param meterID the meter ID to validate (may be {@code null})
     * @return {@code true} if non-null, non-blank, and matches {@code WM\\d{3}}; {@code false} otherwise
     */
    public static boolean isValidMeterID(String meterID) {
        if (meterID == null || meterID.trim().isEmpty()) {
            return false;
        }

        // Check format: should start with "WM" followed by numbers
        return meterID.matches("WM\\d{3}");
    }

    /**
     * Validates whether a usage amount (in liters) falls within a reasonable daily range.
     *
     * <p><strong>Rule:</strong> Accepts values in the inclusive range {@code [0, 10000]} liters.</p>
     *
     * @param amount the usage amount in liters
     * @return {@code true} if {@code 0 <= amount <= 10000}; {@code false} otherwise
     */
    public static boolean isValidUsageAmount(double amount) {
        return amount >= 0 && amount <= 10000; // Reasonable daily usage range
    }

    /**
     * Validates whether a date is acceptable for usage recording.
     *
     * <p><strong>Rules:</strong></p>
     * <ul>
     *   <li>Date must not be {@code null}.</li>
     *   <li>Date must not be in the future relative to {@link LocalDate#now()} (system default clock).</li>
     *   <li>Date must not be more than one year in the past (inclusive window of the past year).</li>
     * </ul>
     *
     * @param date the usage date to validate
     * @return {@code true} if {@code date} is within {@code [now - 1 year, now]} (inclusive); {@code false} otherwise
     */
    public static boolean isValidDate(LocalDate date) {
        if (date == null) return false;

        LocalDate now = LocalDate.now();
        LocalDate oneYearAgo = now.minusYears(1);

        // Date should not be in future or more than 1 year old
        return !date.isAfter(now) && !date.isBefore(oneYearAgo);
    }
}
