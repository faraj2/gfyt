package com.f.schoolsintouchteachers.models.realmObjects;

import com.f.schoolsintouchteachers.models.Post;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Pst extends RealmObject {


    private RealmList<String> images;
 //   private ArrayList<String> img;
    private Post1 post1;
    @PrimaryKey
    private String id;
    private boolean posting=false;


    public Pst() {
    }

    public Pst(Post1 post1, String id,boolean posting) {
        this.post1 = post1;
        this.id=id;
        this.posting=posting;
    }

    public Pst(RealmList<String> images, Post1 post1, String id,boolean posting) {
        this.images = images;
        this.post1 = post1;
        this.id=id;
        this.posting=posting;
    }


    public boolean isPosting() {
        return posting;
    }

    public void setPosting(boolean posting) {
        this.posting = posting;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<String> getImages() {
        return images;
    }

    public void setImages(RealmList<String> images) {
        this.images = images;
    }

    public Post1 getPost1() {
        return post1;
    }

    public void setPost1(Post1 post1) {
        this.post1=post1;
    }
}
