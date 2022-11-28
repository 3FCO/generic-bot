package me.efco.commands;

import me.efco.BotUtilities;
import me.efco.GiveawayTime;
import me.efco.data.DatabaseConnection;
import me.efco.data.PropertiesLoader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.utils.Timestamp;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.awt.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GiveawayCommand extends AbstractCommand {
    public GiveawayCommand() {
        super(
                "giveaway",
                "Giveaway related stuffs",
                new ArrayList<>(),
                List.of(),
                List.of(
                        new SubcommandData("create", "Create a new giveaway")
                                .addOption(OptionType.STRING, "price", "Winning price", true)
                                .addOption(OptionType.STRING, "time", "Run time. Postfix with (s,m,h,d)", true)
                                .addOption(OptionType.INTEGER, "winner-amount", "Amount of winners!", true),
                        new SubcommandData("end", "End a specific giveaway")
                                .addOption(OptionType.INTEGER, "id", "Id of giveaway", true)
                )
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        switch (event.getSubcommandName()) {
            case "create" -> {
                createSubCommand(event);
            }
            default -> {
                event.reply("Currently not supported").setEphemeral(true).queue();
            }
        }
    }

    private void createSubCommand(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();

        String price = event.getOption("price").getAsString();
        String timeRaw = event.getOption("time").getAsString();
        int winnersAmount = event.getOption("winner-amount").getAsInt();

        int time = BotUtilities.parseIntegerOrDefault(timeRaw.substring(0, timeRaw.length()-1), 0);
        GiveawayTime postfix = GiveawayTime.fromId(timeRaw.substring(timeRaw.length()-1));
        if (postfix == null || time <= 0) {
            event.getHook().sendMessage("Formatting error").queue();
            return;
        };

        TextChannel targetChannel = event.getGuild().getTextChannelById(PropertiesLoader.getInstance().getProperty("giveaway_channel"));
        if (targetChannel == null) {
            event.getHook().sendMessage("No giveaway channel specified!").queue();
            return;
        };

        Instant ending = Instant.now();
        switch (postfix) {
            case SECONDS -> ending = ending.plus(time, ChronoUnit.SECONDS);
            case MINUTES -> ending = ending.plus(time, ChronoUnit.MINUTES);
            case HOURS -> ending = ending.plus(time, ChronoUnit.HOURS);
            case DAYS -> ending = ending.plus(time, ChronoUnit.DAYS);
        }

        MessageEmbed messageEmbed = new EmbedBuilder()
                .setTitle(price)
                .addField("End time", "<t:" + (ending.getEpochSecond()) + ":R>", false)
                .addField("Number of winners", winnersAmount + "", false)
                .build();

        targetChannel.sendMessageEmbeds(messageEmbed).queue();

        event.getHook().sendMessage("Giveaway has been created!").queue();
    }
}
