package io.biggestbombs.helioss.heliossdemocraticweather;

import io.biggestbombs.helioss.heliossdemocraticweather.Commands.HVoteCommand;
import io.biggestbombs.helioss.heliossdemocraticweather.Events.ChangeWorldWeatherHandler;
import io.biggestbombs.helioss.heliossdemocraticweather.Util.SpongeUtils;
import io.biggestbombs.helioss.heliossdemocraticweather.Util.Vote.Base.HeliossVote;
import io.biggestbombs.helioss.heliossdemocraticweather.Util.Vote.TimeOfDayVote;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.world.LoadWorldEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Plugin(
        id = "helioss-democratic-weather",
        name = "Helioss Democratic Weather",
        description = "A plugin to vote for changing rain to sun"
)
public class HeliossDemocraticWeather {

    public static HeliossDemocraticWeather plugin_instance;

    private final long TIME_DAY = 1000;
    private final long TIME_NIGHT = 14000;

    public HeliossVote currentVote;
    public HashMap<UUID, Boolean> hasSkippedNight = new HashMap<>();

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
                        UUID worldId = defaultWorld.getUniqueId();
                        long currentDayTime = SpongeUtils.normalizeWorldTime(worldTime);
                        boolean hasSkippedNight = this.hasSkippedNight.get(worldId);

                        if(currentDayTime >= TIME_NIGHT && !hasSkippedNight) {
                            this.currentVote = new TimeOfDayVote(defaultWorld);
                        } else if(currentDayTime >= TIME_DAY && currentDayTime < TIME_NIGHT) {
                            this.hasSkippedNight.put(worldId, false);
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

    @Listener
    public void onWorldLoad(LoadWorldEvent event) {
        this.hasSkippedNight.put(event.getTargetWorld().getUniqueId(), false);
    }
}
