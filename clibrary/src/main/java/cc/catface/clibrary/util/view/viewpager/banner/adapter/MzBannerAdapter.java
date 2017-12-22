package cc.catface.clibrary.util.view.viewpager.banner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import cc.catface.clibrary.R;
import cc.catface.clibrary.util.view.viewpager.banner.RecyclerViewBannerBase;

/**
 * Created by test on 2017/11/22.
 */


public class MzBannerAdapter extends BaseBannerAdapter<MzBannerAdapter.MzViewHolder> {

    private RecyclerViewBannerBase.OnBannerItemClickListener onBannerItemClickListener;

    public MzBannerAdapter(Context context, List<String> urlList, RecyclerViewBannerBase.OnBannerItemClickListener onBannerItemClickListener) {
        super(context, urlList);
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    @Override protected MzBannerAdapter.MzViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        return new MzViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_item, parent, false));
    }

    @Override public void bindCustomViewHolder(MzViewHolder holder, final int position) {
        if (urlList == null || urlList.isEmpty()) return;
        String url = urlList.get(position % urlList.size());
        ImageView iv = holder.iv_img;
        Glide.with(context).load(url).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (onBannerItemClickListener != null)
                    onBannerItemClickListener.onItemClick(position % urlList.size());

            }
        });
    }

    class MzViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_img;

        MzViewHolder(View itemView) {
            super(itemView);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
        }
    }
}
