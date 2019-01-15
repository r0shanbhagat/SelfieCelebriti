package com.selfie.star.imageditor.filters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;
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
import ja.burhanrashid52.photoeditor.PhotoFilter;

/**
 * @author <a href="https://github.com/burhanrashid52">Burhanuddin Rashid</a>
 * @version 0.1.2
 * @since 5/23/2018
 */
public class FilterViewAdapter extends RecyclerView.Adapter<FilterViewAdapter.ViewHolder> {

    private FilterListener mFilterListener;
    private List<Pair<String, PhotoFilter>> mPairList = new ArrayList<>();

    public FilterViewAdapter(FilterListener filterListener) {
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
        Pair<String, PhotoFilter> filterPair = mPairList.get(position);
        Bitmap fromAsset = getBitmapFromAsset(holder.itemView.getContext(), filterPair.first);
        holder.mImageFilterView.setImageBitmap(fromAsset);
        holder.mTxtFilterName.setText(filterPair.second.name().replace("_", " "));
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
        mPairList.add(new Pair<>("filters/none.png", PhotoFilter.NONE));
        mPairList.add(new Pair<>("filters/avg_blur.png", PhotoFilter.AVERAGE_BLUR));
        mPairList.add(new Pair<>("filters/block.png", PhotoFilter.BLOCK));
        mPairList.add(new Pair<>("filters/gaussain_blur.png", PhotoFilter.GAUSSIAN_BLUR));
        mPairList.add(new Pair<>("filters/gotham.png", PhotoFilter.GOTHAM));
        mPairList.add(new Pair<>("filters/gray.png", PhotoFilter.GRAY));
        mPairList.add(new Pair<>("filters/hdr.png", PhotoFilter.HDR));
        mPairList.add(new Pair<>("filters/invert_color.png", PhotoFilter.INVERT));
        mPairList.add(new Pair<>("filters/light.png", PhotoFilter.LIGHT));
        mPairList.add(new Pair<>("filters/lomo.png", PhotoFilter.LOMO));
        mPairList.add(new Pair<>("filters/motion_blur.png", PhotoFilter.MOTION_BLUR));
        mPairList.add(new Pair<>("filters/neon.png", PhotoFilter.NEON));
        mPairList.add(new Pair<>("filters/oil.png", PhotoFilter.OIL));
        mPairList.add(new Pair<>("filters/old.png", PhotoFilter.OLD));
        mPairList.add(new Pair<>("filters/pixelate.png", PhotoFilter.PIXELATE));
        mPairList.add(new Pair<>("filters/sharpen.png", PhotoFilter.SHARPEN));
        mPairList.add(new Pair<>("filters/relief.png", PhotoFilter.RELIEF));
        mPairList.add(new Pair<>("filters/sketch.png", PhotoFilter.SKETCH));
        mPairList.add(new Pair<>("filters/soft_glow.png", PhotoFilter.SOFT_GLOW));
        mPairList.add(new Pair<>("filters/tv.png", PhotoFilter.TV));
       /* mPairList.add(new Pair<>("filters/b_n_w", PhotoFilter.BLACK_WHITE));
        mPairList.add(new Pair<>("filters/flip_horizental", PhotoFilter.FLIP_HORIZONTAL));
        mPairList.add(new Pair<>("filters/flip_vertical", PhotoFilter.FLIP_VERTICAL));
        mPairList.add(new Pair<>("filters/rotate", PhotoFilter.ROTATE));*/
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
                    mFilterListener.onFilterSelected(mPairList.get(getLayoutPosition()).second);
                }
            });
        }
    }
}
