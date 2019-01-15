package com.selfie.star.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.base.commonframework.baselibrary.imageloader.ImageLoader;
import com.base.commonframework.baselibrary.recyclerview.AbstractRecyclerAdapter;
import com.base.commonframework.utility.CommonFrameworkUtil;
import com.selfie.star.R;
import com.selfie.star.databinding.ListItemShowImageBinding;

import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ShowImageAdapter extends AbstractRecyclerAdapter<ShowImageAdapter.ViewHolder, String> {
    private Context mContext;

    public ShowImageAdapter(Context context, List<String> list) {
        super(list);
        this.mContext = context;
    }


    @Override
    public ViewHolder setViewHolder(ViewGroup viewGroup, int viewType) {
        ListItemShowImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.list_item_show_image, viewGroup, false);
        return new ViewHolder(binding, getOnItemClickListener());
    }

    @Override
    public void onBindData(ViewHolder viewHolder, String val) {
        viewHolder.bindView(mContext, val);

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemShowImageBinding binding;
        private String imagePath;

        public ViewHolder(ListItemShowImageBinding viewDataBinding, final OnItemViewClickListener holderClick) {
            super(viewDataBinding.getRoot());
            binding = viewDataBinding;
            itemView.setOnClickListener(v -> {
                if (null == holderClick) {
                    CommonFrameworkUtil.showLog("Holder", "Trying to work on a null object ,returning.");
                    return;
                }
                holderClick.onItemViewClick(v, imagePath);
            });

        }

        public void bindView(Context mContext, String imagePath) {
            this.imagePath = imagePath;
            if (imagePath == null) {
                CommonFrameworkUtil.showLog("Holder", "Trying to work on a null object ,returning.");
                return;
            }
            ImageLoader.loadImage(mContext,imagePath, binding.imageView,R.drawable.no_image_found);
        }

    }


}
