package com.uce.crudestudiantes.rickandmorty.controller;

import com.uce.crudestudiantes.rickandmorty.model.Episodio;
import com.uce.crudestudiantes.rickandmorty.model.Lugar;
import com.uce.crudestudiantes.rickandmorty.model.Personaje;
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

    // ---------- Para llenar el select 2 ----------

    @GetMapping("/lugares")
    public List<Lugar> listarLugares() {
        return service.listarLugares();
    }

    @GetMapping("/episodios")
    public List<Episodio> listarEpisodios() {
        return service.listarEpisodios();
    }

    // ---------- Endpoints anidados ----------

    @GetMapping("/lugares/{id}/residentes")
    public List<Personaje> residentesDeLugar(@PathVariable int id) {
        return service.residentesDeLugar(id);
    }

    @GetMapping("/episodios/{id}/personajes")
    public List<Personaje> personajesDeEpisodio(@PathVariable int id) {
        return service.personajesDeEpisodio(id);
    }

    // ---------- Buscador con debounce ----------

    @GetMapping("/personajes")
    public List<Personaje> buscarPersonajes(@RequestParam(defaultValue = "") String nombre) {
        return service.buscarPersonajes(nombre);
    }
}