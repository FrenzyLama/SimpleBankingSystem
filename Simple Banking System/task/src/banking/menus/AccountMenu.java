package banking.menus;

import banking.accounts.AccountsManager;

import java.util.Scanner;

public class AccountMenu {
    private static final Scanner scanner = new Scanner(System.in);

    public static void selectMenuCommand() {
        System.out.printf("\n%s\n", MENU_BLANK);// Print menu blank list
        int numberOfMenuItem = Integer.parseInt(scanner.nextLine());
        switch (AccountMenuItem.valueOfIndex(numberOfMenuItem)) {
            case BALANCE -> {
                AccountsManager.printBalanceCard();
            }
            case ADD_INCOME -> {
                System.out.println("Enter income:");
                int inputIncome = scanner.nextInt();
                AccountsManager.addIncome(inputIncome);
            }
            case DO_TRANSFER -> {
                System.out.println("\nTransfer");
                System.out.println("Enter card number:");
                String inputNumber = scanner.nextLine();
                boolean isValid = AccountsManager.checkCardToTransfer(inputNumber);

                if (isValid) {
                    System.out.println("Enter how much money you want to transfer:");
                    int inputAmount = scanner.nextInt();
                    AccountsManager.doTransfer(inputNumber, inputAmount);
                }
            }
            case CLOSE_ACCOUNT -> {
                AccountsManager.closeAccount();
                MainMenu.logOut();
            }
            case LOG_OUT -> {
                AccountsManager.setCurrentAccountNumber("");
                MainMenu.logOut();
            }
            case EXIT -> {//End execute program
                MainMenu.endSession();
            }
        }
    }

    private static final String MENU_BLANK = "1. Balance\n" +
            "2. Add income\n" +
            "3. Do transfer\n" +
            "4. Close account\n" +
            "5. Log out\n" +
            "0. Exit";
}
