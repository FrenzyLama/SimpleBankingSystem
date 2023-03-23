package banking.menus;

import banking.accounts.AccountsManager;

import java.util.Scanner;

public class MainMenu {
    private static boolean sessionEnded = false;
    private static boolean logged = false;
    private static final Scanner scanner = new Scanner(System.in);

    public static void selectMenuCommand() {
        System.out.printf("\n%s\n", MENU_BLANK);// Print menu blank list
        int numberOfMenuItem = Integer.parseInt(scanner.nextLine());
        switch (MainMenuItem.valueOfIndex(numberOfMenuItem)) {
            case CREATE_ACCOUNT -> {
                AccountsManager.addNewAccount();
            }
            case LOGIN -> {
                System.out.println("\nEnter your card number:");
                String inputCardNumber = scanner.nextLine();
                System.out.println("Enter your PIN:");
                String inputPin = scanner.nextLine();

                logged = AccountsManager.login(inputCardNumber, inputPin);
            }
            case EXIT -> {//End execute program
                endSession();
            }
        }
    }

    public static boolean isSessionEnded() {
        return sessionEnded;
    }

    public static boolean isLogged() {
        return logged;
    }

    public static void logOut() {
        System.out.println("You have successfully logged out!");
        logged = false;
    }
    public static void endSession() {
        sessionEnded = true;
        System.out.println();
        System.out.println("Bye!");
        System.exit(0);
    }

    private static final String MENU_BLANK = "1. Create an account\n" +
            "2. Log into account\n" +
            "0. Exit";
}
