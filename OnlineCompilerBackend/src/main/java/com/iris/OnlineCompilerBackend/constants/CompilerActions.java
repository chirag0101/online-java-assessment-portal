package com.iris.OnlineCompilerBackend.constants;

public enum CompilerActions {
    COMPILE_ACTION("COMPILE", 1),
    RUN_ACTION("RUN", 2);

    private final String action;
    private final Integer actionKey;

    // All-args constructor
    CompilerActions(String action, Integer actionKey) {
        this.action = action;
        this.actionKey = actionKey;
    }

    // Getters
    public String getAction() {
        return action;
    }

    public Integer getActionKey() {
        return actionKey;
    }

    // Utility methods
    public static String getActionById(Integer actionId) {
        for (CompilerActions actions : CompilerActions.values()) {
            if (actions.getActionKey().equals(actionId)) {
                return actions.getAction();
            }
        }
        return null;
    }

    public static Integer getIdByAction(String action) {
        for (CompilerActions actions : CompilerActions.values()) {
            if (actions.getAction().equalsIgnoreCase(action)) {
                return actions.getActionKey();
            }
        }
        return null;
    }
}
