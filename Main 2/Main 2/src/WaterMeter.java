/**
 * Simple data class representing a water meter and its associated metadata.
 *
 * <p>Each instance holds an immutable meter identifier and mutable attributes
 * for physical location and owner's display name.</p>
 *
 * @author YourName
 * @version 1.0
 * @since 1.0
 */
public class WaterMeter {

    /**
     * Unique identifier of the water meter (e.g., {@code WM001}).
     * <p>This value is set at construction and not intended to change.</p>
     */
    private String meterId;

    /**
     * Human-readable location of the meter (e.g., street address or unit).
     */
    private String location;

    /**
     * Name of the person or entity that owns the meter.
     */
    private String ownerName;

    /**
     * Constructs a new {@code WaterMeter} with the given attributes.
     *
     * @param meterId   unique meter identifier
     * @param location  descriptive location of the meter
     * @param ownerName meter owner's name
     */
    public WaterMeter(String meterId, String location, String ownerName) {
        this.meterId = meterId;
        this.location = location;
        this.ownerName = ownerName;
    }

    /**
     * Returns the unique meter identifier.
     *
     * @return meter ID
     */
    public String getMeterId() {
        return meterId;
    }

    /**
     * Returns the meter's location description.
     *
     * @return location string
     */
    public String getLocation() {
        return location;
    }

    /**
     * Updates the meter's location description.
     *
     * @param location new location string
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Returns the owner's name.
     *
     * @return owner name
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Updates the owner's name.
     *
     * @param ownerName new owner name
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
