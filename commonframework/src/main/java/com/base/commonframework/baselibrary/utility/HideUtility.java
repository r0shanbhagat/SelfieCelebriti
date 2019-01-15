package com.base.commonframework.baselibrary.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class HideUtility {
    private HideUtility(final Activity activity) {
        ViewGroup content = activity.findViewById(android.R.id.content);
        content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dispatchTouchEvent(activity, motionEvent);
                return false;
            }
        });
    }

    public static void init(Activity activity) {
        new HideUtility(activity);
    }

    public static void hideSoftInput(Activity mActivity) {
        View view = mActivity.getCurrentFocus();
        //If no view currently has focus, onCreate a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(mActivity);
        }
        InputMethodManager im = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    private static void hideSoftInput(Activity mActivity, IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    private boolean dispatchTouchEvent(Activity mActivity, MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = mActivity.getCurrentFocus();
            if (null != v && isShouldHideInput(v, ev)) {
                hideSoftInput(mActivity, v.getWindowToken());
            }
        }
        return false;
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v instanceof EditText) {
            Rect rect = new Rect();
            v.getHitRect(rect);
            return !rect.contains((int) event.getX(), (int) event.getY());
        }
        return true;
    }
}