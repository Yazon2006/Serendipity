package com.example.bel.softwarefactory.preferences;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(SharedPref.Scope.UNIQUE)
public interface UserLocalData {

    String user();

    boolean loggedIn();

    boolean rememberUser();

    boolean facebookLogin();

    long facebookId();

    String profilePictureUrl();

}
