package com.base.commonframework.baselibrary.network;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Created by Gautam Sharma on 05-01-2016.
 */
public final class AnnotationExclusionStrategy implements ExclusionStrategy
{
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(Exclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
