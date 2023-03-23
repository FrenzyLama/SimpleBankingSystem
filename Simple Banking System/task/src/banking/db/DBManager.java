package banking.db;

import banking.accounts.Account;
import banking.accounts.AccountsManager;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.HashMap;

public class DBManager {
    private static SQLiteDataSource dataSource;
    private static final String TABLE_FORMAT = """
            CREATE TABLE IF NOT EXISTS card(
            id INTEGER,
            number TEXT,
            pin TEXT,
            balance INTEGER DEFAULT 0);""";

    // Check the existence of the table, if don't create new
    public static void createTableIfNotExists() {
        try (Connection connection = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = connection.createStatement()) {
                // Statement execution
                statement.executeUpdate(TABLE_FORMAT);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add new account to table
    public static void addNewCardToTable(Account account) {
        String sql = "INSERT INTO card(id,number,pin,balance) VALUES(?,?,?,?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, account.getId());
            preparedStatement.setString(2, account.getCardNumber());
            preparedStatement.setString(3, account.getPin());
            preparedStatement.setInt(4, account.getBalance());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Try login to account
    public static boolean tryLoginToAccount(String number, String pin) {
        boolean result = false;

        String sql = "SELECT * FROM card WHERE number = ? AND pin = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, number);
            preparedStatement.setString(2, pin);

            // Get result
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    // TODO: check result
    // Get balance of account
    public static int getBalanceFromDB(String number) {
        int result = 0;
        String sql = "SELECT balance FROM card WHERE number = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, number);

            // Get result
            ResultSet resultSet = preparedStatement.executeQuery();

            // loop through the result set
            while (resultSet.next()) {
                result = resultSet.getInt("balance");
            }
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    // Add income to account balance
    public static void addIncome(int income) {
        String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, income);
            preparedStatement.setString(2, AccountsManager.getCurrentAccountNumber());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    // Check card is valid
    public static boolean checkCardExist (String number) {
        boolean result = false;

        String sql = "SELECT * FROM card WHERE number = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, number);

            // Get result
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    // Do transfer to another account
    public static void transferToAnotherAccount(String sender, String receiver, int amount) {
        String sqlUpdateBalanceReceiverAccount = "UPDATE card SET balance = balance + ? WHERE number = ?";
        String sqlUpdateBalanceSenderAccount = "UPDATE card SET balance = balance - ? WHERE number = ?";

        try (Connection connection = dataSource.getConnection()) {

            // Disable auto-commit mode
            connection.setAutoCommit(false);

            try (PreparedStatement updateReceiverBalance = connection.prepareStatement(sqlUpdateBalanceReceiverAccount);
                 PreparedStatement updateSenderBalance = connection.prepareStatement(sqlUpdateBalanceSenderAccount)) {

                // Update Receiver Balance
                updateReceiverBalance.setInt(1, amount);
                updateReceiverBalance.setString(2, receiver);
                updateReceiverBalance.executeUpdate();

                // Update Sender Balance
                updateSenderBalance.setInt(1, amount);
                updateSenderBalance.setString(2, sender);
                updateSenderBalance.executeUpdate();


                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAccountFromDB(String number) {
        String sql = "DELETE FROM card WHERE number = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, number);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    public static void setDataSource(SQLiteDataSource dataSource) {
        DBManager.dataSource = dataSource;
        createTableIfNotExists();
    }


}
