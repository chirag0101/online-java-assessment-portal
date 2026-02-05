package com.iris.OnlineCompilerBackend.constants;

public enum CompilerActions {
    COMPILE_ACTION("COMPILE", 1),
    RUN_ACTION("RUN", 2),
    SAVE("SAVE",3);

    private final String action;
    private final int actionKey;

    // All-args constructor
    CompilerActions(String action, int actionKey) {
        this.action = action;
        this.actionKey = actionKey;
    }

    // Getters
    public String getAction() {
        return action;
    }

    public int getActionKey() {
        return actionKey;
    }

    // Utility methods
    public static String getActionById(int actionId) {
        for (CompilerActions actions : CompilerActions.values()) {
            if (actions.getActionKey()==actionId) {
                return actions.getAction();
            }
        }
        throw new IllegalArgumentException("Invalid action ID: " + actionId);
    }

    public static int getIdByAction(String action) {
        if (action == null || action.trim().isEmpty()) {
            throw new IllegalArgumentException("Action cannot be null or empty");
        }
        for (CompilerActions actions : CompilerActions.values()) {
            if (actions.getAction().equalsIgnoreCase(action)) {
                return actions.getActionKey();
            }
        }
        throw new IllegalArgumentException("Invalid action: " + action);
    }
}
