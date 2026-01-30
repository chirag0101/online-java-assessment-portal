package com.iris.OnlineCompilerBackend.constants;

public enum ReviewStatus {
    TO_BE_REVIEWED("TO-BE-REVIEWED", 1),
    PASSED("SELECTED", 2),
    FAILED("REJECTED", 3);

    private final String status;
    private final Integer statusId;

    // All-args constructor
    ReviewStatus(String status, Integer statusId) {
        this.status = status;
        this.statusId = statusId;
    }

    // Getters
    public String getStatus() {
        return status;
    }

    public Integer getStatusId() {
        return statusId;
    }

    // Utility methods
    public static String getStatusById(Integer statusId) {
        for (ReviewStatus reviewStatus : ReviewStatus.values()) {
            if (reviewStatus.getStatusId().equals(statusId)) {
                return reviewStatus.getStatus();
            }
        }
        return null;
    }
    public static Integer getIdByStatus(String status) {
        for (ReviewStatus reviewStatus : ReviewStatus.values()) {
            if (reviewStatus.getStatus().equalsIgnoreCase(status)) {
                return reviewStatus.getStatusId();
            }
        }
        return null;
    }
}
