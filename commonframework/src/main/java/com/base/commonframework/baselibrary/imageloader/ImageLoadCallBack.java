package com.base.commonframework.baselibrary.imageloader;

import com.bumptech.glide.load.engine.GlideException;

public interface ImageLoadCallBack
{
    void onSuccess(Object resource);

    void onFailure(GlideException ge);
}
