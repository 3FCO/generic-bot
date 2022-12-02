package me.efco.body;

public record WarnBody(int uniqueId, long userId, String userName, long adminId, String adminName, String reason, boolean active) {
}
