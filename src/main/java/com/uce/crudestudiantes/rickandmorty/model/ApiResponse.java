package com.uce.crudestudiantes.rickandmorty.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

// The "box" the API returns for lists: { "info": {...}, "results": [...] }
@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiResponse<T>(
        PageInfo info,
        List<T> results
) {}
