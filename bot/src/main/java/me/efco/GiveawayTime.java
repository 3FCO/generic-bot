package me.efco;

import org.jetbrains.annotations.Nullable;

public enum GiveawayTime {
    SECONDS("s"),
    MINUTES("m"),
    HOURS("h"),
    DAYS("d");

    private String id;
    GiveawayTime(String id) {
        this.id = id;
    }

    @Nullable
    public static GiveawayTime fromId(String id) {
        for (GiveawayTime gt : GiveawayTime.values()) {
            if (gt.id.equals(id)) {
                return gt;
            }
        }

        return null;
    }

    public String getId() {
        return id;
    }
}
