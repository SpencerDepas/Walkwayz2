package com.clearfaun.spencerdepas.walkwayz.Model;

import java.util.List;
import java.util.Map;

/**
 * Created by SpencerDepas on 5/13/17.
 */

public class UserLocation {
    //poland
    //private LatLng location = new LatLng(54.351210, 18.646448);

    private double latitude = 54.351210;
    private double longitude = 18.646448;
    private List<String> categories;
    private Map<String, String> metadata;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
