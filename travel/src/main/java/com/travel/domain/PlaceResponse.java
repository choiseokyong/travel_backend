package com.travel.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceResponse {
	@JsonProperty("results")
    private List<Place> results;

    @JsonProperty("status")
    private String status;

    public List<Place> getResults() {
        return results;
    }

    public String getStatus() {
        return status;
    }

    public static class Place {
        @JsonProperty("place_id")
        private String placeId;

        @JsonProperty("name")
        private String name;

        @JsonProperty("formatted_address")
        private String formattedAddress;
        
        @JsonProperty("geometry")
        private Geometry geometry;

        public String getPlaceId() {
            return placeId;
        }

        public String getName() {
            return name;
        }

        public String getFormattedAddress() {
            return formattedAddress;
        }
        
        public Geometry getGeometry() { return geometry; }
        
        public static class Geometry {
            @JsonProperty("location")
            private Location location;

            public Location getLocation() { return location; }

            public static class Location {
                @JsonProperty("lat")
                private Double lat;

                @JsonProperty("lng")
                private Double lng;

                public Double getLat() { return lat; }
                public Double getLng() { return lng; }
            }
        }
    }
}
