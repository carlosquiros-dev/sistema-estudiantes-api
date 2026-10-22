package com.uce.crudestudiantes.rickandmorty.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

// An episode from /api/episode
@JsonIgnoreProperties(ignoreUnknown = true)
public record RmEpisode(
        int id,
        String name,
        @JsonProperty("air_date") String airDate,
        String episode,
        List<String> characters   // URLs of the characters in this episode
) {}
