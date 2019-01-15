package com.base.commonframework.baselibrary.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.commonframework.utility.CommonFrameworkUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * #Ref:https://bumptech.github.io/glide/doc/migrating.html
 * https://github.com/qoqa/glide-svg
 * gif,file,drawable,Bitmap,Svg,Uri
 * String urlPng = "https://upload.wikimedia.org/wikipedia/commons/4/47/PNG_transparency_demonstration_1.png";
 * String urlJpg = "https://media.alienwarearena.com/media/tux-r.jpg";
 * String urlGif = "http://ttlstatic-a.akamaihd.net/s3fs/google%20developers%20india%20android%20course%20_2.gif";
 * String urlSVG = "http://upload.wikimedia.org/wikipedia/commons/e/e8/Svg_example3.svg";
 * <p>
 * USE TAG WITH IMAGEVIEW:* http://androidhiker.blogspot.in/2015/10/how-to-resolve-glide-settag-issue.html
 */
public final class ImageLoader {

    private ImageLoader() {
    }

    public static void loadImage(@Nullable final Context mContext, @NonNull final Object arg0, @NonNull final ImageView iv) {
        try {
            GlideApp.with(mContext)
                    .as(Bitmap.class)
                    .load(arg0)
                    .into(iv);
        } catch (Exception e) {
            CommonFrameworkUtil.showException(e);
        }
        //.signature(new ObjectKey(System.currentTimeMillis()))

    }


    public static void loadImageWithoutCache(@Nullable final Context mContext, @NonNull final Object arg0, @NonNull final ImageView iv, int defaultImageId) {
        try {
            GlideApp.with(mContext)
                    .as(Bitmap.class)
                    .load(arg0)
                    .placeholder(defaultImageId)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(iv);
        } catch (Exception e) {
            CommonFrameworkUtil.showException(e);
        }
    }

    public static void loadImage(@Nullable Context mContext, @NonNull Object arg0, @NonNull ImageView iv, int defaultImageId) {
        try {
            GlideApp.with(mContext)
                    .as(Bitmap.class)
                    .load(arg0)
                    .placeholder(defaultImageId)
                    .into(iv);
        } catch (Exception e) {
            CommonFrameworkUtil.showException(e);
        }
    }

    public static void loadImage(@Nullable Context mContext, @NonNull Object arg0, @NonNull ImageView iv, Drawable placeHolder) {
        try {
            GlideApp.with(mContext)
                    .as(Bitmap.class)
                    .load(arg0)
                    .placeholder(placeHolder)
                    .into(iv);
        } catch (Exception e) {
            CommonFrameworkUtil.showException(e);
        }

    }

    public static void loadImage(@Nullable Context mContext, Object arg0, @Nullable ImageView iv, @NonNull RequestOptions requestOptions, int defaultImage) {
        try {
            GlideApp.with(mContext)
                    .as(Bitmap.class)
                    .load(arg0)
                    .apply(requestOptions)
                    .placeholder(defaultImage)
                    .into(iv);
        } catch (Exception e) {
            CommonFrameworkUtil.showException(e);
        }
    }

    public static void loadImageWithDrawable(@Nullable final Context mContext, @Nullable Object arg0,
                                             @Nullable int defaultImageId, final DrawableEnum drawableEnum, final View componentView) {
        try {
            int height = mContext.getResources().getDrawable(defaultImageId).getIntrinsicHeight();
            int width = mContext.getResources().getDrawable(defaultImageId).getIntrinsicWidth();
            GlideApp.with(mContext)
                    .as(Bitmap.class)
                    .load(arg0)
                    .override(width, height)
                    .placeholder(defaultImageId)
                    .into(new BaseTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                            BitmapDrawable resource = new BitmapDrawable(mContext.getResources(), bitmap);
                            TextView view = (TextView) componentView;
                            switch (drawableEnum) {
                                case TOP:
                                    view.setCompoundDrawablesWithIntrinsicBounds(null, resource, null, null);
                                    break;
                                case LEFT:
                                    view.setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null);
                                    break;
                                case BOTTOM:
                                    view.setCompoundDrawablesWithIntrinsicBounds(null, null, null, resource);
                                    break;
                                case RIGHT:
                                    view.setCompoundDrawablesWithIntrinsicBounds(null, null, resource, null);
                                    break;
                            }

                        }

                        @Override
                        public void getSize(SizeReadyCallback cb) {
                            cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                        }

                        @Override
                        public void removeCallback(SizeReadyCallback cb) {

                        }
                    });
        } catch (Exception e) {
            CommonFrameworkUtil.showException(e);
        }

    }

    public static void loadImage(@Nullable Context mContext, @Nullable String url, @Nullable ImageView iv, @Nullable final ImageLoadCallBack imageLoadCallBack) {
        try {
            GlideApp.with(mContext)
                    .as(Bitmap.class)
                    .load(url)
                    .listener(new RequestListener() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                            if (null != imageLoadCallBack) {
                                imageLoadCallBack.onFailure(e);
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                            if (null != imageLoadCallBack) {
                                imageLoadCallBack.onSuccess(resource);
                            }
                            return false;
                        }
                    }).into(iv);
        } catch (Exception e) {
            CommonFrameworkUtil.showException(e);
        }

    }


    public static void loadImage(@Nullable final Context mContext, @Nullable Object arg0, @Nullable final ImageLoadCallBack imageLoadCallBack) {
        try {
            GlideApp.with(mContext)
                    .as(Bitmap.class)
                    .load(arg0)
                    .into(new BaseTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                            if (null != imageLoadCallBack) {
                                imageLoadCallBack.onSuccess(bitmap);
                            }
                        }

                        @Override
                        public void getSize(SizeReadyCallback cb) {
                            cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                        }

                        @Override
                        public void removeCallback(SizeReadyCallback cb) {

                        }
                    });
        } catch (Exception e) {
            CommonFrameworkUtil.showException(e);
        }

    }


    public static void loadGifImage(@Nullable Context context, @Nullable String url, @Nullable ImageView iv) {
        try {
            Glide.with(context)
                    .load(url)
                    .into(iv);
        } catch (Exception e) {
            CommonFrameworkUtil.showException(e);
        }
    }




    public enum DrawableEnum {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM
    }


}
