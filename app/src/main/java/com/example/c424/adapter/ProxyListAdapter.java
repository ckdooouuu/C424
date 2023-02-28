package com.example.c424.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.c424.R;
import com.example.c424.bean.ProxyInfo;
import com.example.c424.utils.AssetsUtil;

import java.util.ArrayList;
import java.util.List;

public class ProxyListAdapter extends RecyclerView.Adapter<ProxyListAdapter.ViewHolder> {
    private Context context;
    private List<ProxyInfo> proxyInfoList = new ArrayList<>();
    private int selectedIndex;
    private LayoutInflater inflater;
    private ClickItemListener clickItemListener;

    public ProxyListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setProxyInfoList(List<ProxyInfo> proxyInfoList) {
        this.proxyInfoList = proxyInfoList;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setClickItemListener(ClickItemListener clickItemListener) {
        this.clickItemListener = clickItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.layout_proxy_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProxyInfo proxyInfo = proxyInfoList.get(position);
        Bitmap flag = AssetsUtil.getFlag(context, proxyInfo.getServerCouAbbr().toUpperCase());
        if (flag == null) {
            flag = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_nor);
        }
        Glide.with(context)
                .load(flag)
                .into(new SimpleTarget<Drawable>() {//这样处理，刷新列表的时候图片就不会闪烁
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
                holder.countryFlag.setImageDrawable(resource);
            }
        });
        holder.proxyName.setText(proxyInfo.getWebAlias());
        if (selectedIndex == position) {
            holder.checkBtn.setImageResource(R.mipmap.ic_checked);
        } else {
            holder.checkBtn.setImageResource(R.mipmap.ic_uncheck);
        }
        if (proxyInfo.getDelay() < 200) {
            holder.proxyDelay.setTextColor(Color.parseColor("#12F2C0"));
        } else if (proxyInfo.getDelay() >= 200 && proxyInfo.getDelay() < 400) {
            holder.proxyDelay.setTextColor(Color.parseColor("#F29412"));
        } else {
            holder.proxyDelay.setTextColor(Color.parseColor("#FF5DB6"));
        }
        holder.proxyDelay.setText(String.format(context.getResources().getString(R.string.delay_time), proxyInfo.getDelay()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickItemListener != null) {
                    clickItemListener.clickItem(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (proxyInfoList != null) {
            return proxyInfoList.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView countryFlag;
        TextView proxyName;
        TextView proxyDelay;
        ImageView checkBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            countryFlag = itemView.findViewById(R.id.countryFlag);
            proxyName = itemView.findViewById(R.id.proxyName);
            proxyDelay = itemView.findViewById(R.id.proxyDelay);
            checkBtn = itemView.findViewById(R.id.checkBtn);
        }
    }

    public interface ClickItemListener {
        void clickItem(int position);
    }
}
