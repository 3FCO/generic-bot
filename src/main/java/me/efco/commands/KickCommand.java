package me.efco.commands;

import me.efco.data.DatabaseConnection;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class KickCommand extends AbstractCommand {
    public KickCommand() {
        super(
                "kick",
                "Kick user from server G",
                new ArrayList<>(),
                List.of(
                        new OptionData(OptionType.USER, "user", "User to kick from server", true),
                        new OptionData(OptionType.STRING, "reason", "Reason for kicking user", false)
                ),
                new ArrayList<>()
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();

        User user = event.getOption("user").getAsUser(); //required
        String reason = event.getOption("reason").getAsString(); //required
        User admin = event.getUser();

        DatabaseConnection.getInstance().userKicked(user.getId(), user.getName(), admin.getId(), admin.getName(), reason);
        event.getGuild().kick(user).queue();
        event.getHook().sendMessage("User has successfully been banned").queue();
    }
}
