package me.efco.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class UnmuteCommand extends AbstractCommand {
    public UnmuteCommand() {
        super(
                "unmute",
                "Unmute user from server G",
                new ArrayList<>(),
                List.of(
                        new OptionData(OptionType.USER, "user", "User to unmute from server", true)
                ),
                new ArrayList<>()
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();

        User user = event.getOption("user").getAsUser(); //required

        event.getGuild().removeTimeout(user).queue();
        event.getHook().sendMessage("User has successfully been unmuted").queue();
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {

    }
}
