package manager;

import account.BankAccount;
import exception.InsufficientBalanceException;
import exception.InvalidAmountException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Quản lý danh sách tài khoản theo accountNumber.
 */
public class BankManager {

    private final Map<String, BankAccount> accounts = new LinkedHashMap<>();

    /**
     * Thêm tài khoản mới.
     *
     * @throws IllegalArgumentException Nếu tài khoản null hoặc số tài khoản đã tồn tại
     */
    public void addAccount(BankAccount account) {
        if (account == null) {
            throw new IllegalArgumentException("Tài khoản không được null.");
        }
        if (accounts.containsKey(account.getAccountNumber())) {
            throw new IllegalArgumentException(
                    "Số tài khoản " + account.getAccountNumber() + " đã tồn tại.");
        }
        accounts.put(account.getAccountNumber(), account);
    }

    /**
     * Tìm tài khoản theo số tài khoản.
     *
     * @return tài khoản, hoặc null nếu không tồn tại
     */
    public BankAccount findAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    /**
     * Chuyển tiền giữa hai tài khoản.
     *
     * @pre  fromAcc và toAcc tồn tại, khác nhau; amount > 0; tài khoản nguồn đủ số dư
     * @post balance_from_new == balance_from_old - amount
     *       && balance_to_new == balance_to_old + amount
     * @throws IllegalArgumentException     Nếu tài khoản không tồn tại hoặc trùng nhau
     * @throws InvalidAmountException       Nếu amount <= 0
     * @throws InsufficientBalanceException Nếu không đủ số dư / vi phạm bất biến
     */
    public void transferMoney(String fromAcc, String toAcc, double amount)
            throws InvalidAmountException, InsufficientBalanceException {
        BankAccount from = findAccount(fromAcc);
        BankAccount to = findAccount(toAcc);
        if (from == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản nguồn: " + fromAcc);
        }
        if (to == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản đích: " + toAcc);
        }
        if (from == to) {
            throw new IllegalArgumentException("Tài khoản nguồn và đích không được trùng nhau.");
        }
        if (amount <= 0) {
            throw new InvalidAmountException("Số tiền chuyển phải lớn hơn 0.");
        }
        // withdraw ném ngoại lệ trước khi thay đổi bất kỳ số dư nào -> không cần rollback;
        // deposit không thể thất bại vì amount đã được kiểm tra > 0.
        from.withdraw(amount);
        to.deposit(amount);
    }

    /**
     * Generics Utility Method với Wildcards (Producer Extends): tính tổng số dư.
     */
    public double calculateTotalBalance(List<? extends BankAccount> accounts) {
        double total = 0.0;
        for (BankAccount acc : accounts) {
            total += acc.getBalance(); // Producer Extends
        }
        return total;
    }

    /** Tổng số dư toàn hệ thống. */
    public double calculateTotalBalance() {
        return calculateTotalBalance(getAllAccounts());
    }

    public List<BankAccount> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public int size() {
        return accounts.size();
    }
}
