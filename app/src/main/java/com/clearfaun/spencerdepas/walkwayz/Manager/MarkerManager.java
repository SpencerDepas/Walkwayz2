package com.clearfaun.spencerdepas.walkwayz.Manager;

import com.backendless.BackendlessCollection;
import com.backendless.geo.GeoPoint;
import com.clearfaun.spencerdepas.walkwayz.Activity.MapsActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by SpencerDepas on 7/16/17.
 */

public class MarkerManager {

    private static MarkerManager sMarkerManager;
    private BackendlessCollection<GeoPoint> response;
    private List<GeoPoint> markerGeoPoints;
    private DialogPopupListener dialogPopupListener;
    private Hashtable<String, Marker> markerHashTable;

    private MarkerManager() {
    }

    public Hashtable<String, Marker> getMarkerHashTable() {
        if (markerHashTable == null) {
            markerHashTable = new Hashtable<String, Marker>();
        }
        return markerHashTable;
    }

    public void init(DialogPopupListener dialogPopupListener){
        this.dialogPopupListener = dialogPopupListener;
    }

    public static synchronized MarkerManager getInstance() {
        if (sMarkerManager == null) {
            sMarkerManager = new MarkerManager();
        }
        return sMarkerManager;
    }

    public void addMarkerToMap(List<GeoPoint> markerInfo) {
        this.markerGeoPoints = markerInfo;

        for(int i = 0 ; i < markerInfo.size(); i ++){

            makeNewMarker(markerInfo.get(i));

        }
    }

    public void addMarkerToMap(GeoPoint markerInfo) {
        makeNewMarker(markerInfo);
    }

    private boolean pinandGeoPointMatch(Marker marker, GeoPoint geoPoint){
        return marker.getPosition().latitude == geoPoint.getLatitude()
                && marker.getPosition().longitude == geoPoint.getLongitude();
    }

    public GeoPoint markerToGeoPoint(Marker marker){

        for(int i = 0; i < markerGeoPoints.size(); i ++){
            if(marker.getTag().equals(markerGeoPoints.get(i).getObjectId())){
                return markerGeoPoints.get(i);
            }
        }
        return null;
    }

    public void deleteMarkerOnMap(GeoPoint geopoint){
        if(getMarkerHashTable().get(geopoint.getObjectId()) != null){
            getMarkerHashTable().get(geopoint.getObjectId()).remove();
            getMarkerHashTable().remove(geopoint.getObjectId());
        }
    }

    private void addGeoPoint(GeoPoint geopoint){
        if(markerGeoPoints == null){
            markerGeoPoints = new ArrayList<>();
        }
        markerGeoPoints.add(geopoint);
    }

    private void makeNewMarker(GeoPoint geopoint) {
        LatLng markerLocation = new LatLng(geopoint.getLatitude(), geopoint.getLongitude());
        Marker marker = MapsActivity.getGmap().addMarker(new MarkerOptions().position(markerLocation));
        getMarkerHashTable().put(geopoint.getObjectId(), marker);
        //addGeoPoint(geopoint);
        marker.setTag(geopoint.getObjectId());

        //marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_grey600_36dp));
        String markerTittle = geopoint.getMetadata(BackendlessManager.MARKER_TITTLE).toString();
        String markerDetail = geopoint.getMetadata(BackendlessManager.MARKER_DETAIL).toString();
        marker.setTitle(markerTittle);
        marker.setSnippet(markerDetail);

        MapsActivity.getGmap().setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                dialogPopupListener.onPopupClicked(marker);
            }
        });
    }

    public interface DialogPopupListener {
        void onPopupClicked(Marker Pin);
    }
}
