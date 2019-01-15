package com.base.commonframework.baselibrary.utility;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Roshan Bhagat
 * @version 3.0
 */
public final class ParseUtility {
    private ParseUtility() {
    }

    public static <T> T getObject(String json, Class<T> className) {
        if (json != null) {
            return new Gson().fromJson(json, className);
        }
        return null;
    }

    public static <T> T getObject(JsonElement jsonElement, Class<T> className) {
        if (jsonElement != null) {
            return new Gson().fromJson(jsonElement, className);
        }
        return null;
    }

    public static <T> T getObject(JsonElement jsonElement, Type typeDef) {
        if (jsonElement != null) {
            return new Gson().fromJson(jsonElement, typeDef);
        }
        return null;
    }

    public static <T> List<T> getList(String json, Class<T> clazz) {
        if (null == json) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<T>() {
        }.getType());
    }

    public static <T> List<T> getList(String json, Type founderListType) {
        if (null == json) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json, founderListType);
    }


    public static String getJson(Object object) {
        if (object != null) {
            return new Gson().toJson(object);
        }
        return null;
    }

    public static String getJson(JsonElement jsonElement) {
        if (jsonElement != null) {
            return new Gson().toJson(jsonElement);
        }
        return null;
    }
}
