package com.f.schoolsintouchteachers.models;

import com.f.schoolsintouchteachers.adapters.HomeAdapter;
import com.koushikdutta.async.future.Future;

import java.io.File;

public class Downloadholders {
    String idd;
    File file;
    String path;
    int position;
    HomeAdapter.Myviewholder progress;
    Future future;

    public Downloadholders(String idd, File file, String path, int position, HomeAdapter.Myviewholder progress, Future builder) {
        this.idd = idd;
        this.file = file;
        this.path = path;
        this.position = position;
        this.progress=progress;
        this.future=builder;
    }

    public Future getFuture() {
        return future;
    }

    public void setFuture(Future future) {
        this.future = future;
    }

    public HomeAdapter.Myviewholder getProgress() {
        return progress;
    }

    public void setProgress(HomeAdapter.Myviewholder progress) {
        this.progress = progress;
    }

    public String getIdd() {
        return idd;
    }

    public void setIdd(String idd) {
        this.idd = idd;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
