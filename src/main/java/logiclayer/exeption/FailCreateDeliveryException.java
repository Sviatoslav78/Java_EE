package logiclayer.exeption;

/**
 * Exception for signal that delivery record can not be created
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class FailCreateDeliveryException extends Exception {

    public FailCreateDeliveryException() {
    }

    public FailCreateDeliveryException(String message) {
        super(message);
    }

    public FailCreateDeliveryException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailCreateDeliveryException(Throwable cause) {
        super(cause);
    }

    public FailCreateDeliveryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
