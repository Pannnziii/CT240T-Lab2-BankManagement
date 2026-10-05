import account.BankAccount;
import account.SavingAccount;
import exception.InsufficientBalanceException;
import exception.InvalidAmountException;
import manager.BankManager;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Chương trình console quản lý tài khoản & giao dịch ngân hàng.
 */
public class Main {

    private static final BankManager manager = new BankManager();
    private static final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        seedData();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> createSavingAccount();
                    case "2" -> deposit();
                    case "3" -> withdraw();
                    case "4" -> transfer();
                    case "5" -> showAccount();
                    case "6" -> listAccounts();
                    case "7" -> System.out.printf("Tổng số dư toàn hệ thống: %,.0f VNĐ%n",
                            manager.calculateTotalBalance());
                    case "0" -> {
                        running = false;
                        System.out.println("Tạm biệt!");
                    }
                    default -> System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại.");
                }
            } catch (InvalidAmountException e) {
                System.out.println("[Lỗi số tiền] " + e.getMessage());
            } catch (InsufficientBalanceException e) {
                System.out.println("[Lỗi số dư] " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("[Lỗi nhập liệu] Vui lòng nhập một số hợp lệ.");
            } catch (IllegalArgumentException e) {
                System.out.println("[Lỗi dữ liệu] " + e.getMessage());
            } catch (Exception e) {
                // Bẫy mọi ngoại lệ còn lại để chương trình không bị sập
                System.out.println("[Lỗi không xác định] " + e.getMessage());
            }
        }
    }

    private static void seedData() {
        try {
            manager.addAccount(new SavingAccount("TK001", "Nguyễn Văn A", 1_000_000, 0.05));
            manager.addAccount(new SavingAccount("TK002", "Trần Thị B", 500_000, 0.06));
        } catch (InsufficientBalanceException e) {
            System.out.println("Không thể khởi tạo dữ liệu mẫu: " + e.getMessage());
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("===== QUẢN LÝ TÀI KHOẢN NGÂN HÀNG =====");
        System.out.println("1. Tạo tài khoản tiết kiệm");
        System.out.println("2. Nạp tiền");
        System.out.println("3. Rút tiền");
        System.out.println("4. Chuyển tiền");
        System.out.println("5. Xem thông tin tài khoản");
        System.out.println("6. Danh sách tài khoản");
        System.out.println("7. Tổng số dư toàn hệ thống");
        System.out.println("0. Thoát");
        System.out.print("Chọn chức năng: ");
    }

    private static String prompt(String label) {
        System.out.print(label);
        return scanner.nextLine().trim();
    }

    private static double promptDouble(String label) {
        return Double.parseDouble(prompt(label));
    }

    private static BankAccount requireAccount(String accountNumber) {
        BankAccount acc = manager.findAccount(accountNumber);
        if (acc == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản: " + accountNumber);
        }
        return acc;
    }

    private static void createSavingAccount() throws InsufficientBalanceException {
        String number = prompt("Số tài khoản: ");
        String name = prompt("Tên chủ tài khoản: ");
        double balance = promptDouble("Số dư ban đầu (>= 50000): ");
        double rate = promptDouble("Lãi suất (VD 0.05 = 5%): ");
        manager.addAccount(new SavingAccount(number, name, balance, rate));
        System.out.println("Tạo tài khoản thành công.");
    }

    private static void deposit() throws InvalidAmountException {
        BankAccount acc = requireAccount(prompt("Số tài khoản: "));
        acc.deposit(promptDouble("Số tiền nạp: "));
        System.out.printf("Nạp tiền thành công. Số dư mới: %,.0f VNĐ%n", acc.getBalance());
    }

    private static void withdraw() throws InvalidAmountException, InsufficientBalanceException {
        BankAccount acc = requireAccount(prompt("Số tài khoản: "));
        acc.withdraw(promptDouble("Số tiền rút: "));
        System.out.printf("Rút tiền thành công. Số dư mới: %,.0f VNĐ%n", acc.getBalance());
    }

    private static void transfer() throws InvalidAmountException, InsufficientBalanceException {
        String from = prompt("Tài khoản nguồn: ");
        String to = prompt("Tài khoản đích: ");
        double amount = promptDouble("Số tiền chuyển: ");
        manager.transferMoney(from, to, amount);
        System.out.println("Chuyển tiền thành công.");
        System.out.println("  " + manager.findAccount(from));
        System.out.println("  " + manager.findAccount(to));
    }

    private static void showAccount() {
        System.out.println(requireAccount(prompt("Số tài khoản: ")));
    }

    private static void listAccounts() {
        List<BankAccount> all = manager.getAllAccounts();
        if (all.isEmpty()) {
            System.out.println("Chưa có tài khoản nào.");
            return;
        }
        all.forEach(System.out::println);
    }
}
