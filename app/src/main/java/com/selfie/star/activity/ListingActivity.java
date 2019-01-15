package com.selfie.star.activity;


import android.content.Intent;
import android.text.TextUtils;
import android.view.MenuItem;

import com.base.commonframework.activity.BaseActivity;
import com.base.commonframework.baselibrary.recyclerview.AbstractRecyclerAdapter;
import com.base.commonframework.baselibrary.utility.ValidationUtils;
import com.base.commonframework.baselibrary.view.WrapContentLinearLayoutManager;
import com.base.commonframework.utility.CommonFrameworkUtil;
import com.girnarsoft.tracking.event.EventInfo;
import com.selfie.star.R;
import com.selfie.star.activity.adapter.CustomAdapter;
import com.selfie.star.activity.model.Category;
import com.selfie.star.databinding.ActivityListingBinding;
import com.selfie.star.imageditor.EditImageActivity;
import com.selfie.star.navigation.IntentHelper;
import com.selfie.star.util.AppUtil;

import org.parceler.Parcels;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.ViewDataBinding;

public class ListingActivity extends BaseActivity {

    public static final String TAG = ListingActivity.class.getSimpleName();
    private ActivityListingBinding binding;
    private List<Category> categoryList;
    private String title;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_listing;
    }

    @Override
    public String getScreenName() {
        return TAG;
    }

    @Override
    protected void onCreate(ViewDataBinding viewDataBinding) {
        binding = (ActivityListingBinding) viewDataBinding;
        if (null != getIntent() && getIntent().hasExtra("data")) {
            categoryList = Parcels.unwrap(Objects.requireNonNull(getIntent().getExtras()).getParcelable("data"));
            title = getIntent().getExtras().getString("title");
        }
        setToolbar();
        setUpData();

    }

    private void setUpData() {
        if (ValidationUtils.isListNotEmpty(categoryList)) {
            Collections.sort(categoryList);
            binding.recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
            CustomAdapter customAdapter = new CustomAdapter(this, categoryList);
            binding.recyclerView.setAdapter(customAdapter);
            customAdapter.setOnItemClickListener((AbstractRecyclerAdapter.OnItemViewClickListener) (v, viewModel) -> {
                Category subCategory = (Category) viewModel;
                sendAnalyticEvent(subCategory);
                String tag = AppUtil.getTag(this, getIntent());
                if (ValidationUtils.isListNotEmpty(subCategory.getSubCategories())) {
                    if (!TextUtils.isEmpty(tag) && tag.equalsIgnoreCase(EditImageActivity.TAG)) {
                        startActivityForResult(IntentHelper.getInstance().newListIntent(this, subCategory.getName(), subCategory.getSubCategories(), tag), 1919);
                    } else {
                        startActivity(IntentHelper.getInstance().newListIntent(this, subCategory.getName(), subCategory.getSubCategories(), tag));
                    }

                } else {
                    if (!TextUtils.isEmpty(tag) && tag.equalsIgnoreCase(EditImageActivity.TAG)) {
                        startActivityForResult(IntentHelper.getInstance().newDetailIntent(this, subCategory.getName(), subCategory, tag),1919);
                    } else {
                        startActivity(IntentHelper.getInstance().newDetailIntent(this, subCategory.getName(), subCategory, tag));
                    }

                }

            });
        }
    }


    private void sendAnalyticEvent(Category category) {
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("Id", String.valueOf(category.getId()));
        stringHashMap.put("Name", category.getName());
        getAnalyticsManager().pushEvent(getEventTrackEventInfo()
                .withEventName(EventInfo.EventName.BUTTON_CLICK)
                .withEventAction(EventInfo.EventAction.CLICKED)
                .withEventCategory(EventInfo.EventCategory.SUB_LISTING)
                .withParamMap(stringHashMap)
                .build());
    }

    @Override
    public void onBackPressed() {
        super.finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.backslide_right_out);
    }


    private void setToolbar() {
        Toolbar toolbarSearchActivity = findViewById(R.id.toolbar_actionbar);
        toolbarSearchActivity.setTitle(CommonFrameworkUtil.getCamelCapsName(title
        ));
        setSupportActionBar(toolbarSearchActivity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

}
