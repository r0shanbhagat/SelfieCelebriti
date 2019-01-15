package com.base.commonframework.baselibrary.dialog;

import android.view.View;

public interface IDialogClick<T> {
    void dialogMessageCallback(View view, T objectText, int requestCode);
}
