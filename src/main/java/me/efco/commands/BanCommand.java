package me.efco.commands;

import me.efco.data.DatabaseConnection;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BanCommand extends AbstractCommand {
    public BanCommand() {
        super(
                "ban",
                "Ban user from server G",
                new ArrayList<>(),
                List.of(
                        new OptionData(OptionType.USER, "user", "User to ban from server", true),
                        new OptionData(OptionType.STRING, "reason", "Reason for kicking user", true),
                        new OptionData(OptionType.INTEGER, "duration", "Ban duration in minutes", true)
                ),
                new ArrayList<>()
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();

        User user = event.getOption("user").getAsUser(); //required
        String reason = event.getOption("reason").getAsString(); //required
        int duration = event.getOption("duration").getAsInt(); //required
        User admin = event.getUser();

        DatabaseConnection.getInstance().userBanned(user.getId(), user.getName(), admin.getId(), admin.getName(), reason, duration);
        event.getGuild().ban(user, duration, TimeUnit.SECONDS).queue();
        event.getHook().sendMessage("User has successfully been banned").queue();
    }
}
