package com.travel.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.travel.client.PlaceClient;
import com.travel.domain.PlaceResponse;

@Service
public class ApiService {
	private final PlaceClient placeClient;

    public ApiService(PlaceClient placeClient) {
        this.placeClient = placeClient;
    }

    public List<PlaceResponse.Place> findPlaces(String query) {
        PlaceResponse response = placeClient.searchPlaces(query);

        if (response == null) {
            throw new RuntimeException("Google API 응답이 없습니다.");
        }

        switch (response.getStatus()) {
            case "OK":
                return response.getResults();
            case "ZERO_RESULTS":
                return Collections.emptyList();
            case "OVER_QUERY_LIMIT":
                throw new RuntimeException("Google Places API 호출 한도 초과");
            default:
                throw new RuntimeException("Google API 에러: " + response.getStatus());
        }
    }
}
