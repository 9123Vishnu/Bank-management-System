import java.util.*;

class Account {
    private final int accountNumber;
    private final String holderName;
    private int balance;
    public Account(String holderName, int accountNumber) {
        this.holderName = holderName;
        this.accountNumber = accountNumber;
        this.balance = 0;
    }
    public int getAccountNumber() {
        return accountNumber;
    }
    public String getHolderName() {
        return holderName;
    }
    public int getBalance() {
        return balance;
    }
    public void deposit(int amount) {
        balance += amount;
    }
    public boolean withdraw(int amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }
}

class AccountRepository {
    private final Map<Integer, Account> accounts;
    public AccountRepository() {
        accounts = new HashMap<>();
    }
    public void save(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }
    public Account findByAccountNumber(int accountNumber) {
        return accounts.get(accountNumber);
    }
    public boolean exists(int accountNumber) {
        return accounts.containsKey(accountNumber);
    }
}

class AccountService {
    private final AccountRepository repository;
    private final Random random;
    public AccountService() {
        repository = new AccountRepository();
        random = new Random();
    }
    public void createAccount(String name) {
        int accountNumber = generateUniqueAccountNumber();
        Account account = new Account(name, accountNumber);
        repository.save(account);
        System.out.println("Account created successfully.");
        System.out.println("Account Number: " + accountNumber);
    }
    public void deposit(int accountNumber, int amount) {
        if (amount <= 0) {
            System.out.println("Invalid deposit amount.");
            return;
        }
        Account account = repository.findByAccountNumber(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        account.deposit(amount);
        System.out.println("Deposit successful.");
        System.out.println("Updated Balance: " + account.getBalance());
    }
    public void withdraw(int accountNumber, int amount) {
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount.");
            return;
        }
        Account account = repository.findByAccountNumber(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        boolean success = account.withdraw(amount);
        if (success) {
            System.out.println("Withdrawal successful.");
            System.out.println("Updated Balance: " + account.getBalance());
        } else {
            System.out.println("Insufficient balance.");
        }
    }
    public void transfer(int fromAccount, int toAccount, int amount) {
        if (amount <= 0) {
            System.out.println("Invalid transfer amount.");
            return;
        }
        Account sender = repository.findByAccountNumber(fromAccount);
        Account receiver = repository.findByAccountNumber(toAccount);
        if (sender == null || receiver == null) {
            System.out.println("One or both accounts not found.");
            return;
        }
        if (!sender.withdraw(amount)) {
            System.out.println("Insufficient balance.");
            return;
        }
        receiver.deposit(amount);
        System.out.println("Transfer successful.");
        System.out.println("Sender Balance: " + sender.getBalance());
        System.out.println("Receiver Balance: " + receiver.getBalance());
    }
    public void showBalance(int accountNumber) {
        Account account = repository.findByAccountNumber(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        System.out.println("Name: " + account.getHolderName());
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Balance: " + account.getBalance());
    }
    private int generateUniqueAccountNumber() {
        int accountNumber;
        do {
            accountNumber = 100000000 + random.nextInt(900000000);
        } while (repository.exists(accountNumber));
        return accountNumber;
    }
}

class BankApplication {
    private final AccountService service;
    private final Scanner scanner;
    public BankApplication() {
        service = new AccountService();
        scanner = new Scanner(System.in);
    }
    public void start() {
        while (true) {
            printMenu();
            try {
                int option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {
                    case 1 -> createAccount();
                    case 2 -> deposit();
                    case 3 -> withdraw();
                    case 4 -> transfer();
                    case 5 -> showBalance();
                    case 6 -> {
                        System.out.println("Thank you for using Bank System.");
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch(InputMismatchException e) {
                System.out.println("Invalid input");
                scanner.nextLine();
            }
        }
    }
    private void printMenu() {
        System.out.println("\n===== BANK MENU =====");
        System.out.println("1. Create Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Show Balance");
        System.out.println("6. Exit");
        System.out.print("Enter option: ");
    }
    private void createAccount() {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        service.createAccount(name);
    }
    private void deposit() {
        System.out.print("Enter Account Number: ");
        int accountNumber = scanner.nextInt();
        System.out.print("Enter Amount: ");
        int amount = scanner.nextInt();
        service.deposit(accountNumber, amount);
    }
    private void withdraw() {
        System.out.print("Enter Account Number: ");
        int accountNumber = scanner.nextInt();
        System.out.print("Enter Amount: ");
        int amount = scanner.nextInt();
        service.withdraw(accountNumber, amount);
    }
    private void transfer() {
        System.out.print("Enter Sender Account Number: ");
        int fromAccount = scanner.nextInt();
        System.out.print("Enter Receiver Account Number: ");
        int toAccount = scanner.nextInt();
        System.out.print("Enter Amount: ");
        int amount = scanner.nextInt();
        service.transfer(fromAccount, toAccount, amount);
    }
    private void showBalance() {
        System.out.print("Enter Account Number: ");
        int accountNumber = scanner.nextInt();
        service.showBalance(accountNumber);
    }
}

public class Bank_Management_System {
    public static void main(String[] args) {
        BankApplication app = new BankApplication();
        app.start();
    }
}