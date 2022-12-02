package me.efco;

import me.efco.body.GiveawayBody;
import me.efco.data.DatabaseConnection;
import me.efco.data.PropertiesLoader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;

import java.time.Instant;
import java.util.*;

public class GiveawayHandler {
    private static GiveawayHandler instance;
    private HashMap<Long, GiveawayBody> activeGiveaways;

    private GiveawayHandler() {
        this.activeGiveaways = new HashMap<>();
    }

    public void newGiveaway(GiveawayBody giveaway) {
        activeGiveaways.put(giveaway.id(), giveaway);

        DatabaseConnection.getInstance().insertNewGiveaway(giveaway.id(), giveaway.reward(), giveaway.end(), giveaway.winnersAmount());

        new Timer().schedule(new TimerTask() {
                                 @Override
                                 public void run() {
                                     endGiveaway(giveaway.id());
                                     cancel();
                                 }
                             },
                new Date(giveaway.end()));
    }

    private void endGiveaway(long id) {
        if (!activeGiveaways.containsKey(id)) return;

        GiveawayBody giveaway = activeGiveaways.get(id);
        Random random = new Random();

        ArrayList<Long> winners = new ArrayList<>();
        for (int i = 0; i < giveaway.winnersAmount(); i++) {
            if (giveaway.entries().size() == 0) {
                break;
            }
            int randomEntry = random.nextInt(giveaway.entries().size());
            winners.add(giveaway.entries().get(randomEntry));
            giveaway.entries().remove(randomEntry);
        }

        TextChannel textChannel = GenericBot.getApi().getTextChannelById(PropertiesLoader.getInstance().getProperty("giveaway_channel"));
        textChannel.retrieveMessageById(id).queue((message -> {
            message.editMessageComponents(new LayoutComponent[]{}).queue();
            DatabaseConnection.getInstance().endGiveaway(id, winners);

            StringBuilder stringBuilder = new StringBuilder();
            for (long w : winners) {
                stringBuilder.append("<@").append(w).append("> ");
            }

            MessageEmbed messageEmbed =  message.getEmbeds().get(0);
            MessageEmbed newEmbed =  new EmbedBuilder()
                    .addField(messageEmbed.getFields().get(0))
                    .addField(messageEmbed.getFields().get(1))
                    .addField("Winners", stringBuilder.toString(), false)
                    .build();
            message.editMessageEmbeds(newEmbed).queue();
        }));
    }

    public void joinGiveaway(long id, ButtonInteractionEvent event) {
        if (!activeGiveaways.containsKey(id)) return;
        User user = event.getUser();

        event.deferReply().setEphemeral(true).queue();

        GiveawayBody giveaway = activeGiveaways.get(id);
        if (giveaway.entries().contains(user.getId())) {
            event.getHook().sendMessage("You already entered this giveaway").queue();
            return;
        }

        giveaway.entries().add(user.getIdLong());
        DatabaseConnection.getInstance().userJoinGiveaway(user.getIdLong(), id);

        TextChannel textChannel = event.getGuild().getTextChannelById(PropertiesLoader.getInstance().getProperty("giveaway_channel"));
        textChannel.retrieveMessageById(id).queue((message) -> {
            MessageEmbed messageEmbed =  message.getEmbeds().get(0);
            MessageEmbed newEmbed =  new EmbedBuilder()
                    .addField(messageEmbed.getFields().get(0))
                    .addField("Entries", giveaway.entries().size() + "", false)
                    .addField(messageEmbed.getFields().get(2))
                    .build();
            message.editMessageEmbeds(newEmbed).queue();

            event.getHook().sendMessage("You have entered this giveaway").queue();
        });
    }

    public void loadGiveaways() {
        this.activeGiveaways = DatabaseConnection.getInstance().getActiveGiveaways();
        long time = Instant.now().toEpochMilli();
        activeGiveaways.forEach((k,v) -> {
            if (v.end() <= time) {
                endGiveaway(v.id());
                return;
            }

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    endGiveaway(v.id());
                    cancel();
                }
            }, new Date(v.end()));
        });
    }

    public HashMap<Long, GiveawayBody> getActiveGiveaways() {
        return activeGiveaways;
    }

    public static GiveawayHandler getInstance() {
        if (instance == null) {
            instance = new GiveawayHandler();
        }

        return instance;
    }
}
