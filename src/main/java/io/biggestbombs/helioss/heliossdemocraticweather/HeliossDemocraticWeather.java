package io.biggestbombs.helioss.heliossdemocraticweather;

import io.biggestbombs.helioss.heliossdemocraticweather.Commands.HVoteCommand;
import io.biggestbombs.helioss.heliossdemocraticweather.Events.ChangeWorldWeatherHandler;
import io.biggestbombs.helioss.heliossdemocraticweather.Util.Vote.Base.HeliossVote;
import io.biggestbombs.helioss.heliossdemocraticweather.Util.Vote.TimeOfDayVote;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

@Plugin(
        id = "helioss-democratic-weather",
        name = "Helioss Democratic Weather",
        description = "A plugin to vote for changing rain to sun"
)
public class HeliossDemocraticWeather {

    public static HeliossDemocraticWeather plugin_instance;

    public HeliossVote currentVote;
    public Task timeTask;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        HeliossDemocraticWeather.plugin_instance = this;

        // Register important stuff.
        Task.builder()
                .intervalTicks(1)
                .execute(task -> {

                    if (this.currentVote != null) {
                        return;
                    }

                    Optional<WorldProperties> potentialDefaultWorld = Sponge.getGame().getServer().getDefaultWorld();

                    if(potentialDefaultWorld.isPresent()) {
                        WorldProperties defaultWorld = potentialDefaultWorld.get();

                        long worldTime = defaultWorld.getWorldTime();

                        if(worldTime > 14000) {
                            this.currentVote = new TimeOfDayVote(defaultWorld);
                        }
                    }
                })
                .submit(this);


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
