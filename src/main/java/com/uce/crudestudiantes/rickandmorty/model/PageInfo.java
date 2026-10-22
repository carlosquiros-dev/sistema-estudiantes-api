package com.uce.crudestudiantes.rickandmorty.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Pagination data: the API returns 20 items per page
@JsonIgnoreProperties(ignoreUnknown = true)
public record PageInfo(
        int count,
        int pages,
        String next,
        String prev
) {}
