package com.uce.crudestudiantes.rickandmorty.service;

import com.uce.crudestudiantes.rickandmorty.model.ApiResponse;
import com.uce.crudestudiantes.rickandmorty.model.RmCharacter;
import com.uce.crudestudiantes.rickandmorty.model.RmEpisode;
import com.uce.crudestudiantes.rickandmorty.model.RmLocation;
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

    // ===================== LISTS (to fill the second select) =====================

    public List<RmLocation> getLocations() {
        return fetchAllPages("/location", new ParameterizedTypeReference<ApiResponse<RmLocation>>() {});
    }

    public List<RmEpisode> getEpisodes() {
        return fetchAllPages("/episode", new ParameterizedTypeReference<ApiResponse<RmEpisode>>() {});
    }

    // ===================== SEARCH (for the debounced search box) =====================

    public List<RmCharacter> searchCharacters(String name) {
        try {
            ApiResponse<RmCharacter> response = restClient.get()
                    .uri("/character/?name={name}", name)
                    .retrieve()
                    .body(new ParameterizedTypeReference<ApiResponse<RmCharacter>>() {});
            return response.results();
        } catch (HttpClientErrorException.NotFound e) {
            return List.of(); // the API answers 404 when nobody matches
        }
    }

    // ===================== NESTED (what appears after choosing in the second select) =====================

    public List<RmCharacter> getLocationResidents(int locationId) {
        RmLocation location = restClient.get()
                .uri("/location/{id}", locationId)
                .retrieve()
                .body(RmLocation.class);
        return charactersFromUrls(location.residents());
    }

    public List<RmCharacter> getEpisodeCharacters(int episodeId) {
        RmEpisode episode = restClient.get()
                .uri("/episode/{id}", episodeId)
                .retrieve()
                .body(RmEpisode.class);
        return charactersFromUrls(episode.characters());
    }

    // ===================== HELPER METHODS (private) =====================

    private <T> List<T> fetchAllPages(String path, ParameterizedTypeReference<ApiResponse<T>> type) {
        List<T> all = new ArrayList<>();
        int page = 1;
        int totalPages;
        do {
            ApiResponse<T> response = restClient.get()
                    .uri(path + "?page={p}", page)
                    .retrieve()
                    .body(type);
            all.addAll(response.results());
            totalPages = response.info().pages();
            page++;
        } while (page <= totalPages);
        return all;
    }

    private List<RmCharacter> charactersFromUrls(List<String> urls) {
        if (urls.isEmpty()) {
            return List.of();
        }
        // "https://rickandmortyapi.com/api/character/38" -> "38"
        String ids = urls.stream()
                .map(url -> url.substring(url.lastIndexOf('/') + 1))
                .collect(Collectors.joining(","));

        if (urls.size() == 1) {
            // with a single id the API returns one object (not a list)
            RmCharacter single = restClient.get().uri("/character/{ids}", ids).retrieve().body(RmCharacter.class);
            return List.of(single);
        }
        RmCharacter[] many = restClient.get().uri("/character/{ids}", ids).retrieve().body(RmCharacter[].class);
        return List.of(many);
    }
}
