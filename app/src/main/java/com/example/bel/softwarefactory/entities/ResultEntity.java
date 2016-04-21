package com.example.bel.softwarefactory.entities;

public class ResultEntity {

    private static final int OK = 200;

    private Integer code;
    private String result;

    public String getResult() {
        return result;
    }

    public Integer getCode() {
        return code;
    }

    public boolean isOk () {
        return code.equals(OK);
    }
}
