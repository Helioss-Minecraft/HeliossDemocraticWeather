package io.biggestbombs.helioss.heliossdemocraticweather.Util.Vote;

import io.biggestbombs.helioss.heliossdemocraticweather.Util.Enums.WeatherVoteOptions;
import io.biggestbombs.helioss.heliossdemocraticweather.Util.Vote.Base.HeliossVote;
import io.biggestbombs.helioss.heliossdemocraticweather.Util.Vote.Base.HeliossVoteOption;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.weather.Weathers;

public class WeatherVote extends HeliossVote {

    private final World targetedWorld;

    public WeatherVote(World targetWorld) {

        super();

        this.targetedWorld = targetWorld;
        this.prefix = Text.of(TextStyles.BOLD, TextColors.DARK_PURPLE, "[WEATHER]");

        this.addOption(new HeliossVoteOption("Clear Skies", WeatherVoteOptions.CLEAR.getValue()));
        this.addOption(new HeliossVoteOption("Rain", WeatherVoteOptions.RAIN.getValue()));
        this.startVote(
                Text.of(TextColors.GOLD, "Rain has started in " + targetWorld.getName() + ", do we want clear skies?"),
                60
        );
    }

    @Override
    public void executeVoteOutcome(HeliossVoteOption winner) {
        if(winner != null) {
            if (winner.getName().equals(WeatherVoteOptions.CLEAR.getValue())) {
                targetedWorld.setWeather(Weathers.CLEAR);
            }
        }
    }
}
