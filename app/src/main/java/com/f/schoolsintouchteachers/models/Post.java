package com.f.schoolsintouchteachers.models;

import java.util.ArrayList;

/**
 * Created by admin on 4/17/2018.
 */

public class Post  {
    private ArrayList<String> images;
    //   private ArrayList<String> img;
    private Post1 post1;
    private String id;

    public Post() {
    }

    public Post(Post1 post1,String id) {
        this.post1 = post1;
        this.id=id;
    }

    public Post(ArrayList<String> images, Post1 post1,String id) {
        this.images = images;
        this.post1 = post1;
        this.id=id;
    }





    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public Post1 getPost1() {
        return post1;
    }

    public void setPost1(Post1 post1) {
        this.post1 = post1;
    }
}
