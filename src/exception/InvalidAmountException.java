package exception;

/**
 * Ngoại lệ Checked: ném ra khi số tiền nạp/rút <= 0.
 */
public class InvalidAmountException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidAmountException(String message) {
        super(message);
    }
}
