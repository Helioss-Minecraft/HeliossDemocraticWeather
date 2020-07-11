package io.biggestbombs.helioss.heliossdemocraticweather.Util.Enums;

public enum TimeOfDayVoteOptions {
    DAY("day"),
    NIGHT("night");

    private final String value;

    TimeOfDayVoteOptions(String voteValue) {
        this.value = voteValue;
    }

    public String getValue() {
        return this.value;
    }

    public static TimeOfDayVoteOptions get(String value) {
        switch(value) {
            case "day":
                return TimeOfDayVoteOptions.DAY;

            case "night":
                return TimeOfDayVoteOptions.NIGHT;

            default:
                return null;
        }
    }
}
