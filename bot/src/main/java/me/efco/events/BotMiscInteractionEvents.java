package me.efco.events;

import me.efco.GiveawayHandler;
import me.efco.commands.SlashCommandHandler;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotMiscInteractionEvents extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String id = event.getButton().getId();

        switch (id) {
            case "ga-join" -> GiveawayHandler.getInstance().joinGiveaway(event.getMessageIdLong(), event);
        }
    }
}
