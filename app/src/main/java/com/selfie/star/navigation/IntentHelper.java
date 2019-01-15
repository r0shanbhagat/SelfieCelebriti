package com.selfie.star.navigation;

import android.content.Context;
import android.content.Intent;

import com.selfie.star.R;
import com.selfie.star.activity.AboutUsActivity;
import com.selfie.star.activity.DetailActivity;
import com.selfie.star.activity.ListingActivity;
import com.selfie.star.activity.MainActivity;
import com.selfie.star.activity.ShowImageActivity;
import com.selfie.star.activity.model.Category;
import com.selfie.star.imageditor.EditImageActivity;

import org.parceler.Parcels;

import java.util.List;


public class IntentHelper {

    private static IntentHelper intentHelper;

    public static IntentHelper getInstance() {
        if (null == intentHelper) {
            intentHelper = new IntentHelper();
        }
        return intentHelper;
    }

    public Intent newMainIntent(Context context, String tag) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(context.getString(R.string.tag), tag);
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }


    public Intent newListIntent(Context context, String title, List<Category> categoryList,String tag) {
        Intent intent = new Intent(context, ListingActivity.class);
        intent.putExtra("title", title);
        intent.putExtra(context.getString(R.string.tag), tag);
        intent.putExtra("data", Parcels.wrap(categoryList));
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public Intent newDetailIntent(Context context, String title, Category category, String tag) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("title", title);
        intent.putExtra(context.getString(R.string.tag), tag);
        intent.putExtra("data", Parcels.wrap(category));
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public Intent newAboutUsIntent(Context context) {
        Intent intent = new Intent(context, AboutUsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public Intent newShowImageIntent(Context context) {
        Intent intent = new Intent(context, ShowImageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public Intent newImageEditorIntent(Context context) {
        Intent intent = new Intent(context, EditImageActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

}