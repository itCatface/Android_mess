package cc.catface.kotlin.module.weather.indexbarsupport;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cc.catface.kotlin.R;


/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private List<CityBean> mDatas;

    public CityAdapter setDatas(List<CityBean> datas) {
        mDatas = datas;
        return this;
    }

    @Override public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    @Override public CityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false));
    }

    @Override public void onBindViewHolder(final CityAdapter.ViewHolder holder, final int position) {
        final CityBean cityBean = mDatas.get(position);

        holder.iv_city.setImageResource(R.drawable.ic_launcher_background);
        holder.tv_city.setText(cityBean.getCity());



        holder.ll_city.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View view) {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onClick(mDatas.get(position).getCity().trim());
                }
            }
        });
//        holder.ll_city.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                Toast.makeText(v.getContext(), "pos:" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_city;
        ImageView iv_city;
        TextView tv_city;

        public ViewHolder(View view) {
            super(view);
            ll_city = view.findViewById(R.id.ll_city);
            iv_city = (ImageView) view.findViewById(R.id.iv_city);
            tv_city = (TextView) view.findViewById(R.id.tv_city);
        }
    }


    /**************************************************** 点击事件 *************************************************/
    public interface OnItemClickListener {
        void onClick(String result);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
