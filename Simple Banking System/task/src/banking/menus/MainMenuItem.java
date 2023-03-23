package banking.menus;

import java.util.HashMap;
import java.util.Map;

public enum MainMenuItem {
    CREATE_ACCOUNT(1),
    LOGIN(2),
    EXIT(0);

    private final int index;

    private static final Map<Integer, MainMenuItem> BY_INDEX = new HashMap<>();

    static {
        for (MainMenuItem menuItem : values()) {
            BY_INDEX.put(menuItem.index, menuItem);
        }
    }

    MainMenuItem(int index) {
        this.index = index;
    }

    public static MainMenuItem valueOfIndex(int index) {
        return BY_INDEX.get(index);
    }

}
