package banking.menus;

import java.util.HashMap;
import java.util.Map;

public enum AccountMenuItem {
    BALANCE(1),
    ADD_INCOME(2),
    DO_TRANSFER(3),
    CLOSE_ACCOUNT(4),
    LOG_OUT(5),
    EXIT(0);

    private final int index;

    private static final Map<Integer, AccountMenuItem> BY_INDEX = new HashMap<>();

    static {
        for (AccountMenuItem menuItem : values()) {
            BY_INDEX.put(menuItem.index, menuItem);
        }
    }

    AccountMenuItem(int index) {
        this.index = index;
    }

    public static AccountMenuItem valueOfIndex(int index) {
        return BY_INDEX.get(index);
    }

}
