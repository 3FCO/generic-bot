package me.efco.commands;

import me.efco.data.DatabaseConnection;
import me.efco.data.PropertiesLoader;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WarnCommand extends AbstractCommand {
    public WarnCommand() {
        super(
                "warn",
                "Warrrrnings G",
                new ArrayList<>(),
                List.of(),
                List.of(
                        new SubcommandData("issue", "Issue a new warning to a user")
                                .addOption(OptionType.USER, "user", "User to be issued warning", true)
                                .addOption(OptionType.STRING, "reason", "Reason for user to be warned", true),
                        new SubcommandData("view", "View a specific users warnings")
                                .addOption(OptionType.USER, "user", "Target user to view warnings")
                                .addOption(OptionType.BOOLEAN, "activeonly", "Only show active warnings", false)
                )
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        switch (event.getSubcommandName().toLowerCase()) {
            case "issue" -> {
                issueSubCommand(event);
            }
            case "view" -> {
                viewSubCommand(event);
            }
        }
    }

    public void issueSubCommand(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();

        User user = event.getOption("user").getAsUser(); //required
        String reason = event.getOption("reason").getAsString(); //required
        User admin = event.getUser();

        DatabaseConnection.getInstance().userWarned(user.getId(), user.getName(), admin.getId(), admin.getName(), reason);
        int activeWarnings = DatabaseConnection.getInstance().getUserActiveWarnings(user.getId());

        if (activeWarnings >= PropertiesLoader.getInstance().getPropertyAsInteger("warning_threshold")) {
            String punishmentType = PropertiesLoader.getInstance().getProperty("warning_punishment_type");
            int punishmentDuration = PropertiesLoader.getInstance().getPropertyAsInteger("warning_punishment_duration");

            DatabaseConnection.getInstance().resetUserWarnings(user.getId());

            switch (punishmentType) {
                case "kick" -> {
                    event.getGuild().kick(user).reason("You hit your warning threshold").queue();
                }
                case "ban" -> {
                    event.getGuild().ban(user, punishmentDuration, TimeUnit.MINUTES).queue();
                }
                case "timeout" -> {
                    event.getGuild().timeoutFor(user, punishmentDuration, TimeUnit.MINUTES).queue();
                }
            }

            event.getHook().sendMessage("User has successfully been warned and received punishment for hitting threshold!").queue();
        } else {
            event.getHook().sendMessage("User has successfully been warned. " + activeWarnings + " current active warnings").queue();
        }

    }

    public void viewSubCommand(SlashCommandInteractionEvent event) {

    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {

    }
}
