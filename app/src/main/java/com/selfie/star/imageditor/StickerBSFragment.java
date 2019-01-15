package com.selfie.star.imageditor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.selfie.star.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StickerBSFragment extends BottomSheetDialogFragment {


    private StickerListener mStickerListener;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    public StickerBSFragment() {
        // Required empty public constructor
    }

    public void setStickerListener(StickerListener stickerListener) {
        mStickerListener = stickerListener;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sticker_emoji_dialog, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        RecyclerView rvEmoji = contentView.findViewById(R.id.rvEmoji);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rvEmoji.setLayoutManager(gridLayoutManager);
        StickerAdapter stickerAdapter = new StickerAdapter(setupFilters());
        rvEmoji.setAdapter(stickerAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private List<String> setupFilters() {
        List<String> mPairList = new ArrayList<>();
        mPairList.add("sticker/animal/animal1.png");
        mPairList.add("sticker/animal/animal2.png");
        mPairList.add("sticker/animal/animal3.png");
        mPairList.add("sticker/animal/animal4.png");
        mPairList.add("sticker/animal/animal5.png");
        mPairList.add("sticker/animal/animal6.png");
        mPairList.add("sticker/animal/animal7.png");
        mPairList.add("sticker/animal/animal8.png");
        mPairList.add("sticker/animal/animal9.png");
        mPairList.add("sticker/animal/animal10.png");
        mPairList.add("sticker/animal/animal11.png");
        mPairList.add("sticker/animal/animal12.png");
        mPairList.add("sticker/animal/animal13.png");
        mPairList.add("sticker/animal/animal14.png");
        mPairList.add("sticker/animal/animal15.png");
        mPairList.add("sticker/animal/animal16.png");
        mPairList.add("sticker/animal/animal17.png");
        mPairList.add("sticker/animal/animal18.png");
        mPairList.add("sticker/animal/animal19.png");
        mPairList.add("sticker/animal/animal20.png");
        mPairList.add("sticker/animal/animal21.png");
        mPairList.add("sticker/animal/animal22.png");
        mPairList.add("sticker/animal/animal23.png");
        mPairList.add("sticker/animal/animal24.png");
        mPairList.add("sticker/animal/animal25.png");
        mPairList.add("sticker/animal/animal26.png");
        mPairList.add("sticker/animal/animal27.png");
        mPairList.add("sticker/animal/animal28.png");
        mPairList.add("sticker/animal/animal29.png");

        mPairList.add("sticker/cartoon/cartoon1.png");
        mPairList.add("sticker/cartoon/cartoon2.png");
        mPairList.add("sticker/cartoon/cartoon3.png");
        mPairList.add("sticker/cartoon/cartoon4.png");
        mPairList.add("sticker/cartoon/cartoon5.png");
        mPairList.add("sticker/cartoon/cartoon6.png");
        mPairList.add("sticker/cartoon/cartoon7.png");
        mPairList.add("sticker/cartoon/cartoon8.png");
        mPairList.add("sticker/cartoon/cartoon9.png");
        mPairList.add("sticker/cartoon/cartoon10.png");
        mPairList.add("sticker/cartoon/cartoon11.png");
        mPairList.add("sticker/cartoon/cartoon12.png");
        mPairList.add("sticker/cartoon/cartoon13.png");
        mPairList.add("sticker/cartoon/cartoon14.png");
        mPairList.add("sticker/cartoon/cartoon15.png");
        mPairList.add("sticker/cartoon/cartoon16.png");
        mPairList.add("sticker/cartoon/cartoon17.png");
        mPairList.add("sticker/cartoon/cartoon18.png");
        mPairList.add("sticker/cartoon/cartoon19.png");
        mPairList.add("sticker/cartoon/cartoon20.png");
        mPairList.add("sticker/cartoon/cartoon21.png");
        mPairList.add("sticker/cartoon/cartoon22.png");
        mPairList.add("sticker/cartoon/cartoon23.png");
        mPairList.add("sticker/cartoon/cartoon24.png");

        mPairList.add("sticker/love/love1.png");
        mPairList.add("sticker/love/love2.png");
        mPairList.add("sticker/love/love3.png");
        mPairList.add("sticker/love/love4.png");
        mPairList.add("sticker/love/love5.png");
        mPairList.add("sticker/love/love6.png");
        mPairList.add("sticker/love/love7.png");
        mPairList.add("sticker/love/love8.png");
        mPairList.add("sticker/love/love9.png");
        mPairList.add("sticker/love/love10.png");
        mPairList.add("sticker/love/love11.png");
        mPairList.add("sticker/love/love12.png");
        mPairList.add("sticker/love/love13.png");
        mPairList.add("sticker/love/love14.png");
        mPairList.add("sticker/love/love15.png");
        mPairList.add("sticker/love/love16.png");
        mPairList.add("sticker/love/love17.png");
        mPairList.add("sticker/love/love18.png");
        mPairList.add("sticker/love/love19.png");
        mPairList.add("sticker/love/love20.png");
        mPairList.add("sticker/love/love21.png");
        mPairList.add("sticker/love/love22.png");


        mPairList.add("sticker/stylish/stylish1.png");
        mPairList.add("sticker/stylish/stylish2.png");
        mPairList.add("sticker/stylish/stylish3.png");
        mPairList.add("sticker/stylish/stylish4.png");
        mPairList.add("sticker/stylish/stylish5.png");
        mPairList.add("sticker/stylish/stylish6.png");
        mPairList.add("sticker/stylish/stylish7.png");
        mPairList.add("sticker/stylish/stylish8.png");
        mPairList.add("sticker/stylish/stylish9.png");
        mPairList.add("sticker/stylish/stylish10.png");
        mPairList.add("sticker/stylish/stylish11.png");
        mPairList.add("sticker/stylish/stylish12.png");
        mPairList.add("sticker/stylish/stylish13.png");
        mPairList.add("sticker/stylish/stylish14.png");
        mPairList.add("sticker/stylish/stylish15.png");
        mPairList.add("sticker/stylish/stylish16.png");
        mPairList.add("sticker/stylish/stylish17.png");
        mPairList.add("sticker/stylish/stylish18.png");
        mPairList.add("sticker/stylish/stylish19.png");
        mPairList.add("sticker/stylish/stylish20.png");
        mPairList.add("sticker/stylish/stylish21.png");
        mPairList.add("sticker/stylish/stylish22.png");
        mPairList.add("sticker/stylish/stylish23.png");
        mPairList.add("sticker/stylish/stylish24.png");

        mPairList.add("sticker/text/text1.png");
        mPairList.add("sticker/text/text2.png");
        mPairList.add("sticker/text/text3.png");
        mPairList.add("sticker/text/text4.png");
        mPairList.add("sticker/text/text5.png");
        mPairList.add("sticker/text/text6.png");
        mPairList.add("sticker/text/text7.png");
        mPairList.add("sticker/text/text8.png");
        mPairList.add("sticker/text/text9.png");
        mPairList.add("sticker/text/text10.png");
        mPairList.add("sticker/text/text11.png");
        mPairList.add("sticker/text/text12.png");
        mPairList.add("sticker/text/text13.png");
        mPairList.add("sticker/text/text14.png");
        mPairList.add("sticker/text/text15.png");
        mPairList.add("sticker/text/text16.png");
        mPairList.add("sticker/text/text16.png");
        mPairList.add("sticker/text/text17.png");
        mPairList.add("sticker/text/text18.png");
        mPairList.add("sticker/text/text19.png");
        mPairList.add("sticker/text/text20.png");
        mPairList.add("sticker/text/text21.png");
        mPairList.add("sticker/text/text22.png");
        mPairList.add("sticker/text/text23.png");
        mPairList.add("sticker/text/text24.png");
        mPairList.add("sticker/text/text25.png");
        mPairList.add("sticker/text/text26.png");
        mPairList.add("sticker/text/text27.png");
        mPairList.add("sticker/text/text28.png");
        mPairList.add("sticker/text/text29.png");
        mPairList.add("sticker/text/text30.png");
        mPairList.add("sticker/text/text31.png");
        mPairList.add("sticker/text/text32.png");
        mPairList.add("sticker/text/text33.png");
        mPairList.add("sticker/text/text34.png");
        mPairList.add("sticker/text/text35.png");
        mPairList.add("sticker/text/text36.png");
        mPairList.add("sticker/text/text37.png");
        mPairList.add("sticker/text/text38.png");
        mPairList.add("sticker/text/text40.png");
        mPairList.add("sticker/text/text41.png");
        mPairList.add("sticker/text/text42.png");
        mPairList.add("sticker/text/text43.png");
        mPairList.add("sticker/text/text44.png");
        mPairList.add("sticker/text/text45.png");
        return mPairList;
    }

    private Bitmap getBitmapFromAsset(Context context, String strName) {
        AssetManager assetManager = context.getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
            return BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface StickerListener {
        void onStickerClick(Bitmap bitmap);
    }

    public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {
        private List<String> stickerList;

        public StickerAdapter(List<String> strings) {
            this.stickerList = strings;
        }

        // int[] stickerList = new int[]{R.drawable.animal1, R.drawable.bb};

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sticker, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.imgSticker.setImageBitmap(getBitmapFromAsset(getContext(), stickerList.get(position)));
        }

        @Override
        public int getItemCount() {
            return stickerList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgSticker;

            ViewHolder(View itemView) {
                super(itemView);
                imgSticker = itemView.findViewById(R.id.imgSticker);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mStickerListener != null) {
                            mStickerListener.onStickerClick(getBitmapFromAsset(getContext(), stickerList.get(getLayoutPosition())));
                        }
                        dismiss();
                    }
                });
            }
        }
    }
}