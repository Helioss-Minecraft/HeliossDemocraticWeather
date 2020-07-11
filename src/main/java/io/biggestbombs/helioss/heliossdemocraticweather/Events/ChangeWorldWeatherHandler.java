package io.biggestbombs.helioss.heliossdemocraticweather.Events;

import io.biggestbombs.helioss.heliossdemocraticweather.HeliossDemocraticWeather;
import io.biggestbombs.helioss.heliossdemocraticweather.Util.Vote.WeatherVote;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.ChangeWorldWeatherEvent;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.weather.Weathers;

public class ChangeWorldWeatherHandler {

    final HeliossDemocraticWeather plugin;

    public ChangeWorldWeatherHandler(HeliossDemocraticWeather plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void onChangeWorldWeather(ChangeWorldWeatherEvent event) {

        if(event.getWeather() != Weathers.RAIN) {
            return;
        }

        // Already a vote happening.
        if (this.plugin.currentVote != null) {
            return;
        }

        World targetedWorld = event.getTargetWorld();

        this.plugin.currentVote = new WeatherVote(targetedWorld);
    }
}
