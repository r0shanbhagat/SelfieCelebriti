
package com.selfie.star.activity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponseData {
    @SerializedName("categories")
    @Expose
    private List<Category> subCategories = null;

    public List<Category> getCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Category> subCategories) {
        this.subCategories = subCategories;
    }
}
