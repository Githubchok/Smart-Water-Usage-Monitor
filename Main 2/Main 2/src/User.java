/**
 * Represents a system user with login capabilities and meter access.
 *
 * <p><strong>Behavior:</strong> A {@code User} is considered logged in when
 * {@link #login(String)} succeeds (i.e., the provided meter ID matches the linked meter ID).
 * Calling {@link #logout()} flips the state back to logged out.</p>
 *
 * <p><strong>Simplifications:</strong></p>
 * <ul>
 *   <li>No credentials beyond a meter ID are validated.</li>
 *   <li>{@code isLoggedOut} mirrors the inverse of {@code isLoggedIn}; both are kept for clarity in this demo.</li>
 *   <li>No persistence or session timeout is implemented.</li>
 * </ul>
 *
 * <p><strong>Thread-safety:</strong> This class is not thread-safe. If accessed by multiple threads,
 * guard state changes externally.</p>
 *
 * @author YourName
 * @version 1.0
 */
public class User {

    /**
     * Unique application-level user identifier.
     */
    private String userID;

    /**
     * The water meter ID linked to this user.
     */
    private String linkedMeterID;

    /**
     * Flag indicating whether the user is currently logged in.
     */
    private boolean isLoggedIn;

    /**
     * Flag indicating whether the user is currently logged out.
     * <p>In this implementation, this mirrors {@code !isLoggedIn}.</p>
     */
    private boolean isLoggedOut;

    /**
     * Creates a new user with an associated meter ID.
     *
     * @param userID        the unique user identifier
     * @param linkedMeterID the water meter ID linked to this user
     */
    public User(String userID, String linkedMeterID) {
        this.userID = userID;
        this.linkedMeterID = linkedMeterID;
        this.isLoggedIn = false;
        this.isLoggedOut = true;
    }

    /**
     * Attempts to log in the user by verifying the provided meter ID against the linked meter ID.
     *
     * @param inputMeterID the meter ID provided for login
     * @return {@code true} if login is successful; {@code false} otherwise
     */
    public boolean login(String inputMeterID) {
        if (linkedMeterID.equals(inputMeterID)) {
            isLoggedIn = true;
            isLoggedOut = false;
            return true;
        }
        return false;
    }

    /**
     * Logs out the user, resetting the session state.
     */
    public void logout() {
        isLoggedIn = false;
        isLoggedOut = true;
    }

    /**
     * Returns a usage report placeholder for the linked meter if the user is logged in.
     *
     * @return a simple usage report string, or a prompt to log in first
     */
    public String viewUsageReport() {
        if (!isLoggedIn) {
            return "Please log in first.";
        }
        return "Usage report for meter: " + linkedMeterID;
    }

    /**
     * Receives and prints an alert message to standard output if the user is logged in.
     *
     * @param alert the alert to receive (must be non-null for meaningful output)
     */
    public void receiveAlert(Alert alert) {
        if (isLoggedIn) {
            System.out.println("Alert received: " + alert.getAlertMessage());
        }
    }

    // Getters

    /**
     * Returns this user's unique identifier.
     * @return user ID
     */
    public String getUserID() { return userID; }

    /**
     * Returns the meter ID linked to this user.
     * @return linked meter ID
     */
    public String getLinkedMeterID() { return linkedMeterID; }

    /**
     * Indicates whether the user is currently logged in.
     * @return {@code true} if logged in; {@code false} otherwise
     */
    public boolean isLoggedIn() { return isLoggedIn; }

    /**
     * Indicates whether the user is currently logged out.
     * @return {@code true} if logged out; {@code false} otherwise
     */
    public boolean isLoggedOut() { return isLoggedOut; }
}
