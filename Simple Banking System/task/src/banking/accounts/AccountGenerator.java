package banking.accounts;

import java.util.concurrent.ThreadLocalRandom;

public class AccountGenerator {
    private static final String BIN = "400000";
    private static int counter = 1;

    // Create new card account
    public static Account createAccount() {
        String cardNumber = generateCardNumber();
        String pin = generatePin();
        return new Account(counter ,cardNumber , pin, 0);
    }

    // Generate card number
    private static String generateCardNumber() {
        counter += 1;
        StringBuilder numb = new StringBuilder(BIN);
        String formattedAccountIdentifier = String.format("%09d", counter);
        numb.append(formattedAccountIdentifier);
        numb.append(lunhAlgorithm(numb.toString()));
        return numb.toString();
    }

    // Generate checksum for card
    private static String lunhAlgorithm(String cardNumber) {
        int sum = 0;
        for (int i = 0; i < cardNumber.length(); i++) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            if (i % 2 == 0) {
                int doubleDigit = digit * 2 > 9 ? digit * 2 - 9 : digit * 2;
                sum += doubleDigit;
                continue;
            }
            sum += digit;
        }

         int checkDigit = 10 - (sum % 10);
         if (checkDigit == 10) {
             checkDigit = 0;
         }
         return Integer.toString(checkDigit);
    }

    // Generate card pin
    private static String generatePin() {
        int num = ThreadLocalRandom.current().nextInt(9999);
        return String.format("%04d", num);
    }
}
