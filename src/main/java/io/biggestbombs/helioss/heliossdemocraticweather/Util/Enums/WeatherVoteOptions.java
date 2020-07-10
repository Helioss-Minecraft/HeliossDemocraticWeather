package io.biggestbombs.helioss.heliossdemocraticweather.Util.Enums;

public enum WeatherVoteOptions {
    CLEAR("clear"),
    RAIN("rain");

    private final String value;

    WeatherVoteOptions(String voteValue) {
        this.value = voteValue;
    }

    public String getValue() {
        return this.value;
    }
}
