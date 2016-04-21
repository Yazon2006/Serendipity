package com.example.bel.softwarefactory.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.example.bel.softwarefactory.R;
import com.example.bel.softwarefactory.entities.AudioRecordEntity;
import com.example.bel.softwarefactory.preferences.SharedPreferencesManager;
import com.example.bel.softwarefactory.utils.LocationHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

import java.util.List;

@EFragment(R.layout.fragment_map)
public class MapFragment extends BaseFragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private final String TAG = this.getClass().getSimpleName();

    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    @Bean
    protected SharedPreferencesManager sharedPreferencesManager;

    @AfterViews
    public void afterViews() {
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(MapFragment.this)
                .addOnConnectionFailedListener(MapFragment.this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected()");
        startLocationUpdates();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                LatLng lastPosition = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                sharedPreferencesManager.setLastPosition(lastPosition);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPosition, 15));
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended() :" + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed() connectionResult:" + connectionResult.getErrorMessage());
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "onConnected()");
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, MapFragment.this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume()");
        if (googleApiClient != null && googleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (locationRequest == null) {
                locationRequest = new LocationRequest();
            }
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(5000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, MapFragment.this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d(TAG, "onStop()");
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        UiSettings mUiSettings = this.googleMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);

        mUiSettings.setTiltGesturesEnabled(false);
        mUiSettings.setRotateGesturesEnabled(false);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.googleMap.setMyLocationEnabled(true);

            List<AudioRecordEntity> audioRecordEntities = sharedPreferencesManager.getAudioRecordsList();
            markAllAudioRecordsOnMap(audioRecordEntities);
        } else {
            Toast.makeText(getActivity(), R.string.permission_not_granted, Toast.LENGTH_LONG).show();
        }
    }

    private void markAllAudioRecordsOnMap(List<AudioRecordEntity> audioRecordEntities) {
        Stream.of(audioRecordEntities).forEach((audioItem) -> {
            if (!audioItem.getLatitude().isEmpty() && !audioItem.getLongitude().isEmpty()) {
                this.googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(audioItem.getLatitude()), Double.parseDouble(audioItem.getLongitude())))
                        .title(audioItem.getFile_name()));
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged()");
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        List<AudioRecordEntity> audioRecordEntities = sharedPreferencesManager.getAudioRecordsList();

        boolean availability = false;
        for (int i = 0; i < audioRecordEntities.size(); i++) {
                if (LocationHelper.isAudioAvailable(audioRecordEntities.get(i), position)) {
                    if (!sharedPreferencesManager.getRecordsAvailability()) {
                        Toast.makeText(getActivity(), R.string.available_new_audio, Toast.LENGTH_SHORT).show();
                    }
                    availability = true;
                }
        }
        sharedPreferencesManager.setRecordsAvailability(availability);
        sharedPreferencesManager.setLastPosition(position);
    }

}
