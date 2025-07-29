package com.example.weatherweb;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather")
    public String showWeather(@RequestParam(value = "city", defaultValue = "Delhi") String city, Model model) {
        double[] coords = weatherService.getCoordinatesForCity(city);

        if (coords == null) {
            model.addAttribute("error", "City not found.");
            return "weather";
        }

        JsonNode data = weatherService.getWeather(coords[0], coords[1]);

        if (data == null || data.get("main") == null) {
            model.addAttribute("error", "Unable to fetch weather data.");
            return "weather";
        }

        JsonNode main = data.path("main");
        model.addAttribute("temperature", main.path("temp").asDouble());
        model.addAttribute("humidity", main.path("humidity").asInt());

        JsonNode wind = data.path("wind");
        model.addAttribute("windSpeed", wind.path("speed").asDouble());
        model.addAttribute("windDeg", wind.path("deg").asInt());

        JsonNode weatherArray = data.path("weather");
        if (weatherArray.isArray() && weatherArray.size() > 0) {
            model.addAttribute("description", weatherArray.get(0).path("description").asText());
        }
        JsonNode rain = data.path("rain");
        if (rain != null && rain.has("1h")) {
            model.addAttribute("rainChance", rain.path("1h").asDouble());
        } else {
            model.addAttribute("rainChance", 0.0); // no rain info available
        }

        model.addAttribute("city", city);
        return "weather"; // maps to weather.jsp or weather.html
    }
}
