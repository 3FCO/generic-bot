package me.efco.commands;

import me.efco.GenericBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;

public class SlashCommandHandler extends ListenerAdapter {
    private static SlashCommandHandler instance;
    private HashMap<String, AbstractCommand> commands;

    private SlashCommandHandler() {
        initializeCommands();
    }

    private void initializeCommands() {
        commands = new HashMap<>();

        //Probably the easiest way of doing it without reflection
        insertNewCommand(new HelpCommand());
        insertNewCommand(new KickCommand());
        insertNewCommand(new BanCommand());
        insertNewCommand(new UnbanCommand());
        insertNewCommand(new MuteCommand());
        insertNewCommand(new UnmuteCommand());
        insertNewCommand(new WarnCommand());
        insertNewCommand(new GiveawayCommand());
    }

    public void insertNewCommand(AbstractCommand command) {
        commands.put(command.getName(), command);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!commands.containsKey(event.getName())) return;

        GenericBot.getApi().getCallbackPool().submit(() -> commands.get(event.getName()).execute(event));
    }

    public void updateCommands() {
        JDA api = GenericBot.getApi();
        /*
        commands.forEach((key, command) -> {
            api.upsertCommand(command.getName(), command.getDescription())
                    .addOptions(command.getOptions())
                    .addSubcommands(command.getSubcommands())
                    .queue();
        });*/

        //If we don't update per guild, commands can take up to 1 hour to update... So in development we do this
        Guild guild = GenericBot.getApi().getGuildById("969657573424918548");

        commands.forEach((key, command) -> {
            guild.upsertCommand(command.getName(), command.getDescription())
                    .addOptions(command.getOptions())
                    .addSubcommands(command.getSubcommands())
                    .queue();
        });
    }

    public HashMap<String, AbstractCommand> getCommands() {
        return commands;
    }

    public static SlashCommandHandler getInstance() {
        if (instance == null) {
            instance = new SlashCommandHandler();
        }

        return instance;
    }
}
