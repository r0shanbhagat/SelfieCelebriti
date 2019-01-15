
package com.selfie.star.activity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;
@Parcel
public class Category implements Comparable {

    @SerializedName("id")
    @Expose
    long id;
    @SerializedName("subCategories")
    @Expose
     List<Category> subCategories = null;
    @SerializedName("name")
    @Expose
     String name;
    @SerializedName("createdOn")
    @Expose
     long createdOn;
    @SerializedName("updatedOn")
    @Expose
     long updatedOn;
    @SerializedName("isActive")
    @Expose
     boolean isActive;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Category> subCategories) {
        this.subCategories = subCategories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public long getUpdatedOn() {
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


    @Override
    public int compareTo(Object object) {
        Category category= (Category) object;
        return name.compareTo(category.name);
    }
}
