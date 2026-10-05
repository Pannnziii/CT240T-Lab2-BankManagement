package account;

import exception.InsufficientBalanceException;
import exception.InvalidAmountException;

/**
 * Tài khoản tiết kiệm.
 *
 * @invariant balance >= 50,000 VNĐ (bất biến mở rộng so với BankAccount)
 */
public class SavingAccount extends BankAccount {

    public static final double MIN_BALANCE = 50_000;

    private double interestRate;

    /**
     * @param interestRate Lãi suất (VD: 0.05 = 5%/năm)
     * @throws InsufficientBalanceException Nếu số dư ban đầu < 50.000 VNĐ
     */
    public SavingAccount(String accountNumber, String holderName, double balance, double interestRate)
            throws InsufficientBalanceException {
        super(accountNumber, holderName, balance);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    @Override
    public double getMinimumBalance() {
        return MIN_BALANCE;
    }

    /**
     * Rút tiền khỏi tài khoản tiết kiệm.
     *
     * @param amount Số tiền cần rút
     * @throws InvalidAmountException       Tiền điều kiện: amount <= 0
     * @throws InsufficientBalanceException Tiền điều kiện: balance < amount,
     *                                      hoặc bất biến: balance_new < 50.000
     * @post-condition balance_new == balance_old - amount
     * @invariant balance_new >= 50,000
     */
    @Override
    public void withdraw(double amount) throws InsufficientBalanceException, InvalidAmountException {
        // Pre-conditions
        if (amount <= 0) {
            throw new InvalidAmountException("Số tiền rút phải lớn hơn 0.");
        }
        if (getBalance() < amount) {
            throw new InsufficientBalanceException(String.format(
                    "Số dư không đủ: hiện có %,.0f VNĐ, cần rút %,.0f VNĐ.", getBalance(), amount));
        }
        // Bất biến mở rộng: số dư còn lại phải >= 50.000
        double remaining = getBalance() - amount;
        if (remaining < MIN_BALANCE) {
            throw new InsufficientBalanceException(String.format(
                    "Tài khoản tiết kiệm phải giữ số dư tối thiểu %,.0f VNĐ (số dư còn lại sẽ là %,.0f VNĐ).",
                    MIN_BALANCE, remaining));
        }
        double oldBalance = getBalance();
        decreaseBalance(amount);
        // Post-condition
        assert getBalance() == oldBalance - amount : "Post-condition vi phạm: withdraw";
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Lãi suất: %.2f%%", interestRate * 100);
    }
}
