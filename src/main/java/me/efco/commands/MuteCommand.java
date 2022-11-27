package me.efco.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MuteCommand extends AbstractCommand {
    public MuteCommand() {
        super(
                "mute",
                "Mute user. Use commands /unmute to unmute again G",
                new ArrayList<>(),
                List.of(
                        new OptionData(OptionType.USER, "user", "User to mute from server", true),
                        new OptionData(OptionType.INTEGER, "duration", "Duration to mute for (minutes)", true)
                ),
                new ArrayList<>()
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();

        User user = event.getOption("user").getAsUser(); //required
        int duration = event.getOption("duration").getAsInt(); //required

        event.getGuild().timeoutFor(user, duration, TimeUnit.MINUTES).queue();
        event.getHook().sendMessage("User has successfully been timeout for " + duration + " minutes").queue();
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {

    }
}
