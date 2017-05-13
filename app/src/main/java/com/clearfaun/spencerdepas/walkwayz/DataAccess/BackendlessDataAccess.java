package com.clearfaun.spencerdepas.walkwayz.DataAccess;

import android.content.Context;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.property.ObjectProperty;
import com.backendless.property.UserProperty;
import com.clearfaun.spencerdepas.walkwayz.Model.User;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by SpencerDepas on 5/7/17.
 */

public class BackendlessDataAccess {

    private final String USER_DATA = "Users";

    private Context context;

    public BackendlessDataAccess(Context context){
        this.context = context;
    }


    public void registerUser(HashMap userInfo){
        //userInfo.put("objectId", USER_DATA);
        BackendlessUser user = new BackendlessUser();
        user.setProperty( "email", "james.bond@mi6.co.uk" );
        user.setProperty( "bacon", true );
        user.setPassword( "iAmWatchingU" );

        Backendless.UserService.register( user, new AsyncCallback<BackendlessUser>()
        {
            public void handleResponse( BackendlessUser registeredUser )
            {
                // user has been registered and now can login
                getUserData();
            }

            public void handleFault( BackendlessFault fault )
            {
                // an error has occurred, the error code can be retrieved with fault.getCode()
            }
        } );
    }



//    public void updateUser(HashMap user) {
//        user.put("objectId", USER_DATA);
//        Backendless.Persistence.save( user, new AsyncCallback<Map>() {
//            @Override
//            public void handleResponse( Map response )
//            {
//                // Contact objecthas been updated
//            }
//            @Override
//            public void handleFault( BackendlessFault fault )
//            {
//                // an error has occurred, the error code can be retrieved with fault.getCode()
//            }
//        } );
//    }


    public void getUserData(){

        Backendless.Persistence.describe( USER_DATA, new AsyncCallback<List<ObjectProperty>>()
        {
            public void handleResponse( List<ObjectProperty> tableProperties )
            {
//                Iterator<ObjectProperty> iterator = tableProperties;
//
//                while( iterator.hasMore() )
//                {
//                    ObjectProperty propDef = iterator.next();
//                    Log.i( "MYAPP", "property name - " + propDef.getName() );
//                    Log.i( "MYAPP", "\tis property required - " + propDef.isRequired() );
//                    Log.i( "MYAPP", "\tproperty data type - " + propDef.getType() );
//                    Log.i( "MYAPP", "\tdefault value - " + propDef.getDefaultValue() );
//                    //Log.i( "MYAPP", "\tis property identity - " + propDef.getPrimaryKey() );
//                }
            }

            public void handleFault( BackendlessFault backendlessFault )
            {
            }
        } );
    }

}
