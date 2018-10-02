package com.osu.cse.apps.mobile.woof;

/*
 * Singleton class for easily retrieving current user while navigating
 * activities.
 */

public class CurrentUser extends User {

    private static User sCurrentUser;

    public static User get() {
        if (sCurrentUser == null) {
            throw new NoCurrentUserException("No current user");
            // better to redirect to login screen if there is no current user than to
            // throw exception?
        }
        return sCurrentUser;
    }

    public CurrentUser() {
        sCurrentUser = new User();
    }


}
