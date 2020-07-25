package io.biggestbombs.helioss.heliossdemocraticweather.Util.Vote;

import io.biggestbombs.helioss.heliossdemocraticweather.HeliossDemocraticWeather;
import io.biggestbombs.helioss.heliossdemocraticweather.Util.Enums.TimeOfDayVoteOptions;
import io.biggestbombs.helioss.heliossdemocraticweather.Util.SpongeUtils;
import io.biggestbombs.helioss.heliossdemocraticweather.Util.Vote.Base.HeliossVote;
import io.biggestbombs.helioss.heliossdemocraticweather.Util.Vote.Base.HeliossVoteOption;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.stream.Stream;

public class TimeOfDayVote extends HeliossVote {

    private final WorldProperties targetedWorld;

    public TimeOfDayVote(WorldProperties targetWorld) {

        super();

        this.targetedWorld = targetWorld;
        this.prefix = Text.of(TextStyles.BOLD, TextColors.DARK_PURPLE, "[DAYTIME]");

        this.addOption(new HeliossVoteOption("Skipping night", TimeOfDayVoteOptions.DAY.getValue()));
        this.addOption(new HeliossVoteOption("Keeping night", TimeOfDayVoteOptions.NIGHT.getValue()));
        this.startVote(
                Text.of(TextColors.GOLD, "Night has fallen upon " + targetWorld.getWorldName() + ", should we skip the night time?"),
                60
        );
    }

    @Override
    public void executeVoteOutcome(HeliossVoteOption winner) {

        TimeOfDayVoteOptions voteOption = TimeOfDayVoteOptions.get(winner.getName());

        if(voteOption == null) {
            voteOption = TimeOfDayVoteOptions.NIGHT;
        }

        if (voteOption == TimeOfDayVoteOptions.DAY) {

            long currentWorldTime = this.targetedWorld.getWorldTime();
            long currentDayCount = SpongeUtils.determineWorldDays(currentWorldTime);
            long nextDay = currentDayCount + 1;

            // Figure out the morning of the next day
            long nextDayMorning = nextDay * 24000L + 1000L;

            this.targetedWorld.setWorldTime(nextDayMorning);
        }

        HeliossDemocraticWeather.plugin_instance.hasSkippedNight.put(this.targetedWorld.getUniqueId(), true);
    }

    @Override
    public HeliossVoteOption getMajority() {

        HeliossVoteOption nightOption = this.options.stream()
                .filter(option -> option.getName().equals(TimeOfDayVoteOptions.NIGHT.getValue()))
                .findFirst()
                .orElse(null);

        if(nightOption == null) {
            return null;
        }

        if(nightOption.getVotes() > 0) {
            return nightOption;
        }

        if (this.options.stream().allMatch(x -> x.getVotes() == 0)) {
            return nightOption;
        }

        return super.getMajority();
    }
}
