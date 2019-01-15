package com.selfie.star.activity;

import android.content.Context;
import android.view.View;

import com.base.commonframework.baselibrary.dialog.AbstractBaseDialog;
import com.selfie.star.R;
import com.selfie.star.databinding.DialogCustomBinding;
import com.selfie.star.imageditor.OnDialogCallback;

import androidx.databinding.ViewDataBinding;

public class CustomDialog extends AbstractBaseDialog {

    public static String TAG = CustomDialog.class.getSimpleName();
    private OnDialogCallback dialogCallback;

    public CustomDialog(Context context, OnDialogCallback dialogCallback) {
        super(context);
        this.dialogCallback = dialogCallback;
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.dialog_custom;
    }

    @Override
    protected void create(ViewDataBinding viewDataBinding) {
        DialogCustomBinding binding = (DialogCustomBinding) viewDataBinding;
        binding.setCustomDialog(this);
    }


    public void onClick(View view) {
        if (view.getId() == R.id.tvCamera) {
            if (null != dialogCallback) {
                dialogCallback.onCameraClick();
            }

        } else if (view.getId() == R.id.tvGallery) {
            if (null != dialogCallback) {
                dialogCallback.onGalleryClick();
            }
        }
        dismiss();
    }


}

