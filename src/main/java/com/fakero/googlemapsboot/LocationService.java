/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fakero.googlemapsboot;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import java.io.IOException;
import org.springframework.stereotype.Service;

/**
 *
 * @author fakero
 */
@Service
public class LocationService {

    private static final String APIKEY = "Google Api Key";
    
    private static GeoApiContext context;

    private static GeoApiContext getContext(){
        if (context == null) {
            context = new GeoApiContext.Builder().apiKey(APIKEY).build();
        }
        return context;
    }
    
    public Location getLocationByTownName(String townName){
        GeocodingResult[] results;
        LatLng geocode;
        Location loc = null;
        try {
            results = GeocodingApi.geocode(getContext(), townName).await();
            geocode = results[0].geometry.location;
            loc = new Location(townName, geocode.lat, geocode.lng);
        } catch (ApiException | IOException | InterruptedException e){
            
        }
        return loc;
    }
}
