package com.uce.crudestudiantes.rickandmorty.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

// A location from /api/location
@JsonIgnoreProperties(ignoreUnknown = true)
public record RmLocation(
        int id,
        String name,
        String type,
        String dimension,
        List<String> residents   // URLs of the characters who live here
) {}
