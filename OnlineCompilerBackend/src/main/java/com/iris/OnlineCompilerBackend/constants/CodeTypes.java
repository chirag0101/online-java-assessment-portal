package com.iris.OnlineCompilerBackend.constants;

public enum CodeTypes {
    JAVA("JAVA", 1),
    SQL("SQL", 2),
    JSON("JSON", 3),
    TEXT("TEXT", 4);

    private final String codeType;
    private final int codeTypeKey;

    // All-args constructor
    CodeTypes(String codeType, int codeTypeKey) {
        this.codeType = codeType;
        this.codeTypeKey = codeTypeKey;
    }

    // Getters
    public String getCodeType() {
        return codeType;
    }

    public int getCodeTypeKey() {
        return codeTypeKey;
    }

    // Utility methods
    public static String getCodeTypeById(int codeTypeKey) {
        for (CodeTypes codeTypes : CodeTypes.values()) {
            if (codeTypes.getCodeTypeKey()==codeTypeKey) {
                return codeTypes.getCodeType();
            }
        }
        throw new IllegalArgumentException("Invalid code type key: " + codeTypeKey);
    }

    public static int getIdByCodeType(String codeType) {
        if (codeType == null || codeType.trim().isEmpty()) {
            throw new IllegalArgumentException("Code type cannot be null or empty");
        }
        for (CodeTypes codeTypes : CodeTypes.values()) {
            if (codeTypes.getCodeType().equalsIgnoreCase(codeType)) {
                return codeTypes.getCodeTypeKey();
            }
        }
        throw new IllegalArgumentException("Invalid code type: " + codeType);
    }
}
