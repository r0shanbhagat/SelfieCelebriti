package com.selfie.star.activity.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.base.commonframework.baselibrary.imageloader.ImageLoader;

import ss.com.bannerslider.ImageLoadingService;

public class BannerImageLoadingService implements ImageLoadingService {
    public Context context;

    public BannerImageLoadingService(Context context) {
        this.context = context;
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
        ImageLoader.loadImage(context, url, imageView);
    }

    @Override
    public void loadImage(int resource, ImageView imageView) {
        ImageLoader.loadImage(context, resource, imageView);

    }

    @Override
    public void loadImage(String url, int placeHolder, int errorDrawable, ImageView imageView) {
        ImageLoader.loadImage(context, url, imageView, placeHolder);

    }
}