package com.selfie.star.imageditor.background;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.selfie.star.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author <a href="https://github.com/burhanrashid52">Burhanuddin Rashid</a>
 * @version 0.1.2
 * @since 5/23/2018
 */
public class BackgroundViewAdapter extends RecyclerView.Adapter<BackgroundViewAdapter.ViewHolder> {

    private BackgroundListener mFilterListener;
    private List<String> mPairList = new ArrayList<>();

    public BackgroundViewAdapter(BackgroundListener filterListener) {
        mFilterListener = filterListener;
        setupFilters();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_filter_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String filterPair = mPairList.get(position);
        Bitmap fromAsset = getBitmapFromAsset(holder.itemView.getContext(), filterPair);
        holder.mImageFilterView.setImageBitmap(fromAsset);
        holder.mTxtFilterName.setText("");
    }

    @Override
    public int getItemCount() {
        return mPairList.size();
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

    private void setupFilters() {
        mPairList.add("pattern/h0.png");
        mPairList.add("pattern/pat1.png");
        mPairList.add("pattern/pat2.png");
        mPairList.add("pattern/pat3.png");
        mPairList.add("pattern/pat4.png");
        mPairList.add("pattern/pat5.png");
        mPairList.add("pattern/pat6.png");
        mPairList.add("pattern/pat7.png");
        mPairList.add("pattern/pat8.png");
        mPairList.add("pattern/pat9.png");
        mPairList.add("pattern/pat10.png");
        mPairList.add("pattern/pattern_1.png");
        mPairList.add("pattern/pattern_2.png");
        mPairList.add("pattern/pattern_3.png");
        mPairList.add("pattern/pattern_4.png");
        mPairList.add("pattern/pattern_5.png");
        mPairList.add("pattern/pattern_6.png");
        mPairList.add("pattern/pattern_8.png");
        mPairList.add("pattern/pattern_9.png");
        mPairList.add("pattern/pattern_10.png");
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageFilterView;
        TextView mTxtFilterName;

        ViewHolder(View itemView) {
            super(itemView);
            mImageFilterView = itemView.findViewById(R.id.imgFilterView);
            mTxtFilterName = itemView.findViewById(R.id.txtFilterName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFilterListener.onBackgroundSelected(mPairList.get(getLayoutPosition()));
                }
            });
        }
    }
}
