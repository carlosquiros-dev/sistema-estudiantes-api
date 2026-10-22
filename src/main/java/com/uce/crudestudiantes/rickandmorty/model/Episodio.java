package com.uce.crudestudiantes.rickandmorty.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Episodio(
        int id,
        String name,
        @JsonProperty("air_date") String airDate,
        String episode,
        List<String> characters
) {}