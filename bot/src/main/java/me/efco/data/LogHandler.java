package me.efco.data;

import me.efco.GenericBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

public class LogHandler {
    private static LogHandler instance;
    private TextChannel logChannel;

    private LogHandler() {
        logChannel = GenericBot.getApi().getTextChannelById(PropertiesLoader.getInstance().getProperty("log_channel"));
    }

    public void logGenericMessage(String message) {
        logChannel.sendMessage(message).queue();
    }

    public void logKick(String targetName, String staffName, String reason) {
        //TODO Update looks
        MessageCreateBuilder messageBuilder = new MessageCreateBuilder();
        messageBuilder.addContent("`" + staffName + "` kicked `" + targetName + "`\n " + reason);

        logChannel.sendMessage(messageBuilder.build()).queue();
    }

    public void logBan(String targetName, String staffName, String reason, int duration) {
        //TODO Update looks
        MessageCreateBuilder messageBuilder = new MessageCreateBuilder();
        messageBuilder.addContent("`" + staffName + "` kicked `" + targetName + "` duration `" + duration + "m`\n " + reason);

        logChannel.sendMessage(messageBuilder.build()).queue();
    }

    public static LogHandler getInstance() {
        if (instance == null) {
            instance = new LogHandler();
        }

        return instance;
    }
}
