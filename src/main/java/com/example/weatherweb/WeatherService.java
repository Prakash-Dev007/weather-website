package com.example.weatherweb;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    public JsonNode getWeather(double lat, double lon) {
        String url = "https://api.openweathermap.org/data/2.5/weather"
                + "?lat=" + lat
                + "&lon=" + lon
                + "&appid=" + apiKey
                + "&units=metric";

        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public double[] getCoordinatesForCity(String cityName) {
        String url = "https://api.openweathermap.org/geo/1.0/direct?q=" + cityName + "&limit=1&appid=" + apiKey;

        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            if (root.isArray() && root.size() > 0) {
                double lat = root.get(0).path("lat").asDouble();
                double lon = root.get(0).path("lon").asDouble();
                return new double[]{lat, lon};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
