package com.example.bel.softwarefactory.preferences;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(SharedPref.Scope.APPLICATION_DEFAULT)
public interface ProgramData {

    String audioRecordsList();

    float lastLongitude();
    float lastLatitude();

    boolean recordsAvailability();

}
