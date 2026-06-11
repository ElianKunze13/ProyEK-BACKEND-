package com.example.demo.enums;

public enum TipoVideo {

    MP4("video/mp4"),
    WEBM("video/webm"),
    OGG("video/ogg"),
    AVI("video/x-msvideo"),
    MKV("video/x-matroska"),
    MOV("video/quicktime"),
    WMV("video/x-ms-wmv"),
    FLV("video/x-flv");

    private final String mimeType;

    TipoVideo(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }
}
