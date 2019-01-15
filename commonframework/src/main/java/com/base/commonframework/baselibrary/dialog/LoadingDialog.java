package com.base.commonframework.baselibrary.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.base.commonframework.R;
import com.base.commonframework.utility.CommonFrameworkUtil;


public class LoadingDialog extends Dialog {
    private static Dialog mainDialog;

    private LoadingDialog(Context context) {
        super(context, R.style.DialogTheme);
    }

    /**
     * Show.
     *
     * @param context the context
     */
    public static void show(Context context) {
        show(context, "Loading. Please wait...");
    }

    public static void show(Builder builder) {
        show(builder.context, builder.message, builder.cancelable, builder.customThemeOrColorCode, builder.cancelListener);
    }

    public static void show(Context context, CharSequence message) {
        show(context, message, false);
    }


    public static void show(Context context, CharSequence message, boolean cancelable) {
        show(context, message, cancelable, 0);
    }

    /**
     * Show.
     *
     * @param context    the context
     * @param message    the message
     * @param cancelable the cancelable
     */
    public static void show(Context context, CharSequence message, boolean cancelable, int customThemeOrColorCode) {
        show(context, message, cancelable, customThemeOrColorCode, null);
    }

    /**
     * Show.
     *
     * @param context        the context
     * @param message        the message
     * @param cancelable     the cancelable
     * @param cancelListener the cancel listener
     */
    public static void show(Context context, CharSequence message, boolean cancelable, int customThemeOrColorCode, OnCancelListener cancelListener) {
        try {
            dismissDialog();
            if (!TextUtils.isEmpty(message)) {
                ProgressDialog dialog = new ProgressDialog(context, customThemeOrColorCode);
                dialog.setMessage(message);
                dialog.setIndeterminate(true);
                dialog.setCanceledOnTouchOutside(cancelable);
                dialog.setOnCancelListener(cancelListener);
                dialog.setCancelable(cancelable);
                dialog.show();

                mainDialog = dialog;
            } else {
                LoadingDialog dialog = new LoadingDialog(context);
                dialog.setCancelable(cancelable);
                dialog.setCanceledOnTouchOutside(cancelable);
                dialog.setOnCancelListener(cancelListener);
                /* The next line will add the ProgressBar to the dialog. */
                ProgressBar progressBar = new ProgressBar(context);
                if (customThemeOrColorCode > 0) {
                    progressBar.getIndeterminateDrawable().setColorFilter(customThemeOrColorCode, PorterDuff.Mode.MULTIPLY);
                }
                dialog.addContentView(progressBar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                dialog.show();

                mainDialog = dialog;
            }
        } catch (Exception e) {
            CommonFrameworkUtil.showException(e);
        }
    }

    /**
     * Dismiss dialog.
     */
    public static void dismissDialog() {
        try {
            if (null != mainDialog && mainDialog.isShowing()) {
                mainDialog.dismiss();

            }
        } catch (Exception e) {
            CommonFrameworkUtil.showException(e);
        }
        mainDialog = null;

    }

    public static class Builder {
        private final Context context;
        private CharSequence message;
        private boolean indeterminate, cancel, cancelable;
        private OnCancelListener cancelListener;
        private int customThemeOrColorCode;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(CharSequence message) {
            this.message = message;
            return this;
        }

        public Builder setIndeterminate(boolean indeterminate) {
            this.indeterminate = indeterminate;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean cancel) {
            this.cancel = cancel;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener cancelListener) {
            this.cancelListener = cancelListener;
            return this;
        }

        public Builder setCustomThemeColor(int customThemeOrColorCode) {
            this.customThemeOrColorCode = customThemeOrColorCode;
            return this;
        }


        public LoadingDialog build() {
            LoadingDialog loadingDialog = new LoadingDialog(context);
            show(this);
            return loadingDialog;
        }
    }
}

