package me.efco;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class GenericBot {
    private static JDA api;

    public static void main(String[] args) {
        api = JDABuilder.createDefault(System.getenv("bot_token"))
                .build();
    }
}