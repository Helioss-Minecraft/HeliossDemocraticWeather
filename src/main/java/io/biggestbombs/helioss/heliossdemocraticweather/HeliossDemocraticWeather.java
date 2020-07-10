package io.biggestbombs.helioss.heliossdemocraticweather;

import com.google.inject.Inject;

import io.biggestbombs.helioss.heliossdemocraticweather.Commands.HVoteCommand;
import io.biggestbombs.helioss.heliossdemocraticweather.Events.ChangeWorldWeatherHandler;
import io.biggestbombs.helioss.heliossdemocraticweather.Util.WeatherVote;

import org.slf4j.Logger;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

@Plugin(
        id = "helioss-democratic-weather",
        name = "Helioss Democratic Weather",
        description = "A plugin to vote for changing rain to sun"
)
public class HeliossDemocraticWeather {

    public static HeliossDemocraticWeather plugin_instance;

    public WeatherVote currentVote;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        HeliossDemocraticWeather.plugin_instance = this;

        // Register events
        Sponge.getEventManager().registerListeners(this, new ChangeWorldWeatherHandler(this));

        // Register commands
        CommandSpec hVoteCommand = CommandSpec.builder()
                .description(Text.of("Vote for something!"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("vote")))
                )
                .executor(new HVoteCommand(this))
                .build();

        Sponge.getCommandManager().register(this, hVoteCommand, "hvote");
    }
}
