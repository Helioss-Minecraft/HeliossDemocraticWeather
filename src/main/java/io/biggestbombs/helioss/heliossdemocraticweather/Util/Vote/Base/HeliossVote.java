package io.biggestbombs.helioss.heliossdemocraticweather.Util.Vote.Base;

import io.biggestbombs.helioss.heliossdemocraticweather.HeliossDemocraticWeather;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *  A class to handle votes of any kind, including automatic chat messages.
 */
public abstract class HeliossVote {

    private final MessageChannel broadcastChannel;

    public Text prefix = Text.of(TextStyles.BOLD, TextColors.DARK_PURPLE, "[Vote]");
    public ArrayList<HeliossVoteOption> options = new ArrayList<>();

    public HeliossVote() {
        this.broadcastChannel = Sponge.getServer().getBroadcastChannel();
    }

    public void startVote(Text question, long timeInSeconds) {

        this.broadcastChannel.send(buildVoteMessage(question));

        for (HeliossVoteOption option : options) {
            Text voteText = Text.builder()
                    .append(Text.of("  - Vote for "))
                    .append(Text.of(option.voteName))
                    .onHover(TextActions.showText(Text.of(TextColors.GREEN, "Click me to vote")))
                    .onClick(TextActions.runCommand("/hvote " + option.getName()))
                    .build();

            this.broadcastChannel.send(voteText);
        }

        Task.builder()
                .delay(timeInSeconds, TimeUnit.SECONDS)
                .execute(task -> {
                    HeliossVoteOption winner = this.getMajority();

                    this.executeVoteOutcome(winner);

                    Text.Builder messageBuilder = Text.builder()
                            .append(prefix)
                            .append(Text.of(" "));

                    if (winner != null) {
                        messageBuilder.append(Text.of(String.format("The votes are in! %1$s has won with %2$s votes.", winner.voteName, winner.getVotes())));
                    } else {
                        messageBuilder.append(Text.of("There was no winner of the vote!"));
                    }

                    this.broadcastChannel.send(messageBuilder.build());
                    HeliossDemocraticWeather.plugin_instance.currentVote = null;
                })
                .submit(HeliossDemocraticWeather.plugin_instance);
    }

    public void addOption(HeliossVoteOption newOption) {
        this.options.add(newOption);
    }

    public HeliossVoteOption getMajority() {
        return this.options.stream()
                .max(Comparator.comparing(HeliossVoteOption::getVotes))
                .orElse(null);
    }

    public void vote(Player player, String votedOption) {

        UUID playerId = player.getUniqueId();
        if(this.options.stream().anyMatch(x -> x.hasVoted(playerId))) {
            player.sendMessage(buildVoteMessage(Text.of("You've already voted in this vote!")));
            return;
        }

        Optional<HeliossVoteOption> possibleOption = this.options.stream()
                .filter(x -> x.getName().equals(votedOption))
                .findFirst();

        possibleOption.ifPresent(heliossVoteOption -> heliossVoteOption.vote(playerId));
        player.sendMessage(buildVoteMessage(Text.of("Thank you for voting!")));
    }


    public abstract void executeVoteOutcome(HeliossVoteOption winner);

    /* Util */
    private Text buildVoteMessage(Text message) {
        return Text.builder()
                .append(prefix)
                .append(Text.of(" "))
                .append(message)
                .build();
    }

}

