package banking.accounts;

import banking.db.DBManager;

import java.util.Objects;

public class AccountsManager {
    private static String currentAccountNumber;

    // Create new account
    public static void addNewAccount() {
        Account account = AccountGenerator.createAccount();
        DBManager.addNewCardToTable(account);
        printNewAccountInfo(account);
    }

    // Try to log in to account
    public static boolean login(String cardNumber, String pin) {
        boolean isLoginSuccess = DBManager.tryLoginToAccount(cardNumber, pin);
        if (isLoginSuccess) {
            System.out.println("You have successfully logged in!");
            currentAccountNumber = cardNumber;
            return true;
        } else {
            System.out.println("Wrong card number or PIN!");
            return false;
        }
    }

    // Get and print account balance
    public static void printBalanceCard() {
        int balance = DBManager.getBalanceFromDB(currentAccountNumber);
        System.out.println("Balance: " + balance);
    }

    public static void addIncome(int income) {
        DBManager.addIncome(income);
        System.out.println("Income was added!");
    }

    public static boolean checkCardToTransfer(String accountNumber) {
        if (Objects.equals(currentAccountNumber, accountNumber)) {
            System.out.println("You can't transfer money to the same account!");
            return false;
        }

        boolean isValidCard = checkLuhnAlgorithm(accountNumber);
        boolean isCardExist = DBManager.checkCardExist(accountNumber);

        if (!isValidCard) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
            return false;
        }

        if (!isCardExist) {
            System.out.println("Such a card does not exist.");
            return false;
        }

        return true;
    }

    public static void doTransfer(String receiver, int amountOfTransfer) {
        int currentBalance = DBManager.getBalanceFromDB(currentAccountNumber);
        if (amountOfTransfer <= currentBalance) {
            DBManager.transferToAnotherAccount(currentAccountNumber, receiver, amountOfTransfer);
            System.out.println("Success!");
        } else {
            System.out.println("Not enough money!");
        }
    }

    public static void closeAccount() {
        DBManager.deleteAccountFromDB(currentAccountNumber);
        currentAccountNumber = "";
        System.out.println("The account has been closed!");
    }

    public static void setCurrentAccountNumber(String currentAccountNumber) {
        AccountsManager.currentAccountNumber = currentAccountNumber;
    }

    public static String getCurrentAccountNumber() {
        return currentAccountNumber;
    }

    // Print info of new account
    private static void printNewAccountInfo(Account account) {
        System.out.println("\nYour card has been created");
        System.out.println("Your card number:");
        System.out.println(account.getCardNumber());
        System.out.println("Your card PIN:");
        System.out.println(account.getPin());
        System.out.println();
    }

    private static boolean checkLuhnAlgorithm(String cardNumber) {
        int result = 0;
        for (int i = 0; i < cardNumber.length(); i++) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            if (i % 2 == 0) {
                int doubleDigit = digit * 2 > 9 ? digit * 2 - 9 : digit * 2;
                result += doubleDigit;
                continue;
            }
            result += digit;
        }
        return result % 10 == 0;
    }
}
