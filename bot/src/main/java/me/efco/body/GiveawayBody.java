package me.efco.body;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public record GiveawayBody(long id, String reward, long end, int winnersAmount, List<Long> entries) {
}
