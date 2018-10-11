package com.f.schoolsintouchteachers.models;

import android.support.annotation.Nullable;

import com.google.firebase.storage.UploadTask;
import com.vincent.videocompressor.VideoCompress;

public class Uploadaholders {
    VideoCompress.VideoCompressTask videoCompressTask;
    UploadTask task,taskpic;
    int position;
    Post post;

    public Uploadaholders(@Nullable VideoCompress.VideoCompressTask videoCompressTask, UploadTask task, int position,@Nullable UploadTask taskpic,@Nullable Post post) {
        this.videoCompressTask = videoCompressTask;
        this.task = task;
        this.position = position;
        this.taskpic=taskpic;
        this.post=post;
    }

    public Uploadaholders() {
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public UploadTask getTaskpic() {
        return taskpic;
    }

    public void setTaskpic(UploadTask taskpic) {
        this.taskpic = taskpic;
    }

    public UploadTask getTask() {
        return task;
    }

    public void setTask(UploadTask task) {
        this.task = task;
    }

    public VideoCompress.VideoCompressTask getVideoCompressTask() {
        return videoCompressTask;
    }

    public void setVideoCompressTask(VideoCompress.VideoCompressTask videoCompressTask) {
        this.videoCompressTask = videoCompressTask;
    }



    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
