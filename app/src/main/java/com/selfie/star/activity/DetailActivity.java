package com.selfie.star.activity;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.base.commonframework.activity.BaseActivity;
import com.base.commonframework.baselibrary.dialog.LoadingDialog;
import com.base.commonframework.baselibrary.recyclerview.AbstractRecyclerAdapter;
import com.base.commonframework.baselibrary.utility.ValidationUtils;
import com.base.commonframework.network.viewcallback.AbstractViewCallback;
import com.base.commonframework.utility.CommonFrameworkUtil;
import com.girnarsoft.tracking.event.EventInfo;
import com.selfie.star.R;
import com.selfie.star.activity.adapter.CustomDetailAdapter;
import com.selfie.star.activity.model.Category;
import com.selfie.star.activity.model.ImageDtoListResponse;
import com.selfie.star.activity.model.ImagesDto;
import com.selfie.star.activity.service.IMainService;
import com.selfie.star.databinding.ActivityDetailBinding;
import com.selfie.star.imageditor.EditImageActivity;
import com.selfie.star.navigation.IntentHelper;
import com.selfie.star.util.AppUtil;

import org.parceler.Parcels;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.ViewDataBinding;

public class DetailActivity extends BaseActivity implements LoadBitmap {

    public static final String TAG = DetailActivity.class.getSimpleName();
    private ActivityDetailBinding binding;
    private Category category;
    private String title;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_detail;
    }

    @Override
    public String getScreenName() {
        return TAG;
    }


    @Override
    protected void onCreate(ViewDataBinding viewDataBinding) {
        binding = (ActivityDetailBinding) viewDataBinding;
        handleIntent();
        setToolbar();
        setUpData();
    }

    private void handleIntent() {
        if (null != getIntent() && getIntent().hasExtra("data")) {
            category = Parcels.unwrap(Objects.requireNonNull(getIntent().getExtras()).getParcelable("data"));
            title = getIntent().getExtras().getString("title");
        }
    }

    private void setUpData() {
        LoadingDialog.show(this);
        IMainService service = getLocator().getService(IMainService.class);
        if (null != service) {
            service.getDetailData(this, category.getId(), new AbstractViewCallback<ImageDtoListResponse>() {
                @Override
                public void onSuccess(ImageDtoListResponse imageDtoListResponse) {
                    LoadingDialog.dismissDialog();
                    List<ImagesDto> imagesDtoList = imageDtoListResponse.getImagesDto();
                    if (ValidationUtils.isListNotEmpty(imagesDtoList)) {
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        binding.tvNoDataFound.setVisibility(View.GONE);

                        setUpView(imagesDtoList);
                    } else {
                        binding.tvNoDataFound.setVisibility(View.VISIBLE);
                        binding.recyclerView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Throwable e) {
                    super.onFailure(e);
                    showSnackBar(getString(R.string.error_msg));
                }

                @Override
                public boolean isLive() {
                    return !isFinishing();
                }
            });
        }
    }

    private void setUpView(List<ImagesDto> imagesDtoList) {
        CommonFrameworkUtil.setRecyclerGridProperties(this, binding.recyclerView);
        CustomDetailAdapter customAdapter = new CustomDetailAdapter(this, imagesDtoList);
        binding.recyclerView.setAdapter(customAdapter);
        customAdapter.setOnItemClickListener((AbstractRecyclerAdapter.OnItemViewClickListener) (v, viewModel) -> {
            ImagesDto imagesDto = (ImagesDto) viewModel;
            sendAnalyticEvent(imagesDto);
            new AsyncTaskLoadImage(this,this::onBitmapLoaded).execute(imagesDto.getImgUrl());
        });

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
        toolbarSearchActivity.setTitle(CommonFrameworkUtil.getCamelCapsName(title));
        setSupportActionBar(toolbarSearchActivity);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    private void sendAnalyticEvent(ImagesDto category) {
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("id", String.valueOf(category.getId()));
        stringHashMap.put("categoryId", String.valueOf(category.getCategoryId()));
        stringHashMap.put("imageUrl", category.getImgUrl());
        getAnalyticsManager().pushEvent(getEventTrackEventInfo()
                .withEventName(EventInfo.EventName.BUTTON_CLICK)
                .withEventAction(EventInfo.EventAction.CLICKED)
                .withEventCategory(EventInfo.EventCategory.DETAIL)
                .withParamMap(stringHashMap)
                .build());
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap) {
        String tag = AppUtil.getTag(this, getIntent());
        if (!TextUtils.isEmpty(tag) && tag.equalsIgnoreCase(EditImageActivity.TAG)) {
           setResult(RESULT_OK);
            finish();
        } else {
            startActivity(IntentHelper.getInstance().newImageEditorIntent(this));
        }
        EditImageActivity.setBitmap(bitmap);
    }

    static class AsyncTaskLoadImage extends AsyncTask<String, String, Bitmap> {
        private Context context;
        private LoadBitmap loadBitmap;

        public AsyncTaskLoadImage(Context context,LoadBitmap loadBitmap) {
            this.loadBitmap = loadBitmap;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingDialog.show(context);

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(params[0]);
                bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());
            } catch (IOException e) {
                CommonFrameworkUtil.showException(e);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            LoadingDialog.dismissDialog();
            loadBitmap.onBitmapLoaded(bitmap);

        }
    }


}

interface LoadBitmap{
    void onBitmapLoaded(Bitmap bitmap);
}
