package com.uce.crudestudiantes.rickandmorty.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Personaje(
        int id,
        String name,
        String status,
        String species,
        String gender,
        String image,
        List<String> episode
) {}