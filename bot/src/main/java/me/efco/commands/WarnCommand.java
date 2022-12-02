package me.efco.commands;

import me.efco.body.WarnBody;
import me.efco.data.DatabaseConnection;
import me.efco.data.PropertiesLoader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.util.ArrayList;
import java.util.List;
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
                                .addOption(OptionType.USER, "user", "Target user to view warnings", true)
                                .addOption(OptionType.BOOLEAN, "activeonly", "Only show active warnings", true),
                        new SubcommandData("reset", "Set all active warnings to inactive of specific user")
                                .addOption(OptionType.USER, "user", "User whose warnings to reset"),
                        new SubcommandData("remove", "Remove specific warning by ID")
                                .addOption(OptionType.INTEGER, "warnid", "Warning ID")
                )
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        switch (event.getSubcommandName()) {
            case "issue" -> {
                issueSubCommand(event);
            }
            case "view" -> {
                viewSubCommand(event);
            }
            case "reset" -> {
                resetSubCommand(event);
            }
            case "remove" -> {
                removeSubCommand(event);
            }
        }
    }

    public void issueSubCommand(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();

        User user = event.getOption("user").getAsUser(); //required
        String reason = event.getOption("reason").getAsString(); //required
        User admin = event.getUser();

        DatabaseConnection.getInstance().userWarned(user.getIdLong(), user.getName(), admin.getIdLong(), admin.getName(), reason);
        int activeWarnings = DatabaseConnection.getInstance().getUserActiveWarningsCount(user.getIdLong());

        if (activeWarnings >= PropertiesLoader.getInstance().getPropertyAsInteger("warning_threshold")) {
            String punishmentType = PropertiesLoader.getInstance().getProperty("warning_punishment_type");
            int punishmentDuration = PropertiesLoader.getInstance().getPropertyAsInteger("warning_punishment_duration");

            DatabaseConnection.getInstance().resetUserWarnings(user.getIdLong());

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
        event.deferReply().queue();

        User user = event.getOption("user").getAsUser(); //required
        boolean activeOnly = event.getOption("activeonly").getAsBoolean(); //required
        User admin = event.getUser();

        List<WarnBody> warnings;
        if (activeOnly) {
            warnings = DatabaseConnection.getInstance().getUserActiveWarnings(user.getIdLong());
        } else {
            warnings = DatabaseConnection.getInstance().getUserAllWarnings(user.getIdLong());
        }

        MessageCreateBuilder messageBuilder = new MessageCreateBuilder();
        for (WarnBody wb : warnings) {
            messageBuilder.addEmbeds( new EmbedBuilder()
                    .setTitle("Warning #" + String.format("%05d", wb.uniqueId()) + " [" + (wb.active() ? "Active" : "Inactive") + "]")
                    .addField("Admin", wb.adminName() + " [" + wb.adminId() + "]", false)
                    .addField("User", wb.userName() + " [" + wb.userId() + "]", false)
                    .addField("Reason", wb.reason(), false).build() );
        }

        event.getHook().sendMessage(messageBuilder.build()).queue();
    }

    private void resetSubCommand(SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        User user = event.getOption("user").getAsUser(); //required
        User admin = event.getUser();

        DatabaseConnection.getInstance().resetUserWarnings(user.getIdLong());

        event.getHook().sendMessage("Users active warnings successfully set to inactive").queue();
    }

    private void removeSubCommand(SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        int warnId = event.getOption("warnid").getAsInt(); //required
        User admin = event.getUser();

        DatabaseConnection.getInstance().removeWarningById(warnId);

        event.getHook().sendMessage("Warning successfully set to inactive").queue();
    }
}
