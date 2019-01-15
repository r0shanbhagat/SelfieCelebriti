package com.selfie.star.activity;

import android.view.MenuItem;
import android.view.View;

import com.base.commonframework.activity.BaseActivity;
import com.base.commonframework.baselibrary.recyclerview.AbstractRecyclerAdapter;
import com.base.commonframework.baselibrary.utility.ValidationUtils;
import com.base.commonframework.utility.CommonFrameworkUtil;
import com.selfie.star.R;
import com.selfie.star.activity.adapter.ShowImageAdapter;
import com.selfie.star.databinding.ActivityShowImageBinding;
import com.selfie.star.util.AppUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.ViewDataBinding;

public class ShowImageActivity extends BaseActivity {

    public static final String TAG = ShowImageActivity.class.getSimpleName();
    private ActivityShowImageBinding binding;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_show_image;
    }

    @Override
    public String getScreenName() {
        return TAG;
    }

    @Override
    protected void onCreate(ViewDataBinding viewDataBinding) {
        binding = (ActivityShowImageBinding) viewDataBinding;
        setUpData();
    }

    private void setUpData() {
        List<String> imagePathList = getImagePath();
        if (ValidationUtils.isListNotEmpty(imagePathList)) {
            binding.tvNoDataFound.setVisibility(View.GONE);
            Collections.sort(imagePathList, Collections.reverseOrder());
            CommonFrameworkUtil.setRecyclerGridProperties(this, binding.recyclerView);
            ShowImageAdapter customAdapter = new ShowImageAdapter(this, imagePathList);
            binding.recyclerView.setAdapter(customAdapter);
            customAdapter.setOnItemClickListener((AbstractRecyclerAdapter.OnItemViewClickListener) (v, viewModel) -> {
                PreviewDialog previewDialog = new PreviewDialog(this, (String) viewModel);
                previewDialog.show();

            });
        }

       /* Dialog settingsDialog = new Dialog(this);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout, null));
        settingsDialog.show();*/


    }

    @Override
    protected void onActivityReady() {
        super.onActivityReady();
        Toolbar toolbarSearchActivity = findViewById(R.id.toolbar_actionbar);
        toolbarSearchActivity.setTitle("");
        setSupportActionBar(toolbarSearchActivity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    private List<String> getImagePath() {
        List<String> imagePathList = new ArrayList();
        File[] listFile;
        File directoryFile = new File(AppUtil.getDirectoryPath(this));
        if (directoryFile.isDirectory()) {
            listFile = directoryFile.listFiles();
            for (File file : listFile) {
                imagePathList.add(file.getAbsolutePath());
            }
        }
        return imagePathList;
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

}
