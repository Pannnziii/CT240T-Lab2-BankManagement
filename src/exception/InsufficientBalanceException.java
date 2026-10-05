package exception;

/**
 * Ngoại lệ Checked: ném ra khi số dư không đủ thỏa mãn tiền điều kiện
 * hoặc làm vi phạm bất biến của lớp (balance >= số dư tối thiểu).
 */
public class InsufficientBalanceException extends Exception {

    private static final long serialVersionUID = 1L;

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
