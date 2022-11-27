package me.efco.commands;

import me.efco.data.DatabaseConnection;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.List;

public class GiveawayCommand extends AbstractCommand {
    public GiveawayCommand() {
        super(
                "giveaway",
                "Giveaway related stuffs",
                new ArrayList<>(),
                List.of(),
                List.of(
                        new SubcommandData("create", "Create a new giveaway")
                                .addOption(OptionType.STRING, "reward", "Reward for the giveaway")
                                .addOption(OptionType.STRING, "time", "Time with postfix (s,m,h,d)")
                )
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        switch (event.getSubcommandName()) {
            case "create" -> {
                createSubCommand(event);
            }
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {

    }

    private void createSubCommand(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();

        User user = event.getOption("reward").getAsUser(); //required
        String timeWithPostfix = event.getOption("time").getAsString(); //required
        User admin = event.getUser();


    }
}
