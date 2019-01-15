package com.base.commonframework.baselibrary.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.base.commonframework.R;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by ankit on 28/3/17.
 *
 * @param <T> the type parameter
 */
public abstract class AbstractRecyclerAdapter<T extends RecyclerView.ViewHolder, V> extends RecyclerView.Adapter<T> {

    private final static int VIEW_TYPE_ITEM = 0;
    private final static int VIEW_TYPE_LOADING = 404;
    public List<V> itemArrayList;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private OnItemViewClickListener<V> onItemClickListener;


    /**
     * Instantiates a new Abstract recycler adapter.
     *
     * @param itemArrayList the item array list
     */
    public AbstractRecyclerAdapter(List<V> itemArrayList) {
        this.itemArrayList = itemArrayList;

    }

    @Override
    public T onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_loading, viewGroup, false);
            return (T) new LoadingViewHolder(view);
        } else {
            return setViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        if (holder instanceof AbstractRecyclerAdapter.LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        } else {
            onBindData(holder, (V) itemArrayList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return null != itemArrayList && !itemArrayList.isEmpty() ? itemArrayList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return itemArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    /**
     * Sets loaded.
     */
    public void setLoaded() {
        isLoading = false;
    }

    public OnItemViewClickListener<V> getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemViewClickListener<V> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Add items.
     *
     * @param itemAdd the saved card itemz
     */
    public void addItems(List<V> itemAdd) {
        itemArrayList = itemAdd;
        this.notifyDataSetChanged();
    }

    /**
     * Sets on load more listener.
     *
     * @param recyclerView        the recycler view
     * @param mOnLoadMoreListener the m on load more listener
     */
    public void setOnLoadMoreListener(RecyclerView recyclerView, OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    // isLoading = true;
                }

            }
        });
    }

    public void setOnLoadMorePageListener(RecyclerView recyclerView, final OnLoadMoreListener mOnLoadMoreListener) {
       /* this.onLoadMoreListener = mOnLoadMoreListener;
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (!isLoading) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                        onLoadMoreListener.onLoadMore(currentPage);
                    }
                    isLoading = true;
                }
            }
        });*/
    }

    /**
     * Gets item.
     *
     * @param position the position
     * @return the item
     */
    public Object getItem(int position) {
        return itemArrayList.get(position);
    }

    /**
     * Gets Adapter ItemList.
     *
     * @return the item
     */
    public List<V> getItemList() {
        return itemArrayList;
    }


    /**
     * Sets view holder.
     *
     * @param parent   the parent
     * @param viewType
     * @return the view holder
     */
    public abstract T setViewHolder(ViewGroup parent, int viewType);

    /**
     * On bind data.
     *
     * @param viewHolder the t
     * @param itemVal    the val
     */
    public abstract void onBindData(T viewHolder, V itemVal);

    public interface OnItemViewClickListener<V> {
        void onItemViewClick(View view, V viewModel);
    }

    /**
     * The interface On load more listener.
     */
    public static abstract class OnLoadMoreListener {
        /**
         * On load more.
         */
        protected void onLoadMore() {

        }

        protected void onLoadMore(int currentPage) {

        }


    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Progress bar.
         */
        public ProgressBar progressBar;

        /**
         * Instantiates a new Loading view holder.
         *
         * @param view the view
         */
        LoadingViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressBar);
        }

    }
}
