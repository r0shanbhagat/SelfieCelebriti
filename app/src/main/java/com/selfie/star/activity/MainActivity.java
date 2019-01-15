package com.selfie.star.activity;


import android.content.Intent;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.base.commonframework.activity.BaseActivity;
import com.base.commonframework.baselibrary.dialog.LoadingDialog;
import com.base.commonframework.baselibrary.recyclerview.AbstractRecyclerAdapter;
import com.base.commonframework.baselibrary.utility.ValidationUtils;
import com.base.commonframework.baselibrary.view.WrapContentLinearLayoutManager;
import com.base.commonframework.network.viewcallback.AbstractViewCallback;
import com.base.commonframework.utility.CommonFrameworkConstant;
import com.base.commonframework.utility.CommonFrameworkUtil;
import com.girnarsoft.tracking.event.EventInfo;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.selfie.star.R;
import com.selfie.star.activity.adapter.BannerSliderAdapter;
import com.selfie.star.activity.adapter.CustomAdapter;
import com.selfie.star.activity.model.Category;
import com.selfie.star.activity.model.CategoryResponseData;
import com.selfie.star.activity.service.IMainService;
import com.selfie.star.databinding.ActivityMainBinding;
import com.selfie.star.imageditor.EditImageActivity;
import com.selfie.star.navigation.IntentHelper;
import com.selfie.star.util.AppUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.ViewDataBinding;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    protected boolean flag_temp = false;
    protected long back_pressed;
    private  String tag;


    @Override
    protected int getActivityLayout() {
        return R.layout.activity_main;
    }

    @Override
    public String getScreenName() {
        return TAG;
    }

    @Override
    protected void onCreate(ViewDataBinding viewDataBinding) {
        binding = (ActivityMainBinding) viewDataBinding;
         tag = AppUtil.getTag(this, getIntent());
        setDrawerLayout();
        setUpBannerData();
        setUpData();
    }

    private void setDrawerLayout() {
        Toolbar toolbar = binding.appbar.toolbarActionbar;
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void setUpBannerData() {
        List<String> bannerImageUrlList = new ArrayList<>();
        bannerImageUrlList.add(String.valueOf("https://www.thehansindia.com/assets/sho_1984.jpg"));
        bannerImageUrlList.add(String.valueOf("https://www.thehansindia.com/assets/kama_1653.jpg"));
        bannerImageUrlList.add(String.valueOf("http://assets-news-bcdn.dailyhunt.in/cmd/resize/400x400_60/fetchdata13/images/99/28/3d/99283d15b9ced7e84aa1270c6aa11134.jpg"));
        bannerImageUrlList.add(String.valueOf("https://www.thehansindia.com/assets/RS_8345.jpg"));
        bannerImageUrlList.add(String.valueOf("https://www.newsworldindia.in/public/media_uploads/mediakha6Rajkummar%20Banner.jpg"));
        bannerImageUrlList.add(String.valueOf("https://upclosed.com/media/images/covers/A/ajay-devgan.jpg"));
        bannerImageUrlList.add(String.valueOf("https://assets-news-bcdn.dailyhunt.in/cmd/resize/400x400_60/fetchdata13/images/b4/a1/dc/b4a1dc11f9b42ae6b18e128ed8035574.jpg"));

        binding.appbar.content.bannerSlider.setAdapter(new BannerSliderAdapter(bannerImageUrlList));
        //   binding.bannerSlider.setOnSlideClickListener(position -> AppUtils.onHomeMenuItemClickEvent(HomeActivity.this, bannerModelList.get(position)));


    }

    private void setUpData() {
        LoadingDialog.show(this);
        IMainService service = getLocator().getService(IMainService.class);
        if (null != service) {
            service.getListData(this, new AbstractViewCallback<CategoryResponseData>() {
                @Override
                public void onSuccess(CategoryResponseData customModel) {
                    LoadingDialog.dismissDialog();
                    List<Category> categoryList = customModel.getCategories();
                    if (ValidationUtils.isListNotEmpty(categoryList)) {
                        setUpView(categoryList);
                        binding.appbar.content.tvNoDataFound.setVisibility(View.GONE);
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

    private void setUpView(List<Category> categoryList) {
        binding.appbar.content.recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
        Collections.sort(categoryList);
        CustomAdapter customAdapter = new CustomAdapter(this, categoryList);
        binding.appbar.content.recyclerView.setAdapter(customAdapter);
        customAdapter.setOnItemClickListener((AbstractRecyclerAdapter.OnItemViewClickListener) (v, viewModel) -> {
            Category subCategory = (Category) viewModel;
            sendAnalyticEvent(subCategory);
            if (ValidationUtils.isListNotEmpty(subCategory.getSubCategories())) {
                if (!TextUtils.isEmpty(tag) && tag.equalsIgnoreCase(EditImageActivity.TAG)) {
                    startActivityForResult(IntentHelper.getInstance().newListIntent(this, subCategory.getName(), subCategory.getSubCategories(), tag), CommonFrameworkConstant.ADD_STAR);

                } else {
                    startActivity(IntentHelper.getInstance().newListIntent(this, subCategory.getName(), subCategory.getSubCategories(), tag));
                }
            } else {
                if (!TextUtils.isEmpty(tag) && tag.equalsIgnoreCase(EditImageActivity.TAG)) {
                    startActivityForResult(IntentHelper.getInstance().newDetailIntent(this, subCategory.getName(), subCategory, tag),CommonFrameworkConstant.ADD_STAR);
                } else {
                    startActivity(IntentHelper.getInstance().newDetailIntent(this, subCategory.getName(), subCategory, tag));

                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode);
        finish();

    }

    private void sendAnalyticEvent(Category category) {
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("Id", String.valueOf(category.getId()));
        stringHashMap.put("Name", category.getName());
        getAnalyticsManager().pushEvent(getEventTrackEventInfo()
                .withEventName(EventInfo.EventName.BUTTON_CLICK)
                .withEventAction(EventInfo.EventAction.CLICKED)
                .withEventCategory(EventInfo.EventCategory.LISTING)
                .withParamMap(stringHashMap)
                .build());
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (!CommonFrameworkUtil.isActivityRunning(this, intent)) {
                startActivity(intent);
            }
        } else if (id == R.id.nav_about_us) {
            startActivity(IntentHelper.getInstance().newAboutUsIntent(this));

        } else if (id == R.id.nav_rate) {

            AppUtil.openPlayStore(this);

        } else if (id == R.id.nav_share) {
            AppUtil.shareApp(this);
        } else if (id == R.id.nav_show_image) {
            startActivity(IntentHelper.getInstance().newShowImageIntent(this));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!TextUtils.isEmpty(tag) && tag.equalsIgnoreCase(EditImageActivity.TAG)) {
                super.onBackPressed();
                return;
            }
            if (!flag_temp) {
                Snackbar snackbar = Snackbar.make(drawer, "Press Back again to exit", Snackbar.LENGTH_SHORT);
                snackbar.show();
                flag_temp = true;
            } else if (back_pressed + 2000 > System.currentTimeMillis() && flag_temp) {
                super.onBackPressed();
            } else {
                flag_temp = false;
            }
            back_pressed = System.currentTimeMillis();
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.backslide_right_out);
    }


}
