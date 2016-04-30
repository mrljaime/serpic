package com.tilatina.guardcheck.Utillities;

/**
 * Created by jaime on 30/04/16.
 */
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

/**
 * Created by jaime on 19/04/16.
 */
public class NoveltyThumbnail {
    private Bitmap thumbnail;
    private String path;

    public NoveltyThumbnail() {

    }

    public NoveltyThumbnail(Bitmap thumbnail, String path) {
        this.thumbnail = thumbnail;
        this.path = path;
    }

    public NoveltyThumbnail setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public NoveltyThumbnail setPath(String path) {
        this.path = path;
        return this;
    }

    public String getPath() {
        return path;
    }

}

