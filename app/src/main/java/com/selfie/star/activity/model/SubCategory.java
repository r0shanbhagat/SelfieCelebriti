
package com.selfie.star.activity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubCategory {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("subCategories")
    @Expose
    private List<SubCategory> subCategories = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("createdOn")
    @Expose
    private int createdOn;
    @SerializedName("updatedOn")
    @Expose
    private int updatedOn;
    @SerializedName("isActive")
    @Expose
    private boolean isActive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(int createdOn) {
        this.createdOn = createdOn;
    }

    public int getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(int updatedOn) {
        this.updatedOn = updatedOn;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

}
