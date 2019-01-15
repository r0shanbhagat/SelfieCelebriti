package com.selfie.star.activity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImagesDto {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("categoryId")
    @Expose
    private long categoryId;
    @SerializedName("thumbnailUrl")
    @Expose
    private Object thumbnailUrl;
    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;
    @SerializedName("createdOn")
    @Expose
    private long createdOn;
    @SerializedName("isactive")
    @Expose
    private boolean isactive;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Object getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(Object thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }
}