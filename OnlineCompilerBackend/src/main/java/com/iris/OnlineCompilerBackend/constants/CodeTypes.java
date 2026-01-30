package com.iris.OnlineCompilerBackend.constants;

public enum CodeTypes {
    JAVA("JAVA", 1),
    SQL("SQL", 2),
    JSON("JSON", 3),
    TEXT("TEXT", 4);

    private final String codeType;
    private final Integer codeTypeKey;

    // All-args constructor
    CodeTypes(String codeType, Integer codeTypeKey) {
        this.codeType = codeType;
        this.codeTypeKey = codeTypeKey;
    }

    // Getters
    public String getCodeType() {
        return codeType;
    }

    public Integer getCodeTypeKey() {
        return codeTypeKey;
    }

    // Utility methods
    public static String getCodeTypeById(Integer codeTypeKey) {
        for (CodeTypes codeTypes : CodeTypes.values()) {
            if (codeTypes.getCodeTypeKey().equals(codeTypeKey)) {
                return codeTypes.getCodeType();
            }
        }
        return null;
    }

    public static Integer getIdByCodeType(String codeType) {
        for (CodeTypes codeTypes : CodeTypes.values()) {
            if (codeTypes.getCodeType().equalsIgnoreCase(codeType)) {
                return codeTypes.getCodeTypeKey();
            }
        }
        return null;
    }
}
