package com.base.commonframework.baselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

import com.base.commonframework.R;
import com.base.commonframework.baselibrary.utility.AnimationUtility;
import com.base.commonframework.utility.CommonFrameworkUtil;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Created by ankit on 28/3/17.
 */
public abstract class AbstractBaseDialog extends Dialog {

    private boolean isCancelOnTouchOutside, isCancelable, isAnimated;
    private ViewDataBinding viewDataBinding;

    /**
     * Instantiates a new Abstract base dialog.
     *
     * @param context the m context
     */
    public AbstractBaseDialog(Context context) {
        super(context, R.style.DialogTheme);
    }

    /**
     * Instantiates a new Abstract base dialog.
     *
     * @param context    the context
     * @param themeResId the theme res id
     */
    public AbstractBaseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    /**
     * Instantiates a new Abstract base dialog.
     *
     * @param context                the context
     * @param themeResId             the theme res id
     * @param isCancelOnTouchOutside the is cancel on touch outside
     * @param isCancelable           the is cancelable
     * @param isAnimated             the is animated
     */
    public AbstractBaseDialog(Context context, int themeResId, boolean isCancelOnTouchOutside, boolean isCancelable, boolean isAnimated) {
        super(context, R.style.DialogTheme);
        setDialogProperty(isCancelOnTouchOutside, isCancelable, isAnimated);
    }

    public AbstractBaseDialog(Context context, boolean isCancelOnTouchOutside, boolean isCancelable, boolean isAnimated) {
        super(context, R.style.DialogTheme);
        setDialogProperty(isCancelOnTouchOutside, isCancelable, isAnimated);
    }

    private void setDialogProperty(boolean isCancelOnTouchOutside, boolean isCancelable, boolean isAnimated) {
        this.isCancelOnTouchOutside = isCancelOnTouchOutside;
        this.isCancelable = isCancelable;
        this.isAnimated = isAnimated;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), getLayoutResourceId(), null, false);
        setContentView(viewDataBinding.getRoot());
        if (isAnimated) {
            AnimationUtility.setDialogAnimationEnter(this);
        }
        setCanceledOnTouchOutside(isCancelOnTouchOutside);
        setCancelable(isCancelable);
        CommonFrameworkUtil.setDialogHeightWidth(getContext(), this);
        create(viewDataBinding);
    }


    @Override
    protected void onStop() {
        dismiss();
       /* if (isAnimated) {
            AnimationUtility.setDialogAnimationExit(this);
        }*/
        super.onStop();
    }


    /**
     * Gets layout resource id.
     *
     * @return the layout resource id
     */
    protected abstract int getLayoutResourceId();

    /**
     * Create.
     *
     * @param viewDataBinding the view data binding
     */
    protected abstract void create(ViewDataBinding viewDataBinding);

}
