package com.uce.crudestudiantes.rickandmorty.controller;

import com.uce.crudestudiantes.rickandmorty.model.RmCharacter;
import com.uce.crudestudiantes.rickandmorty.model.RmEpisode;
import com.uce.crudestudiantes.rickandmorty.model.RmLocation;
import com.uce.crudestudiantes.rickandmorty.service.RickAndMortyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rickandmorty")
public class RickAndMortyController {

    private final RickAndMortyService service;

    public RickAndMortyController(RickAndMortyService service) {
        this.service = service;
    }

    // ---------- To fill the second select ----------

    @GetMapping("/locations")
    public List<RmLocation> getLocations() {
        return service.getLocations();
    }

    @GetMapping("/episodes")
    public List<RmEpisode> getEpisodes() {
        return service.getEpisodes();
    }

    // ---------- Nested endpoints ----------

    @GetMapping("/locations/{id}/residents")
    public List<RmCharacter> getLocationResidents(@PathVariable int id) {
        return service.getLocationResidents(id);
    }

    @GetMapping("/episodes/{id}/characters")
    public List<RmCharacter> getEpisodeCharacters(@PathVariable int id) {
        return service.getEpisodeCharacters(id);
    }

    // ---------- Debounced search ----------

    @GetMapping("/characters")
    public List<RmCharacter> searchCharacters(@RequestParam(defaultValue = "") String name) {
        return service.searchCharacters(name);
    }
}
