package com.travel.client;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.domain.PlaceResponse;

@Component
public class PlaceClient {
	@Value("${google.maps.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public PlaceResponse searchPlaces(String query) {
    	 try {
    		 System.out.println("query : " + query);
    		// 디버깅용으로 훨씬 안전
	        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8).replace("+","%20");
	        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="
	                     + encodedQuery + "&key=" + apiKey;
	
	     // 🔹 호출 URL 로그
	        System.out.println("▶ Google API URL: " + url);
	
	        // 🔹 먼저 String으로 받아서 확인
	        String rawJson = restTemplate.getForObject(url, String.class);
	        System.out.println("▶ Raw JSON from Google: " + rawJson);
	        
	     // 🔹 ObjectMapper로 DTO 변환
	        ObjectMapper mapper = new ObjectMapper();
	        PlaceResponse response = mapper.readValue(rawJson, PlaceResponse.class);
	        
	        return response;
//	        Spring이 자동으로 JSON → DTO 변환
//	        return restTemplate.getForObject(url, PlaceResponse.class) 
    	 } catch (Exception e) {
             e.printStackTrace();
             throw new RuntimeException("Google Places API 호출 실패");
         }

    }
}
