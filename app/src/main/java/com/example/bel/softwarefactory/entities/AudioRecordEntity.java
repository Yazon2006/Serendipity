package com.example.bel.softwarefactory.entities;

import java.io.Serializable;

public class AudioRecordEntity implements Serializable {

    private Integer rec_id;
    private String file_name;
    private String file_path;
    private String status;
    private String date;
    private String time;
    private String love;
    private String description;
    private String owner;
    private String latitude;
    private String longitude;

    public Integer getRec_id() {
        return rec_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLove() {
        return love;
    }

    public String getDescription() {
        return description;
    }

    public String getOwner() {
        return owner;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
