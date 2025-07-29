package com.example.weatherweb;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;

@Service
public class WeatherAIService {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private WeatherService weatherService;

    public String askWeatherQuestion(String question, double lat, double lon) {
        JsonNode data = weatherService.getWeather(lat, lon);
        JsonNode current = data.get("current");

        double temp = current.get("temp").asDouble();
        int humidity = current.get("humidity").asInt();
        double windSpeed = current.get("wind_speed").asDouble();
        double rainChance = data.get("hourly").get(0).get("pop").asDouble() * 100;

        String sunrise = Instant.ofEpochSecond(current.get("sunrise").asLong())
                .atZone(ZoneId.systemDefault()).toLocalTime().toString();
        String sunset = Instant.ofEpochSecond(current.get("sunset").asLong())
                .atZone(ZoneId.systemDefault()).toLocalTime().toString();

        String context = String.format(
            "Weather details:\n" +
            "- Temperature: %.1f°C\n" +
            "- Humidity: %d%%\n" +
            "- Wind Speed: %.1f km/h\n" +
            "- Rain Chance (next hour): %.0f%%\n" +
            "- Sunrise: %s\n" +
            "- Sunset: %s\n\n" +
            "User question: %s\n\n" +
            "Please provide a helpful weather answer in simple language.",
            temp, humidity, windSpeed, rainChance, sunrise, sunset, question);

        try {
            return chatClient.call(context);
        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, I couldn't get the weather info right now.";
        }
    }
}
