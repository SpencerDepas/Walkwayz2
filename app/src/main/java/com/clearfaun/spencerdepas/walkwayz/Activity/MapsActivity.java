package com.clearfaun.spencerdepas.walkwayz.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.backendless.BackendlessCollection;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.GeoPoint;
import com.clearfaun.spencerdepas.walkwayz.Adapter.PopupAdapter;
import com.clearfaun.spencerdepas.walkwayz.Manager.BackendlessManager;
import com.clearfaun.spencerdepas.walkwayz.Manager.MarkerManager;
import com.clearfaun.spencerdepas.walkwayz.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hypertrack.lib.HyperTrack;
import com.hypertrack.lib.callbacks.HyperTrackCallback;
import com.hypertrack.lib.models.ErrorResponse;
import com.hypertrack.lib.models.Place;
import com.hypertrack.lib.models.SuccessResponse;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, MarkerManager.DialogPopupListener {

    private static GoogleMap mMap;
    private Toolbar toolbar;

    LatLng position1 = new LatLng(54.5312293, 18.5193164);
    LatLng position2 = new LatLng(54.53332, 18.5250559);
    LatLng position3 = new LatLng(54.5161936, 18.5396568);

    String markerTittle = "";
    String markerDetail = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_maps);

        setUpToolBar();

        getCurrentUserLocation();
        setUpMap();
        MarkerManager.getInstance().init(this);
        getMarkersFromBackendless();


    }

    private void getMarkersFromBackendless(){
        BackendlessManager.getInstance().getGlobalMarkers(
                new BackendlessManager.BackendlessGetMarkerCallback(){
                    @Override
                    public void callbackSuccess(BackendlessCollection<GeoPoint> response) {

                        MarkerManager.getInstance().addMarkerToMap(response.getData());

                        //BackendlessManager.getInstance().deleteMarker(response.getData().get(0));
                    }

                    @Override
                    public void callbackFailure(BackendlessFault fault) {

                    }
                }
        );
    }

    public static GoogleMap getGmap(){
        return mMap;
    }

    private void setUpMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setUpToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Walkwayz");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, MapsActivity.class);
        return  intent;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng arg0) {
                addMarkerDialog(arg0);
            }
        });
    }


    private void addMarkerDialog(final LatLng arg0) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.maps_add_marker_dialog);
        dialog.setTitle("Title...");

        final EditText tittle = (EditText) dialog.findViewById(R.id.place_edit_text);
        final EditText detail = (EditText) dialog.findViewById(R.id.place_detail_edit_text);

        Button dialogButtonOK = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                sendMarkerToBackendless(tittle.getText().toString(),detail.getText().toString(),  arg0);
            }
        });

        Button dialogCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        // if button is clicked, close the custom dialog
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void sendMarkerToBackendless(String tittle, String detail, LatLng location){
        BackendlessManager.getInstance().saveGlobleGeoLocation(tittle, detail, location,
                new BackendlessManager.BackendlessPostMarkerCallback(){
                    @Override
                    public void callbackSuccess(GeoPoint geoPoint) {

                        addMarkerToMap(geoPoint);
                    }

                    @Override
                    public void callbackFailure(BackendlessFault fault) {

                    }
                }
        );
    }

    private void addMarkerToMap(GeoPoint geoPoint){
        MarkerManager.getInstance().addMarkerToMap(geoPoint);
    }


    private void updateMapCameraLocation( Location location){
        LatLng poland = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(poland,14), new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {
                Snackbar snackbar = Snackbar
                        .make(toolbar, "To Add a marker long press on the map", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void getCurrentUserLocation(){
        HyperTrack.getCurrentLocation(new HyperTrackCallback() {
            @Override
            public void onSuccess(@NonNull SuccessResponse successResponse) {
                Location location = (Location) successResponse.getResponseObject();
                updateMapCameraLocation(location);
            }

            @Override
            public void onError(@NonNull ErrorResponse errorResponse) {

            }
        });
    }

    @Override
    public void onPopupClicked(Marker pin) {


        GeoPoint selectedGeoPoint = MarkerManager.getInstance().markerToGeoPoint(pin);
        if(selectedGeoPoint != null){

            deleteMarkerOnServer(selectedGeoPoint);
        }

    }

    private void deleteMarkerOnServer(final GeoPoint selectedGeoPoint){
        BackendlessManager.getInstance()
                .deleteMarker(selectedGeoPoint, new BackendlessManager.BackendlessDeleteMarkerCallback() {
                            @Override
                            public void callbackSuccess(Void response) {
                                Log.d("e", "");
                                MarkerManager.getInstance().deleteMarkerOnMap(selectedGeoPoint);
                                Snackbar.make(toolbar, "Marker deleted", Snackbar.LENGTH_LONG);
                            }

                            @Override
                            public void callbackFailure(BackendlessFault fault) {
                                Snackbar.make(toolbar, "Error deleting. Try again soon", Snackbar.LENGTH_LONG);
                            }
                        }
                );
    }
}
