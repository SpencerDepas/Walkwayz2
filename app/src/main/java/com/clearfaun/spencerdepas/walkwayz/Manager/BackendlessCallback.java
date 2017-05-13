package com.clearfaun.spencerdepas.walkwayz.Manager;

import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessFault;

/**
 * Created by SpencerDepas on 5/12/17.
 */

public interface BackendlessCallback {

    public void loginSuccess(BackendlessUser user);
    public void loginFailure(BackendlessFault fault);
}
