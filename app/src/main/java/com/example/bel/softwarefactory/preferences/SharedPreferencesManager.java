package com.example.bel.softwarefactory.preferences;

import com.example.bel.softwarefactory.entities.AudioRecordEntity;
import com.example.bel.softwarefactory.entities.UserEntity;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.lang.reflect.Type;
import java.util.List;

@EBean
public class SharedPreferencesManager {

    @Pref
    protected UserLocalData_ userLocalData;

    @Pref
    protected ProgramData_ programData;

    public void saveUser(UserEntity user) {
        Gson gson = new Gson();
        String userString = gson.toJson(user);

        userLocalData.edit()
                .user()
                .put(userString)
                .apply();
    }

    public UserEntity getUser() {
        Gson gson = new Gson();
        return gson.fromJson(userLocalData.user().get(), UserEntity.class);
    }

    public void setUsername(String username) {
        Gson gson = new Gson();
        UserEntity userEntity = gson.fromJson(userLocalData.user().get(), UserEntity.class);
        userEntity.setUsername(username);
        saveUser(userEntity);
    }

    public void setEmail(String email) {
        Gson gson = new Gson();
        UserEntity userEntity = gson.fromJson(userLocalData.user().get(), UserEntity.class);
        userEntity.setEmail(email);
        saveUser(userEntity);
    }

    public void setPassword(String password) {
        Gson gson = new Gson();
        UserEntity userEntity = gson.fromJson(userLocalData.user().get(), UserEntity.class);
        userEntity.setPassword(password);
        saveUser(userEntity);
    }

    public void setUserLoggedIn(boolean loggedIn) {
        userLocalData.edit()
                .loggedIn()
                .put(loggedIn)
                .apply();
    }

    public boolean isUserLoggedIn() {
        return userLocalData.loggedIn().getOr(false);
    }

    public void clearUserData() {
        userLocalData.clear();
    }

    // set variable to remember user details if he want or don't want to be remembered (depends on the check box)
    public void setRememberUser(boolean rememberUser) {
        userLocalData.edit()
                .rememberUser()
                .put(rememberUser)
                .apply();
    }

    public boolean isRememberMe() {
        return userLocalData.rememberUser().getOr(false);
    }

    public void setFacebookLoggedIn(boolean facebookLogin) {
        userLocalData.edit()
                .facebookLogin()
                .put(facebookLogin)
                .apply();
    }

    public boolean isFacebookLoggedIn() {
        return userLocalData.facebookLogin().getOr(false);
    }

    public void setFacebookId(long id) {
        userLocalData.edit()
                .facebookId()
                .put(id)
                .apply();
    }

    public long getFacebookId() {
        return userLocalData.facebookId().getOr(-1L);
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        userLocalData.edit()
                .profilePictureUrl()
                .put(profilePictureUrl)
                .apply();
    }

    public String getProfilePictureUrl() {
        return userLocalData.profilePictureUrl().getOr(null);
    }

    public void setLastPosition(LatLng latLng) {
        programData.edit()
                .lastLatitude()
                .put((float) latLng.latitude)
                .lastLongitude()
                .put((float) latLng.longitude)
                .apply();
    }

    public LatLng getLastLocation() {
        return new LatLng(programData.lastLatitude().get(), programData.lastLongitude().get());
    }

    public void saveAudioRecordsList(List<AudioRecordEntity> audioRecordEntities) {
        Gson gson = new Gson();
        String saveAudioRecordsListString = gson.toJson(audioRecordEntities);

        programData.edit()
                .audioRecordsList()
                .put(saveAudioRecordsListString)
                .apply();
    }

    public List<AudioRecordEntity> getAudioRecordsList() {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<AudioRecordEntity>>() {}.getType();
        return gson.fromJson(programData.audioRecordsList().get(), listType);
    }

    public void setRecordsAvailability(boolean available) {
        programData.edit()
                .recordsAvailability()
                .put(available)
                .apply();
    }

    public boolean getRecordsAvailability() {
        return programData.recordsAvailability().getOr(false);
    }

}

