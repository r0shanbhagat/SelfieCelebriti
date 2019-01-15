package com.selfie.star.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.base.commonframework.baselibrary.imageloader.ImageLoader;
import com.base.commonframework.baselibrary.recyclerview.AbstractRecyclerAdapter;
import com.base.commonframework.utility.CommonFrameworkUtil;
import com.selfie.star.R;
import com.selfie.star.activity.model.ImagesDto;
import com.selfie.star.databinding.ListItemDetailAdapterBinding;

import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CustomDetailAdapter extends AbstractRecyclerAdapter<CustomDetailAdapter.ViewHolder, ImagesDto> {
    private Context mContext;

    public CustomDetailAdapter(Context context, List<ImagesDto> list) {
        super(list);
        this.mContext = context;
    }


    @Override
    public ViewHolder setViewHolder(ViewGroup viewGroup, int viewType) {
        ListItemDetailAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.list_item_detail_adapter, viewGroup, false);
        return new ViewHolder(binding, getOnItemClickListener());
    }

    @Override
    public void onBindData(ViewHolder viewHolder, ImagesDto val) {
        viewHolder.bindView(mContext, val);

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemDetailAdapterBinding binding;
        private ImagesDto imagesDto;

        public ViewHolder(ListItemDetailAdapterBinding viewDataBinding, final OnItemViewClickListener holderClick) {
            super(viewDataBinding.getRoot());
            binding = viewDataBinding;
            itemView.setOnClickListener(v -> {
                if (null == holderClick) {
                    CommonFrameworkUtil.showLog("Holder", "Trying to work on a null object ,returning.");
                    return;
                }
                holderClick.onItemViewClick(v, imagesDto);
            });

        }

        public void bindView(Context mContext, ImagesDto imagesDto) {
            this.imagesDto = imagesDto;
            if (imagesDto == null) {
                CommonFrameworkUtil.showLog("Holder", "Trying to work on a null object ,returning.");
                return;
            }
            ImageLoader.loadImage(mContext,imagesDto.getImgUrl(), binding.imageView,R.drawable.no_image_found);
        }

    }


}
