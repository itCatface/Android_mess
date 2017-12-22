package cc.catface.clibrary.util.view.viewpager.banner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by test on 2017/11/22.
 */


public abstract class BaseBannerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<String> urlList;
    protected Context context;

    BaseBannerAdapter(Context context, List<String> urlList) {
        this.urlList = urlList;
        this.context = context;
    }

    @Override public int getItemCount() {
        return urlList.size() < 2 ? 1 : Integer.MAX_VALUE;
    }

    @Override public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return createCustomViewHolder(parent, viewType);
    }

    @Override public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindCustomViewHolder((VH) holder, position);
    }


    protected abstract VH createCustomViewHolder(ViewGroup parent, int viewType);

    public abstract void bindCustomViewHolder(VH holder, int position);
}
