package com.uce.crudestudiantes.rickandmorty.service;

import com.uce.crudestudiantes.rickandmorty.model.Episodio;
import com.uce.crudestudiantes.rickandmorty.model.Lugar;
import com.uce.crudestudiantes.rickandmorty.model.Personaje;
import com.uce.crudestudiantes.rickandmorty.model.RespuestaApi;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RickAndMortyService {

    private final RestClient restClient = RestClient.create("https://rickandmortyapi.com/api");

    // ===================== LISTAS (para llenar el select 2) =====================

    public List<Lugar> listarLugares() {
        return traerTodasLasPaginas("/location", new ParameterizedTypeReference<RespuestaApi<Lugar>>() {});
    }

    public List<Episodio> listarEpisodios() {
        return traerTodasLasPaginas("/episode", new ParameterizedTypeReference<RespuestaApi<Episodio>>() {});
    }

    // ===================== BÚSQUEDA (para el buscador con debounce) =====================

    public List<Personaje> buscarPersonajes(String nombre) {
        try {
            RespuestaApi<Personaje> respuesta = restClient.get()
                    .uri("/character/?name={nombre}", nombre)
                    .retrieve()
                    .body(new ParameterizedTypeReference<RespuestaApi<Personaje>>() {});
            return respuesta.results();
        } catch (HttpClientErrorException.NotFound e) {
            return List.of(); // la API responde 404 cuando no encuentra a nadie
        }
    }

    // ===================== ANIDADOS (lo que sale al elegir en el select 2) =====================

    public List<Personaje> residentesDeLugar(int idLugar) {
        Lugar lugar = restClient.get()
                .uri("/location/{id}", idLugar)
                .retrieve()
                .body(Lugar.class);
        return personajesDesdeUrls(lugar.residents());
    }

    public List<Personaje> personajesDeEpisodio(int idEpisodio) {
        Episodio episodio = restClient.get()
                .uri("/episode/{id}", idEpisodio)
                .retrieve()
                .body(Episodio.class);
        return personajesDesdeUrls(episodio.characters());
    }

    // ===================== MÉTODOS DE AYUDA (privados) =====================

    private <T> List<T> traerTodasLasPaginas(String ruta, ParameterizedTypeReference<RespuestaApi<T>> tipo) {
        List<T> todos = new ArrayList<>();
        int pagina = 1;
        int totalPaginas;
        do {
            RespuestaApi<T> respuesta = restClient.get()
                    .uri(ruta + "?page={p}", pagina)
                    .retrieve()
                    .body(tipo);
            todos.addAll(respuesta.results());
            totalPaginas = respuesta.info().pages();
            pagina++;
        } while (pagina <= totalPaginas);
        return todos;
    }

    private List<Personaje> personajesDesdeUrls(List<String> urls) {
        if (urls.isEmpty()) {
            return List.of();
        }
        // "https://rickandmortyapi.com/api/character/38" -> "38"
        String ids = urls.stream()
                .map(url -> url.substring(url.lastIndexOf('/') + 1))
                .collect(Collectors.joining(","));

        if (urls.size() == 1) {
            // con un solo id, la API devuelve un objeto (no una lista)
            Personaje uno = restClient.get().uri("/character/{ids}", ids).retrieve().body(Personaje.class);
            return List.of(uno);
        }
        Personaje[] varios = restClient.get().uri("/character/{ids}", ids).retrieve().body(Personaje[].class);
        return List.of(varios);
    }
}