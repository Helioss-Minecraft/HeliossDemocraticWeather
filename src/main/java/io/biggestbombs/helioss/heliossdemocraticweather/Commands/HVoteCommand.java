package io.biggestbombs.helioss.heliossdemocraticweather.Commands;

import io.biggestbombs.helioss.heliossdemocraticweather.HeliossDemocraticWeather;
import io.biggestbombs.helioss.heliossdemocraticweather.Util.Vote.HeliossVoteOption;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import java.util.Optional;

public class HVoteCommand implements CommandExecutor {

    private final HeliossDemocraticWeather plugin;

    public HVoteCommand(HeliossDemocraticWeather plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(@Nonnull CommandSource source, @Nonnull CommandContext args) {

        if (!(source instanceof Player)) {
            source.sendMessage(Text.of("Only a player may run the hvote command"));
            return CommandResult.empty();
        }

        Player player = (Player) source;

        if (this.plugin.currentVote == null) {
            player.sendMessage(Text.of("There is currently no vote running!"));
            return CommandResult.empty();
        }

        Optional<String> vote = args.getOne("vote");
        if (!vote.isPresent()) {
            player.sendMessage(Text.of("Which option are you voting for? The options are:"));

            for (HeliossVoteOption option : this.plugin.currentVote.options) {
                source.sendMessage(Text.of(" - " + option.getName()));
            }

            return CommandResult.empty();
        }

        String voteOption = vote.get();
        this.plugin.currentVote.vote(player, voteOption);
        player.sendMessage(Text.of("Thank you for voting!"));

        return CommandResult.success();
    }
}
