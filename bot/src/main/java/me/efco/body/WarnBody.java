package me.efco.body;

public record WarnBody(int uniqueId, String userId, String userName, String adminId, String adminName, String reason, boolean active) {
}
