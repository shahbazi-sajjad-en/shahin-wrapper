package biz.espc.shahin.enumeration.transaction;

/**
 * Represents the lifecycle status of a transaction within the system.
 *
 * <p>
 * A transaction progresses through different states from initialization
 * to final completion or failure. These statuses are used for:
 * <ul>
 *     <li>Tracking transaction processing state</li>
 *     <li>Persisting current status in the database</li>
 *     <li>Auditing and reporting purposes</li>
 *     <li>Error handling and retry mechanisms</li>
 * </ul>
 * </p>
 *
 * <p>
 * Typical lifecycle:
 * <pre>
 * INITIALIZE → SUCCESS
 * INITIALIZE → FAILED
 * INITIALIZE → CORE_FAILED
 * INITIALIZE → TIMEOUT
 * </pre>
 * </p>
 *
 * @author Ebrahim Sheyki
 */
public enum TransactionStatus {

    /**
     * Transaction has been created and persisted,
     * but processing has not yet completed.
     */
    INITIALIZE,

    /**
     * Transaction completed successfully
     * and external system confirmed execution.
     */
    SUCCESS,

    /**
     * Transaction failed due to a general system or business error.
     */
    FAILED,

    /**
     * Transaction failed specifically in the core banking system.
     */
    CORE_FAILED,

    /**
     * Transaction did not receive a response within the expected time window.
     */
    TIMEOUT
}

// Control Panel\System and Security\Windows Defender Firewall\Customize Settings