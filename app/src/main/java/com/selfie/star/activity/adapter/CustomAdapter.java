package com.selfie.star.activity.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.base.commonframework.baselibrary.recyclerview.AbstractRecyclerAdapter;
import com.base.commonframework.utility.CommonFrameworkUtil;
import com.selfie.star.R;
import com.selfie.star.activity.model.Category;
import com.selfie.star.databinding.ListItemAdapterBinding;

import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends AbstractRecyclerAdapter<CustomAdapter.ViewHolder, Category> {
    private Context mContext;

    public CustomAdapter(Context context, List<Category> list) {
        super(list);
        this.mContext = context;
    }


    @Override
    public ViewHolder setViewHolder(ViewGroup viewGroup, int viewType) {
        ListItemAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.list_item_adapter, viewGroup, false);
        return new ViewHolder(binding, getOnItemClickListener());
    }

    @Override
    public void onBindData(ViewHolder viewHolder, Category val) {
        viewHolder.bindView(mContext, val);

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemAdapterBinding binding;
        private Category category;

        public ViewHolder(ListItemAdapterBinding viewDataBinding, final OnItemViewClickListener holderClick) {
            super(viewDataBinding.getRoot());
            binding = viewDataBinding;
            itemView.setOnClickListener(v -> {
                if (null == holderClick) {
                    CommonFrameworkUtil.showLog("Holder", "Trying to work on a null object ,returning.");
                    return;
                }
                holderClick.onItemViewClick(v, category);
            });

        }

        public void bindView(Context mContext, Category category) {
            this.category = category;
            if (category == null) {
                CommonFrameworkUtil.showLog("Holder", "Trying to work on a null object ,returning.");
                return;
            }
            binding.textView.setText(CommonFrameworkUtil.getCamelCapsName(category.getName()));
        }

    }


}
