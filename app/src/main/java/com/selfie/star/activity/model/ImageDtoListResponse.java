package com.selfie.star.activity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageDtoListResponse {
    @SerializedName("imagesDto")
    @Expose
    private List<ImagesDto> imagesDto = null;

    public List<ImagesDto> getImagesDto() {
        return imagesDto;
    }

    public void setImagesDto(List<ImagesDto> imagesDto) {
        this.imagesDto = imagesDto;
    }
}
