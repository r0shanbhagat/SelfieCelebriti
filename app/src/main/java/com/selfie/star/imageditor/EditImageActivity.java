package com.selfie.star.imageditor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.base.commonframework.utility.CommonFrameworkConstant;
import com.base.commonframework.utility.CommonFrameworkUtil;
import com.selfie.star.R;
import com.selfie.star.activity.CustomDialog;
import com.selfie.star.imageditor.background.BackgroundListener;
import com.selfie.star.imageditor.background.BackgroundViewAdapter;
import com.selfie.star.imageditor.base.BaseActivity;
import com.selfie.star.imageditor.filters.FilterListener;
import com.selfie.star.imageditor.filters.FilterViewAdapter;
import com.selfie.star.imageditor.tools.EditingToolsAdapter;
import com.selfie.star.imageditor.tools.ToolType;
import com.selfie.star.navigation.IntentHelper;
import com.selfie.star.util.AppUtil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.alhazmy13.imagefilter.ImageFilter;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.SaveSettings;
import ja.burhanrashid52.photoeditor.ViewType;

import static ja.burhanrashid52.photoeditor.PhotoFilter.NONE;

public class EditImageActivity extends BaseActivity implements OnPhotoEditorListener,
        View.OnClickListener, ViewGroup.OnHierarchyChangeListener,
        PropertiesBSFragment.Properties,
        EmojiBSFragment.EmojiListener, OnDialogCallback,
        StickerBSFragment.StickerListener, EditingToolsAdapter.OnItemSelected, FilterListener, BackgroundListener {

    public static final String EXTRA_IMAGE_PATHS = "extra_image_paths";
    public static final int REQUEST_PERMISSION = 200;
    public static final String TAG = EditImageActivity.class.getSimpleName();
    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;
    private static Bitmap bitmap;
    private Bitmap sourceBitmap;
    // private Uri cameraImageUri;
    private String imageFilePath = "";
    private PhotoEditor mPhotoEditor;
    private PhotoEditorView mPhotoEditorView;
    private PropertiesBSFragment mPropertiesBSFragment;
    private EmojiBSFragment mEmojiBSFragment;
    private StickerBSFragment mStickerBSFragment;
    private TextView mTxtCurrentTool;
    private Typeface mWonderFont;
    private RecyclerView mRvTools, mRvFilters;
    private EditingToolsAdapter mEditingToolsAdapter = new EditingToolsAdapter(this);
    private FilterViewAdapter mFilterViewAdapter = new FilterViewAdapter(this);
    private BackgroundViewAdapter mBackgroundViewAdapter = new BackgroundViewAdapter(this);
    private ConstraintLayout mRootView;
    private ConstraintSet mConstraintSet = new ConstraintSet();
    private boolean mIsFilterVisible;

    public static void setBitmap(Bitmap bit) {
        bitmap = bit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_edit_image);
        initViews();

        mWonderFont = Typeface.createFromAsset(getAssets(), "beyond_wonderland.ttf");

        mPropertiesBSFragment = new PropertiesBSFragment();
        mEmojiBSFragment = new EmojiBSFragment();
        mStickerBSFragment = new StickerBSFragment();
        mStickerBSFragment.setStickerListener(this);
        mEmojiBSFragment.setEmojiListener(this);
        mPropertiesBSFragment.setPropertiesChangeListener(this);

        LinearLayoutManager llmTools = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvTools.setLayoutManager(llmTools);
        mRvTools.setAdapter(mEditingToolsAdapter);

        LinearLayoutManager llmFilters = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvFilters.setLayoutManager(llmFilters);
        //mRvFilters.setAdapter(mFilterViewAdapter);


        //Typeface mTextRobotoTf = ResourcesCompat.getFont(this, R.font.roboto_medium);
        //Typeface mEmojiTypeFace = Typeface.createFromAsset(getAssets(), "emojione-android.ttf");
        mPhotoEditorView.setOnPhotoEditorListener(this);

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true) // set flag to make text scalable when pinch
                //.setDefaultTextTypeface(mTextRobotoTf)
                //.setDefaultEmojiTypeface(mEmojiTypeFace)
                .build(); // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this);

        if (null != bitmap) {
            mPhotoEditor.addImage(bitmap, ViewType.IMAGE);
        }

        mPhotoEditorView.setOnHierarchyChangeListener(this);

        //Set Image Dynamically
        //  mPhotoEditorView.getSource().setImageResource(R.drawable.banner_default_image);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }

    }

    private void initViews() {
        ImageView imgUndo;
        ImageView imgRedo;
        ImageView imgCamera;
        ImageView imgGallery;
        ImageView imgSave;
        ImageView imgClose;
        ImageView imgShare;

        mPhotoEditorView = findViewById(R.id.photoEditorView);
        mTxtCurrentTool = findViewById(R.id.txtCurrentTool);
        mRvTools = findViewById(R.id.rvConstraintTools);
        mRvFilters = findViewById(R.id.rvFilterView);
        mRootView = findViewById(R.id.rootView);

        imgUndo = findViewById(R.id.imgUndo);
        imgUndo.setOnClickListener(this);

        imgRedo = findViewById(R.id.imgRedo);
        imgRedo.setOnClickListener(this);

        imgCamera = findViewById(R.id.imgCamera);
        imgCamera.setOnClickListener(this);

        imgGallery = findViewById(R.id.imgGallery);
        imgGallery.setOnClickListener(this);

        imgSave = findViewById(R.id.imgSave);
        imgSave.setOnClickListener(this);

        imgShare = findViewById(R.id.imgShare);
        imgShare.setOnClickListener(this);

        imgClose = findViewById(R.id.imgClose);
        imgClose.setOnClickListener(this);

    }

    @Override
    public void onEditTextChangeListener(final View rootView,String fontPath, String text, int colorCode) {
        TextEditorDialogFragment textEditorDialogFragment =
                TextEditorDialogFragment.show(this, fontPath,text, colorCode);
        textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
            @Override
            public void onDone(String inputText, int colorCode,String fontPath) {
                mPhotoEditor.editText(rootView, inputText, colorCode,fontPath);
                mTxtCurrentTool.setText(R.string.label_text);
            }
        });
    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(int numberOfAddedViews) {
        Log.d(TAG, "onRemoveViewListener() called with: numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
        handleViewLayer();
    }

    private void handleViewLayer() {
        mPhotoEditorView.bringToFront();
        bringViewFront(ViewType.IMAGE);
        bringViewFront(ViewType.STICKER);
        bringViewFront(ViewType.EMOJI);
        bringViewFront(ViewType.TEXT);
    }

    private void bringViewFront(ViewType viewType) {
        View view = mPhotoEditorView.findViewWithTag(viewType);
        if (null != view) {
            view.bringToFront();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imgUndo:
                mPhotoEditor.undo();
                break;

            case R.id.imgRedo:
                mPhotoEditor.redo();
                break;

            case R.id.imgSave:
                saveImage(false);
                break;
            case R.id.imgShare:
                saveImage(true);
                break;

            case R.id.imgClose:
                onBackPressed();
                break;


        }
    }

    @SuppressLint("MissingPermission")
    private void saveImage(boolean isShare) {
        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showLoading("Saving...");

            File dir = new File(AppUtil.getDirectoryPath(EditImageActivity.this));
            if (!dir.exists()) {
                dir.mkdir();
            }

            File file = new File(dir + File.separator + System.currentTimeMillis() + ".png");
            try {
                file.createNewFile();

                SaveSettings saveSettings = new SaveSettings.Builder()
                        .setClearViewsEnabled(true)
                        .setTransparencyEnabled(true)
                        .build();

                mPhotoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                        hideLoading();
                        showSnackbar("Image Saved Successfully");
                        mPhotoEditorView.getSource().setImageURI(Uri.fromFile(new File(imagePath)));
                        if (isShare) {
                            CommonFrameworkUtil.shareImage(EditImageActivity.this, imagePath);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        hideLoading();
                        showSnackbar("Failed to save Image");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                hideLoading();
                showSnackbar(e.getMessage());
            }
        }
    }


    private void startCropActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setAutoZoomEnabled(true)
                .setBackgroundColor(R.color.colorAccent)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);


    }

    @Override
    public void onColorChanged(int colorCode) {
        mPhotoEditor.setBrushColor(colorCode);
        mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onOpacityChanged(int opacity) {
        mPhotoEditor.setOpacity(opacity);
        mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onBrushSizeChanged(int brushSize) {
        mPhotoEditor.setBrushSize(brushSize);
        mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onEmojiClick(String emojiUnicode) {
        mPhotoEditor.addEmoji(emojiUnicode);
        mTxtCurrentTool.setText(R.string.label_emoji);

    }

    @Override
    public void onStickerClick(Bitmap bitmap) {
        mPhotoEditor.addImage(bitmap, ViewType.STICKER);
        mTxtCurrentTool.setText(R.string.label_sticker);
    }

    @Override
    public void isPermissionGranted(boolean isGranted, String permission) {
        if (isGranted) {
            saveImage(false);
        }
    }

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you want to exit without saving image ?");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveImage(false);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();

    }

    @Override
    public void onFilterSelected(PhotoFilter photoFilter) {
        //mPhotoEditor.setFilterEffect(photoFilter);
        ImageFilter.Filter filter = null;
        switch (photoFilter) {
            case AVERAGE_BLUR:
                filter = ImageFilter.Filter.AVERAGE_BLUR;
                break;
            case BLOCK:
                filter = ImageFilter.Filter.BLOCK;
                break;
            case GAUSSIAN_BLUR:
                filter = ImageFilter.Filter.GAUSSIAN_BLUR;
                break;
            case GOTHAM:
                filter = ImageFilter.Filter.GOTHAM;
                break;
            case GRAY:
                filter = ImageFilter.Filter.GRAY;
                break;
            case HDR:
                filter = ImageFilter.Filter.HDR;
                break;
            case INVERT:
                filter = ImageFilter.Filter.INVERT;
                break;
            case LIGHT:
                filter = ImageFilter.Filter.LIGHT;
                break;
            case LOMO:
                filter = ImageFilter.Filter.LOMO;
                break;
            case MOTION_BLUR:
                filter = ImageFilter.Filter.MOTION_BLUR;
                break;
            case NEON:
                filter = ImageFilter.Filter.NEON;
                break;
            case OIL:
                filter = ImageFilter.Filter.OIL;
                break;
            case OLD:
                filter = ImageFilter.Filter.OLD;
                break;
            case PIXELATE:
                filter = ImageFilter.Filter.PIXELATE;
                break;
            case RELIEF:
                filter = ImageFilter.Filter.RELIEF;
                break;
            case SHARPEN:
                filter = ImageFilter.Filter.SHARPEN;
                break;
            case SKETCH:
                filter = ImageFilter.Filter.SKETCH;
                break;
            case SOFT_GLOW:
                filter = ImageFilter.Filter.SOFT_GLOW;
                break;
            case TV:
                filter = ImageFilter.Filter.TV;
                break;

        }
        if (photoFilter == NONE) {
            mPhotoEditorView.getSource().setImageBitmap(sourceBitmap);
            return;
        }
        if (null != filter && null != sourceBitmap) {
            Bitmap bitmap = ImageFilter.applyFilter(sourceBitmap, filter);
            mPhotoEditorView.getSource().setImageBitmap(bitmap);
        }

    }


    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {
            case BACKGROUND:
                mTxtCurrentTool.setText(R.string.label_background);
                mRvFilters.setAdapter(mBackgroundViewAdapter);
                showFilter(true);
                break;
            case BRUSH:
                mPhotoEditor.setBrushDrawingMode(true);
                mTxtCurrentTool.setText(R.string.label_brush);
                mPropertiesBSFragment.show(getSupportFragmentManager(), mPropertiesBSFragment.getTag());
                break;
            case ADDSTAR:
                startActivityForResult(IntentHelper.getInstance().newMainIntent(this, TAG), CommonFrameworkConstant.ADD_STAR);
                break;
            case ADDPHOTO:
                new CustomDialog(this, this).show();
                break;
            case TEXT:
                TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this);
                textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
                    @Override
                    public void onDone(String inputText, int colorCode,String fontPath) {
                        mPhotoEditor.addText(inputText, colorCode,fontPath);
                        mTxtCurrentTool.setText(R.string.label_text);
                    }
                });
                break;
            case ERASER:
                mPhotoEditor.brushEraser();
                mTxtCurrentTool.setText(R.string.label_eraser);
                break;
            case FILTER:
                mTxtCurrentTool.setText(R.string.label_filter);
                mRvFilters.setAdapter(mFilterViewAdapter);
                showFilter(true);
                break;
            case EMOJI:
                mEmojiBSFragment.show(getSupportFragmentManager(), mEmojiBSFragment.getTag());
                break;
            case STICKER:
                mStickerBSFragment.show(getSupportFragmentManager(), mStickerBSFragment.getTag());
                break;
        }
    }

    void showFilter(boolean isVisible) {
        mIsFilterVisible = isVisible;
        mConstraintSet.clone(mRootView);

        if (isVisible) {
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
        } else {
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.END);
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(mRootView, changeBounds);

        mConstraintSet.applyTo(mRootView);

    }

    @Override
    public void onBackgroundSelected(String image) {
        Drawable drawable = AppUtil.getDrawableFromAsset(this, image);
        if (null != drawable && null != mPhotoEditorView) {
            mPhotoEditorView.setBackground(drawable);
        }
    }

    @Override
    public void onBackPressed() {
        if (mIsFilterVisible) {
            showFilter(false);
            mTxtCurrentTool.setText(R.string.app_name);
        } else if (!mPhotoEditor.isCacheEmpty()) {
            showSaveDialog();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onCameraClick() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            openCameraIntent();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = createImageFile();
                Uri photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(pictureIntent, CAMERA_REQUEST);
            } catch (Exception e) {
                CommonFrameworkUtil.showException(e);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                   /* Bitmap photo = (Bitmap) data.getExtras().get("data");
                    cameraImageUri = getCameraImageUri(photo);
                    if (null == cameraImageUri) {
                        return;
                    }*/

                    startCropActivity(Uri.fromFile(new File(imageFilePath)));
                    //mPhotoEditor.clearAllViews();
                    //Bitmap photo = (Bitmap) data.getExtras().get("data");
                    // mPhotoEditorView.getSource().setImageBitmap(photo);
                    break;
                case PICK_REQUEST:
                    Uri uri = data.getData();
                    startCropActivity(uri);
                   /* try {
                        //  mPhotoEditor.clearAllViews();
                        Uri uri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        mPhotoEditorView.getSource().setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    try {
                        CropImage.ActivityResult result = CropImage.getActivityResult(data);
                        Uri resultUri = result.getUri();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                        sourceBitmap = bitmap;
                        mPhotoEditorView.getSource().setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case CommonFrameworkConstant.ADD_STAR: {
                    View view = mPhotoEditorView.findViewWithTag(ViewType.IMAGE);
                    if (null != bitmap && null != view) {
                        mPhotoEditorView.removeView(view);
                        mPhotoEditor.addImage(bitmap, ViewType.IMAGE);
                    }
                }
                break;
            }
        }
    }


    @Override
    public void onGalleryClick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST);

    }

    private File createImageFile() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        storageDir.mkdirs();
        File image = new File(storageDir, "image_001.jpg");
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        handleViewLayer();
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {

    }


}
