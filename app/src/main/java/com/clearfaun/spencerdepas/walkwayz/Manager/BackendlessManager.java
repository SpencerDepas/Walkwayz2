package com.clearfaun.spencerdepas.walkwayz.Manager;

import android.content.Context;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.BackendlessGeoQuery;
import com.backendless.geo.GeoPoint;
import com.clearfaun.spencerdepas.walkwayz.Activity.WalkWayzApplication;
import com.clearfaun.spencerdepas.walkwayz.Model.User;
import com.clearfaun.spencerdepas.walkwayz.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SpencerDepas on 5/12/17.
 */

public class BackendlessManager {

    public static final String PHONE = "phone";
    private static final String AGE = "age";
    private static final String HEIGHT = "height";
    private static final String LOCATION = "location";
    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    public static final String IMAGE = "image";
    private static final String EMERGENCY = "Emergencies";
    private static final String EMERGENCY_TYPE = "emergency_type";
    private static final String HYPER_TRACK_USER_ID = "hypertrackID";
    private static final String GEO_CATEGORY_MARKER_ID = "GEO_CATEGORY_MARKER_ID";
    public static final String OBJECT_ID = "objectId";
    public static final String MARKER_TITTLE = "marker_tittle";
    public static final String MARKER_DETAIL = "marker_detail";


    public static final String ID = "ownerId";
    private final String USER = "user";


    private static BackendlessManager backendlessManager;

    public static BackendlessManager getInstance() {
        if (backendlessManager == null) {
            backendlessManager = new BackendlessManager();
        }
        return backendlessManager;
    }

    public void init(Context context) {
        Backendless.setUrl(context.getString(R.string.backendless_server_url));
        Backendless.initApp(WalkWayzApplication.getAppContext(),
                context.getString(R.string.backendless_application_id),
                context.getString(R.string.backendless_api_key),
                context.getString(R.string.backendless_version));
    }

    public void logIn(String username, String password, final BackendlessCallback backendlessCallback) {
        Backendless.UserService.login(username,
                password, new AsyncCallback<BackendlessUser>() {
                    public void handleResponse(BackendlessUser user) {
                        backendlessCallback.callbackSuccess(user);
                    }

                    public void handleFault(BackendlessFault fault) {
                        backendlessCallback.callbackFailure(fault);
                    }
                });
    }

    public BackendlessUser getCurrentUser() {
        return Backendless.UserService.CurrentUser();
    }


    public void emergencyCall(String emergencyType, final BackendlessEmergencyCallback backendlessCallback) {
        HashMap emergencyInfo = new HashMap();
        emergencyInfo.put( EMERGENCY_TYPE, emergencyType);
        emergencyInfo.put( HYPER_TRACK_USER_ID, User.getInstance().getHypertrackID());

        BackendlessUser user = getCurrentUser();
        emergencyInfo.put( USER, getCurrentUser());
        GeoPoint geoPoint = new GeoPoint(User.getInstance().getLocation().getLatitude(),
                User.getInstance().getLocation().getLongitude());
        user.setProperty(LOCATION, geoPoint);
        Backendless.Persistence.of( EMERGENCY ).save( emergencyInfo, new AsyncCallback<Map>() {
            public void handleResponse( Map response ) {
                backendlessCallback.callbackSuccess(response);
            }

            public void handleFault( BackendlessFault fault ) {
                backendlessCallback.callbackFailure(fault);
            }
        });
    }

    public void updateUser(BackendlessUser user, final BackendlessCallback backendlessCallback) {
        user.setProperty(AGE, User.getInstance().getAge());
        user.setProperty(PHONE, User.getInstance().getPhone());
        user.setProperty(HEIGHT, User.getInstance().getHeight());
        GeoPoint geoPoint = new GeoPoint(User.getInstance().getLocation().getLatitude(),
                User.getInstance().getLocation().getLongitude());
        user.setProperty(LOCATION, geoPoint);
        //user.setProperty(EMAIL,User.getInstance().getEmail());
        //user.setProperty(PASSWORD,User.getInstance().getPassword());
        //user.setProperty(IMAGE,"");
        Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
            public void handleResponse(BackendlessUser user) {
                backendlessCallback.callbackSuccess(user);
            }

            public void handleFault(BackendlessFault fault) {
                backendlessCallback.callbackFailure(fault);
            }
        });
    }

    public void saveGlobleGeoLocation(String tittle, String detail, LatLng location,
                                      final BackendlessPostMarkerCallback backendlessCallback) {

        List<String> categories = new ArrayList<String>();
        categories.add(GEO_CATEGORY_MARKER_ID);

        Map<String, Object> meta = new HashMap<String, Object>();
        meta.put(MARKER_TITTLE, tittle);
        meta.put(MARKER_DETAIL, detail);

        Backendless.Geo.savePoint(location.latitude, location.longitude, categories, meta,
                new AsyncCallback<GeoPoint>() {
                    @Override
                    public void handleResponse(GeoPoint geoPoint) {
                        backendlessCallback.callbackSuccess(geoPoint);
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        backendlessCallback.callbackFailure(backendlessFault);
                    }
                });
    }

    public void getGlobalMarkers(final BackendlessGetMarkerCallback backendlessCallback) {

        BackendlessGeoQuery geoQuery = new BackendlessGeoQuery();
        geoQuery.addCategory(GEO_CATEGORY_MARKER_ID);
        geoQuery.setIncludeMeta( true );
        Backendless.Geo.getPoints( geoQuery , new AsyncCallback<BackendlessCollection<GeoPoint>>(){
            @Override
            public void handleResponse(BackendlessCollection<GeoPoint> response) {
                backendlessCallback.callbackSuccess(response);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                backendlessCallback.callbackFailure(fault);
            }
        });
    }


    public void deleteMarker(GeoPoint marker, final BackendlessDeleteMarkerCallback backendlessDeleteMarkerCallback){

        Backendless.Geo.removePoint(marker, new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {
                backendlessDeleteMarkerCallback.callbackSuccess(response);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                backendlessDeleteMarkerCallback.callbackFailure(fault);
            }
        } );

    }

    public void registerUser(HashMap userInfo){
        //userInfo.put("objectId", USER_DATA);
        BackendlessUser user = new BackendlessUser();
        user.setProperty( "email", "james.bond@mi6.co.uk" );
        user.setProperty( "bacon", true );
        user.setPassword( "iAmWatchingU" );

        Backendless.UserService.register( user, new AsyncCallback<BackendlessUser>() {
            public void handleResponse( BackendlessUser registeredUser ) {
                // user has been registered and now can login

            }

            public void handleFault( BackendlessFault fault ) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
            }
        } );
    }

    public interface BackendlessEmergencyCallback {
        void callbackSuccess(Map response);
        void callbackFailure(BackendlessFault fault);
    }

    public interface BackendlessPostMarkerCallback {
        void callbackSuccess(GeoPoint response);
        void callbackFailure(BackendlessFault fault);
    }

    public interface BackendlessGetMarkerCallback {
        void callbackSuccess(BackendlessCollection<GeoPoint> response);
        void callbackFailure(BackendlessFault fault);
    }

    public interface BackendlessDeleteMarkerCallback {
        void callbackSuccess(Void response);
        void callbackFailure(BackendlessFault fault);
    }


}
