package me.efco.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class UnbanCommand extends AbstractCommand {
    public UnbanCommand() {
        super(
                "unban",
                "Unban user from server G",
                new ArrayList<>(),
                List.of(
                        new OptionData(OptionType.STRING, "user", "User (ID) to unban from server", true)
                ),
                new ArrayList<>()
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();

        User user = event.getJDA().getUserById(event.getOption("user").getAsString()); //required
        if (user == null) {
            event.getHook().sendMessage("No user with that ID found").queue();
        } else {
            event.getGuild().unban(user).queue();
            event.getHook().sendMessage("User has successfully been unbanned").queue();
        }
    }
}
