package com.selfie.star.activity.adapter;


import com.selfie.star.R;

import java.util.List;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class BannerSliderAdapter extends SliderAdapter {

    private List<String> bannerImageList;

    public BannerSliderAdapter(List<String> bannerImageList) {
        this.bannerImageList = bannerImageList;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        viewHolder.bindImageSlide(bannerImageList.get(position), R.drawable.banner_default_image, R.drawable.banner_default_image);
    }

    @Override
    public int getItemCount() {
        return null != bannerImageList ? bannerImageList.size() : 0;
    }
}