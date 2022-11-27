package me.efco.body;

public class WarnBody {
    private final int uniqueId;
    private final String userId;
    private final String userName;
    private final String adminId;
    private final String adminName;
    private final String reason;
    private final boolean active;

    public WarnBody(int uniqueId, String userId, String userName, String adminId, String adminName, String reason, boolean active) {
        this.uniqueId = uniqueId;
        this.userId = userId;
        this.userName = userName;
        this.adminId = adminId;
        this.adminName = adminName;
        this.reason = reason;
        this.active = active;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getAdminId() {
        return adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getReason() {
        return reason;
    }

    public boolean isActive() {
        return active;
    }
}
