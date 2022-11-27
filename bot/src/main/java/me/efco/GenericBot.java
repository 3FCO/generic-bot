package me.efco;

import me.efco.commands.SlashCommandHandler;
import me.efco.data.PropertiesLoader;
import me.efco.data.DatabaseConnection;
import me.efco.events.BotMiscEvents;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;

import java.util.List;

public class GenericBot {
    private static JDA api;

    public static void main(String[] args) {
        DatabaseConnection.getInstance();
        PropertiesLoader.getInstance();

        api = JDABuilder.createDefault(PropertiesLoader.getInstance().getProperty("bot_token"))
                .setActivity(Activity.watching("Under Construction S1E1"))
                .addEventListeners(new BotMiscEvents())
                .addEventListeners(SlashCommandHandler.getInstance())
                .build();

        api.setRequiredScopes("identify", "bot", "webhook.incoming", "applications.commands");

        System.out.println("Bot has sucessfully started... Invitation link " + api.getInviteUrl(List.of(
                //TODO Near the end of development, appropriate permission should be set... Not Permission.ADMINISTRATOR
                Permission.ADMINISTRATOR
        )));
    }

    public static JDA getApi() {
        return api;
    }
}