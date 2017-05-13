package com.clearfaun.spencerdepas.walkwayz.Manager;

import android.content.Context;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.clearfaun.spencerdepas.walkwayz.R;

/**
 * Created by SpencerDepas on 5/12/17.
 */

public class BackendlessManager {

    private static BackendlessManager backendlessManager;

    public static BackendlessManager getInstance() {
        if (backendlessManager == null) {
            backendlessManager = new BackendlessManager();
        }
        return backendlessManager;
    }

    public void init(Context context){
        Backendless.setUrl(context.getString(R.string.backendless_server_url));
        Backendless.initApp( context,
                context.getString(R.string.backendless_application_id),
                context.getString(R.string.backendless_api_key));
    }

    public void logIn(String username, String password, final BackendlessCallback backendlessCallback){
        Backendless.UserService.login(username,
                password, new AsyncCallback<BackendlessUser>() {
                    public void handleResponse(BackendlessUser user) {
                        backendlessCallback.loginSuccess(user);
                    }

                    public void handleFault(BackendlessFault fault) {
                        backendlessCallback.loginFailure(fault);
                    }
                });
    }

    public BackendlessUser getCurrentUser(){
        return Backendless.UserService.CurrentUser();
    }

    public void updateUser(BackendlessUser user){

        getCurrentUser().setProperty();

        Backendless.UserService.update( user, new AsyncCallback<BackendlessUser>() {
            public void handleResponse( BackendlessUser user ) {
                // user has been updated
            }

            public void handleFault( BackendlessFault fault ) {
                // user update failed, to get the error code call fault.getCode()
            }
        });
    }
}
