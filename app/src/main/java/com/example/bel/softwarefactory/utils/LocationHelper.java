package com.example.bel.softwarefactory.utils;

import com.example.bel.softwarefactory.entities.AudioRecordEntity;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

public class LocationHelper {

    public static boolean isAudioAvailable(AudioRecordEntity audioRecordEntity, LatLng position) {
        if (!audioRecordEntity.getLatitude().isEmpty() && !audioRecordEntity.getLongitude().isEmpty()) {
            double distance = SphericalUtil.computeDistanceBetween(new LatLng(position.latitude, position.longitude), new LatLng(Double.parseDouble(audioRecordEntity.getLatitude()), Double.parseDouble(audioRecordEntity.getLongitude())));
            return distance < AppConstants.AVAILABILITY_RADIUS;
        }
        return false;
    }

}