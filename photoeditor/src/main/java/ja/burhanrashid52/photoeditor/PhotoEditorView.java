package ja.burhanrashid52.photoeditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * <p>
 * This ViewGroup will have the {@link BrushDrawingView} to draw paint on it with {@link ImageView}
 * which our source image
 * </p>
 *
 * @author <a href="https://github.com/burhanrashid52">Burhanuddin Rashid</a>
 * @version 0.1.1
 * @since 1/18/2018
 */

public class PhotoEditorView extends RelativeLayout implements OnPhotoEditorListener {

    private static final String TAG = "PhotoEditorView";
    private static final int imgSrcId = 1, brushSrcId = 2, glFilterId = 3;
    View imageRootView;
    private FilterImageView mImgSource;
    private BrushDrawingView mBrushDrawingView;
    private boolean isFlip;
    private boolean freeze = false;
    private ViewGroup.LayoutParams layoutParams;
    private int baseh;
    private int basew;
    private int basex;
    private int basey;
    private int margl;
    private int margt;


    // private ImageFilterView mImageFilterView;
    private View deleteView;
    private OnPhotoEditorListener mOnPhotoEditorListener;

    public PhotoEditorView(Context context) {
        super(context);
        init(context, null);
    }

    public PhotoEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PhotoEditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PhotoEditorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @SuppressLint("Recycle")
    private void init(Context context, @Nullable AttributeSet attrs) {
        //Setup image attributes
        LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageRootView = mLayoutInflater.inflate(R.layout.view_photo_editor, null);
        mImgSource = imageRootView.findViewById(R.id.imgPhotoEditorImage);

        final ImageView imageViewFlip = imageRootView.findViewById(R.id.imgFlip);
        final ImageView imageViewStretch = imageRootView.findViewById(R.id.imgStretch);
        final FrameLayout frmBorder = imageRootView.findViewById(R.id.frmBorder);
        final ImageView imgClose = imageRootView.findViewById(R.id.imgPhotoEditorClose);


        mImgSource.setId(imgSrcId);
        // mImgSource = new FilterImageView(getContext());
        //mImgSource.setId(imgSrcId);
        //mImgSource.setAdjustViewBounds(true);
        final RelativeLayout.LayoutParams imgSrcParam = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgSrcParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PhotoEditorView);
            Drawable imgSrcDrawable = a.getDrawable(R.styleable.PhotoEditorView_photo_src);
            if (imgSrcDrawable != null) {
                mImgSource.setImageDrawable(imgSrcDrawable);
            }
        }

        //Setup brush view
        mBrushDrawingView = new BrushDrawingView(getContext());
        mBrushDrawingView.setVisibility(GONE);
        mBrushDrawingView.setId(brushSrcId);
        //Align brush to the size of image view
        RelativeLayout.LayoutParams brushParam = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        brushParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        brushParam.addRule(RelativeLayout.ALIGN_TOP, imgSrcId);
        brushParam.addRule(RelativeLayout.ALIGN_BOTTOM, imgSrcId);

        //Setup GLSurface attributes
      /*  mImageFilterView = new ImageFilterView(getContext());
        mImageFilterView.setId(glFilterId);
        mImageFilterView.setVisibility(GONE);*/

        //Align brush to the size of image view
        RelativeLayout.LayoutParams imgFilterParam = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgFilterParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        imgFilterParam.addRule(RelativeLayout.ALIGN_TOP, imgSrcId);
        imgFilterParam.addRule(RelativeLayout.ALIGN_BOTTOM, imgSrcId);

        mImgSource.setOnImageChangedListener(new FilterImageView.OnImageChangedListener() {
            @Override
            public void onBitmapLoaded(@Nullable Bitmap sourceBitmap) {
               /* mImageFilterView.setFilterEffect(PhotoFilter.NONE);
                mImageFilterView.setSourceBitmap(sourceBitmap);*/
                if (null != sourceBitmap) {
                    removeView(imageRootView);
                    addView(imageRootView, imgSrcParam);

                }
                Log.d(TAG, "onBitmapLoaded() called with: sourceBitmap = [" + sourceBitmap + "]");
            }
        });

        final MultiTouchListener multiTouchListener = getMultiTouchListener();
        imageViewStretch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                multiTouchListener.mScaleGestureDetector.onTouchEvent(view, event);
                if (!freeze) {
                    int j = (int) event.getRawX();
                    int i = (int) event.getRawY();
                    layoutParams = mImgSource.getLayoutParams();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mImgSource.invalidate();
                            basex = j;
                            basey = i;
                            basew = mImgSource.getWidth();
                            baseh = mImgSource.getHeight();
                            int[] loaction = new int[2];
                            mImgSource.getLocationOnScreen(loaction);
                            //margl = layoutParams.leftMargin;
                            //margt = layoutParams.topMargin;
                            break;
                        case MotionEvent.ACTION_MOVE:

                            float f2 = (float) Math.toDegrees(Math.atan2(i - basey, j - basex));
                            float f1 = f2;
                            if (f2 < 0.0F) {
                                f1 = f2 + 360.0F;
                            }
                            j -= basex;
                            int k = i - basey;
                            i = (int) (Math.sqrt(j * j + k * k) * Math.cos(Math.toRadians(f1
                                    - mImgSource.getRotation())));
                            j = (int) (Math.sqrt(i * i + k * k) * Math.sin(Math.toRadians(f1
                                    - mImgSource.getRotation())));
                            k = i * 2 + basew;
                            int m = j * 2 + baseh;
                            if (k > 150) {
                                layoutParams.width = k;
                                // layoutParams.leftMargin = (margl - i);
                            }
                            if (m > 150) {
                                layoutParams.height = m;
                                // layoutParams.topMargin = (margt - j);
                            }
                            mImgSource.setLayoutParams(layoutParams);
                            imageRootView.performLongClick();
                            break;
                    }
                    return true;

                }
                return freeze;
            }
        });

        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick() {
                boolean isBackgroundVisible = frmBorder.getTag() != null && (boolean) frmBorder.getTag();
                frmBorder.setBackgroundResource(isBackgroundVisible ? 0 : R.drawable.rounded_border_tv);
                imgClose.setVisibility(isBackgroundVisible ? View.GONE : View.VISIBLE);
                imageViewStretch.setVisibility(isBackgroundVisible ? View.GONE : View.VISIBLE);
                imageViewFlip.setVisibility(isBackgroundVisible ? View.GONE : View.VISIBLE);
                frmBorder.setTag(!isBackgroundVisible);
            }

            @Override
            public void onLongClick() {

            }
        });

        imgClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(imageRootView);
            }
        });
        imageViewFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlip) {
                    mImgSource.setImageDrawable(mImgSource.getDrawable());
                    isFlip = false;
                } else {
                    Bitmap bInput = mImgSource.getBitmap(), bOutput;
                    Matrix matrix = new Matrix();
                    matrix.preScale(-1.0f, 1.0f);
                    bOutput = Bitmap.createBitmap(bInput, 0, 0, bInput.getWidth(), bInput.getHeight(), matrix, true);
                    mImgSource.setImageBitmap(bOutput);
                    isFlip = true;
                }


            }
        });


        imageRootView.setOnTouchListener(multiTouchListener);


        //Add image source
        imageRootView.setTag(ViewType.CAMERA_GALLERY_IMAGE);
        //addView(imageRootView, imgSrcParam);


        //Add Gl FilterView
        // addView(mImageFilterView, imgFilterParam);

        //Add brush view
        addView(mBrushDrawingView, brushParam);


    }

    @NonNull
    private MultiTouchListener getMultiTouchListener() {
        MultiTouchListener multiTouchListener = new MultiTouchListener(
                deleteView,
                this,
                mImgSource,
                true,
                this);

        //multiTouchListener.setOnMultiTouchListener(this);

        return multiTouchListener;
    }


    /**
     * Source image which you want to edit
     *
     * @return source ImageView
     */
    public ImageView getSource() {
        return mImgSource;
    }

    BrushDrawingView getBrushDrawingView() {
        return mBrushDrawingView;
    }

    /**
     * Callback on editing operation perform on {@link PhotoEditorView}
     *
     * @param onPhotoEditorListener {@link OnPhotoEditorListener}
     */
    public void setOnPhotoEditorListener(@NonNull OnPhotoEditorListener onPhotoEditorListener) {
        this.mOnPhotoEditorListener = onPhotoEditorListener;
    }


    void saveFilter(@NonNull final OnSaveBitmap onSaveBitmap) {
       /* if (mImageFilterView.getVisibility() == VISIBLE) {
            mImageFilterView.saveBitmap(new OnSaveBitmap() {
                @Override
                public void onBitmapReady(final Bitmap saveBitmap) {
                    Log.e(TAG, "saveFilter: " + saveBitmap);
                    mImgSource.setImageBitmap(saveBitmap);
                    mImageFilterView.setVisibility(GONE);
                    onSaveBitmap.onBitmapReady(saveBitmap);
                }

                @Override
                public void onFailure(Exception e) {
                    onSaveBitmap.onFailure(e);
                }
            });
        } else {
            onSaveBitmap.onBitmapReady(mImgSource.getBitmap());
        }*/
        onSaveBitmap.onBitmapReady(mImgSource.getBitmap());


    }

    void setFilterEffect(PhotoFilter filterType) {
     /*   mImageFilterView.setVisibility(VISIBLE);
        mImageFilterView.setSourceBitmap(mImgSource.getBitmap());
        mImageFilterView.setFilterEffect(filterType);*/
    }

    void setFilterEffect(CustomEffect customEffect) {
      /*  mImageFilterView.setVisibility(VISIBLE);
        mImageFilterView.setSourceBitmap(mImgSource.getBitmap());
        mImageFilterView.setFilterEffect(customEffect);*/
    }

    @Override
    public void onEditTextChangeListener(View rootView,String fontPath, String text, int colorCode) {

    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
        if(null!=mOnPhotoEditorListener) {
            mOnPhotoEditorListener.onAddViewListener(viewType, numberOfAddedViews);
        }
    }

    @Override
    public void onRemoveViewListener(int numberOfAddedViews) {
        if(null!=mOnPhotoEditorListener) {
            mOnPhotoEditorListener.onRemoveViewListener( numberOfAddedViews);
        }
    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {
        if(null!=mOnPhotoEditorListener) {
            mOnPhotoEditorListener.onRemoveViewListener(viewType, numberOfAddedViews);
        }
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
        if(null!=mOnPhotoEditorListener) {
            mOnPhotoEditorListener.onStartViewChangeListener(viewType);
        }
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        if(null!=mOnPhotoEditorListener) {
            mOnPhotoEditorListener.onStopViewChangeListener(viewType);
        }
    }
}
