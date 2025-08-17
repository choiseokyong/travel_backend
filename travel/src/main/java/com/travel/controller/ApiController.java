package com.travel.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.travel.domain.PlaceResponse;
import com.travel.service.ApiService;

@RestController
@RequestMapping("/api")
public class ApiController {
	private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/places")
    public ResponseEntity<List<PlaceResponse.Place>> getPlaces(@RequestParam String query) {
        List<PlaceResponse.Place> places = apiService.findPlaces(query);
        System.out.println("places size = " + places.size());
        places.forEach(p -> {
            System.out.println(p.getName() + " / " + p.getFormattedAddress() +
                               " / lat=" + p.getGeometry().getLocation().getLat() +
                               ", lng=" + p.getGeometry().getLocation().getLng());
        });
        return ResponseEntity.ok(places);
    }
}
