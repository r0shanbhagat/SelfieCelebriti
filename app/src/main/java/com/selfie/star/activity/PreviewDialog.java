package com.selfie.star.activity;

import android.content.Context;

import com.base.commonframework.baselibrary.dialog.AbstractBaseDialog;
import com.base.commonframework.baselibrary.imageloader.ImageLoader;
import com.base.commonframework.utility.CommonFrameworkUtil;
import com.selfie.star.R;
import com.selfie.star.databinding.ImageLayoutBinding;

import androidx.databinding.ViewDataBinding;


public class PreviewDialog extends AbstractBaseDialog {

    private Context mContext;
    private String imagePath;

    public PreviewDialog(Context mContext, String imagePath) {
        super(mContext, true, true, true);
        this.mContext = mContext;
        this.imagePath = imagePath;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.image_layout;
    }

    @Override
    protected void create(ViewDataBinding viewDataBinding) {
        CommonFrameworkUtil.setFullScreenDialog(getContext(), this);
        ImageLayoutBinding dialogImagePreviewBinding = (ImageLayoutBinding) viewDataBinding;
        dialogImagePreviewBinding.setPreviewDialog(this);
        //  mPhotoEditorView.getSource().setImageURI(Uri.fromFile(new File(imagePath)));
       ImageLoader.loadImage(mContext,imagePath, dialogImagePreviewBinding.ivPreview, R.drawable.banner_default_image);
        dialogImagePreviewBinding.ivPreview.invalidate();
    }

    public void onDismiss() {
        dismiss();
    }

    public void onShare() {
        CommonFrameworkUtil.shareImage(getContext(), imagePath);
    }
}
