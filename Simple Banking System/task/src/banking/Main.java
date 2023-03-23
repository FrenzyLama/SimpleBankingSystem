package banking;

import banking.accounts.AccountsManager;
import banking.db.DBManager;
import banking.menus.AccountMenu;
import banking.menus.MainMenu;
import org.sqlite.SQLiteDataSource;

public class Main {
    public static void main(String[] args) {

        String url = "jdbc:sqlite:" + args[1];
        // Init data source
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        // Set data source to DB manager
        DBManager.setDataSource(dataSource);

        // Main work flow
        do {
            try {
                if (MainMenu.isLogged()) {
                    AccountMenu.selectMenuCommand();
                } else {
                    MainMenu.selectMenuCommand();
                }

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (!MainMenu.isSessionEnded());

    }
}