package com.f.schoolsintouchteachers.models;



public class Post1 {
    private String thumbnail,video,name,postedtime,post;
    private String id,audio;
    private Integer claps;
    private boolean clap=false;
   private Post1 post1;
   private int type=0;

    public Post1() {
    }

    public Post1 getPost1() {
        return post1;
    }

    public void setPost1(Post1 post1) {
        this.post1 = post1;
    }

    public Post1(String thumbnail, String video, String name, String postedtime, String post, String id,int type,String audio) {
        this.thumbnail = thumbnail;
        this.video = video;
        this.name = name;
        this.postedtime = postedtime;
        this.post = post;
        this.id = id;
        this.audio=audio;
        this.type=type;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isClap() {
        return clap;
    }

    public void setClap(boolean clap) {
        this.clap = clap;
    }

    public Integer getClaps() {
        return claps;
    }

    public void setClaps(Integer claps) {
        this.claps = claps;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostedtime() {
        return postedtime;
    }

    public void setPostedtime(String postedtime) {
        this.postedtime = postedtime;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
