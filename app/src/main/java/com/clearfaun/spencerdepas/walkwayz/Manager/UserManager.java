package com.clearfaun.spencerdepas.walkwayz.Manager;

import com.clearfaun.spencerdepas.walkwayz.Model.User;

/**
 * Created by SpencerDepas on 5/7/17.
 */

public class UserManager {

    static UserManager userManager;
    private User user;

    private UserManager(){

    }

    public static UserManager getInstance() {
        if (userManager == null) {
            userManager = new UserManager();
        }
        return userManager;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
