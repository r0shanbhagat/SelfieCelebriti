package com.base.commonframework.baselibrary.utility;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ankit on 28/3/17.
 */
public final class ToastUtility {

    private ToastUtility()
    {

    }

    /**
     * Show toast message.
     *
     * @param context the m context
     * @param message the message
     */
    public static void showToastMessage(@NonNull Context context, @NonNull String message) {
        CustomToast customToast = new CustomToast();
        customToast.show(context, message);
    }

    /**
     * Show toast message.
     *
     * @param context the m context
     * @param message the message
     * @param gravity the gravity
     */
    public static void showToastMessage(@NonNull Context context, @NonNull String message, int gravity) {
        CustomToast customToast = new CustomToast(gravity);
        customToast.show(context, message);
    }


    /**
     * The type Custom toast.
     */
    private static final class CustomToast {
        private Toast toast;
        private int gravity = -1;

        private CustomToast() {
        }

        private CustomToast(int gravity) {
            this.gravity = gravity;
        }

        /**
         * Show.
         *
         * @param context the m context
         * @param msg     the msg
         */
        public void show(@NonNull Context context, @NonNull String msg) {
            if (null != toast) {
                toast.cancel();
            }
            toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            centerText(toast.getView());
            if (gravity != -1) {
                toast.setGravity(gravity, 0, 0);
            }
            toast.show();
        }

        private void centerText(@NonNull View view) {
            if (view instanceof TextView) {
                ((TextView) view).setGravity(Gravity.CENTER);
            } else if (view instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) view;
                int n = group.getChildCount();
                for (int i = 0; i < n; i++) {
                    centerText(group.getChildAt(i));
                }
            }
        }
    }
}
