package me.efco.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.util.ArrayList;

public class HelpCommand extends AbstractCommand {
    public HelpCommand() {
        super(
                "help",
                "Display all usable commands G",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        SlashCommandHandler.getInstance().getCommands().forEach((key, command) -> {
            embedBuilder.addField(command.getName(), command.getDescription(), false);
        });


        event.reply(
                new MessageCreateBuilder().addEmbeds(embedBuilder.build()).build()
        ).queue();
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {

    }
}
