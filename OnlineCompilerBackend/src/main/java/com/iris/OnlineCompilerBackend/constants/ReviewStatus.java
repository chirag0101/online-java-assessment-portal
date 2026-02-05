package com.iris.OnlineCompilerBackend.constants;

public enum ReviewStatus {
    TO_BE_REVIEWED("TO-BE-REVIEWED", 1),
    SELECTED("SELECTED", 2),
    REJECTED("REJECTED", 3);

    private final String status;
    private final int statusId;

    // All-args constructor
    ReviewStatus(String status, int statusId) {
        this.status = status;
        this.statusId = statusId;
    }

    // Getters
    public String getStatus() {
        return status;
    }

    public int getStatusId() {
        return statusId;
    }

    // Utility methods
    public static String getStatusById(int statusId) {
        for (ReviewStatus reviewStatus : ReviewStatus.values()) {
            if (reviewStatus.getStatusId()==statusId) {
                return reviewStatus.getStatus();
            }
        }
        throw new IllegalArgumentException("Invalid status ID: " + statusId);
    }
    
    public static Integer getIdByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        for (ReviewStatus reviewStatus : ReviewStatus.values()) {
            if (reviewStatus.getStatus().equalsIgnoreCase(status)) {
                return reviewStatus.getStatusId();
            }
        }
        throw new IllegalArgumentException("Invalid status: " + status);
    }
}
