package com.titikcoe.www.bluehacks.Models;

/**
 * Created by Adrian Mark Perea on 28/01/2018.
 */

public class Course {
    private String mTitle;
    private String mUploader;
    private String mDuration;
    private String mFileSize;
    private String mDescription;
    private String mUrl;

    public Course(String title, String uploader, String duration,
                  String fileSize, String description, String url) {
        mTitle = title;
        mUploader = uploader;
        mDuration = duration;
        mFileSize = fileSize;
        mDescription = description;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getUploader() {
        return mUploader;
    }

    public void setUploader(String mUploader) {
        this.mUploader = mUploader;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getFileSize() {
        return mFileSize;
    }

    public void setFileSize(String mFileSize) {
        this.mFileSize = mFileSize;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }
}
