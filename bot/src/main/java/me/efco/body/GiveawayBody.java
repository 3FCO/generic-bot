package me.efco.body;

import java.time.Instant;

public record GiveawayBody(int id, String reward, Instant end) {
}
