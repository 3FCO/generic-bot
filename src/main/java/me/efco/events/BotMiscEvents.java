package me.efco.events;

import me.efco.commands.SlashCommandHandler;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotMiscEvents extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        SlashCommandHandler.getInstance().updateCommands();
    }
}
