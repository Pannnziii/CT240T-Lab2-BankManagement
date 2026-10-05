package account;

import exception.InsufficientBalanceException;
import exception.InvalidAmountException;

/**
 * Tài khoản ngân hàng (lớp trừu tượng).
 *
 * @invariant balance >= 0 (số dư không bao giờ âm)
 */
public abstract class BankAccount {

    private String accountNumber;
    private String holderName;
    private double balance;

    /**
     * @param accountNumber Số tài khoản
     * @param holderName    Tên chủ tài khoản
     * @param balance       Số dư ban đầu
     * @throws InsufficientBalanceException Nếu số dư ban đầu vi phạm bất biến (balance < số dư tối thiểu)
     */
    @SuppressWarnings("this-escape") // getMinimumBalance() chỉ trả về hằng số nên an toàn khi gọi trong constructor
    public BankAccount(String accountNumber, String holderName, double balance)
            throws InsufficientBalanceException {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        // Gọi hàm kiểm tra bất biến (có thể được lớp con mở rộng qua getMinimumBalance()).
        checkInvariant(balance);
        this.balance = balance;
    }

    // ---------------------------------------------------------------------
    // Getter / Setter
    // ---------------------------------------------------------------------

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public double getBalance() {
        return balance;
    }

    /**
     * Gán số dư mới (chỉ chấp nhận nếu không vi phạm bất biến của lớp).
     *
     * @throws InsufficientBalanceException Nếu balance < getMinimumBalance()
     */
    public void setBalance(double balance) throws InsufficientBalanceException {
        checkInvariant(balance);
        this.balance = balance;
    }

    // ---------------------------------------------------------------------
    // Invariant
    // ---------------------------------------------------------------------

    /**
     * Số dư tối thiểu theo bất biến của lớp. Lớp cơ sở: 0.
     * Lớp con có thể ghi đè để mở rộng bất biến (VD: SavingAccount = 50.000).
     */
    public double getMinimumBalance() {
        return 0;
    }

    /**
     * Kiểm tra bất biến: newBalance >= getMinimumBalance().
     */
    protected void checkInvariant(double newBalance) throws InsufficientBalanceException {
        if (newBalance < getMinimumBalance()) {
            throw new InsufficientBalanceException(String.format(
                    "Vi phạm bất biến: số dư (%,.0f VNĐ) không được nhỏ hơn %,.0f VNĐ.",
                    newBalance, getMinimumBalance()));
        }
    }

    /**
     * Trừ tiền sau khi lớp con đã kiểm tra xong tiền điều kiện. Chỉ lớp con được dùng.
     */
    protected void decreaseBalance(double amount) {
        this.balance -= amount;
    }

    // ---------------------------------------------------------------------
    // Operations
    // ---------------------------------------------------------------------

    /**
     * Nạp tiền vào tài khoản ngân hàng.
     *
     * @param amount Số tiền cần nạp
     * @throws InvalidAmountException Tiền điều kiện: amount <= 0
     * @post-condition balance_new == balance_old + amount
     * @invariant balance_new >= 0
     */
    public void deposit(double amount) throws InvalidAmountException {
        // Pre-condition
        if (amount <= 0) {
            throw new InvalidAmountException("Số tiền nạp phải lớn hơn 0.");
        }
        double oldBalance = balance;
        balance += amount;
        // Post-condition
        assert balance == oldBalance + amount : "Post-condition vi phạm: deposit";
    }

    /**
     * Rút tiền khỏi tài khoản ngân hàng.
     *
     * @param amount Số tiền cần rút
     * @throws InvalidAmountException       Tiền điều kiện: amount <= 0
     * @throws InsufficientBalanceException Tiền điều kiện: balance < amount
     * @post-condition balance_new == balance_old - amount
     * @invariant balance_new >= 0
     */
    public abstract void withdraw(double amount)
            throws InsufficientBalanceException, InvalidAmountException;

    @Override
    public String toString() {
        return String.format("[%s] %s | Số dư: %,.0f VNĐ", accountNumber, holderName, balance);
    }
}
