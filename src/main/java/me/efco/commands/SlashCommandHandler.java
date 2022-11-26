package me.efco.commands;

import me.efco.GenericBot;
import net.dv8tion.jda.api.JDA;
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
        commands.forEach((key, command) -> {
            api.upsertCommand(command.getName(), command.getDescription())
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
