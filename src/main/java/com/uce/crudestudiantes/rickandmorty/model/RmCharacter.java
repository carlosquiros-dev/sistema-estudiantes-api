package com.uce.crudestudiantes.rickandmorty.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

// A character from /api/character
// Named RmCharacter (Rick and Morty Character) to avoid clashing with java.lang.Character
@JsonIgnoreProperties(ignoreUnknown = true)
public record RmCharacter(
        int id,
        String name,
        String status,
        String species,
        String gender,
        String image,
        List<String> episode   // URLs of the episodes this character appears in
) {}
