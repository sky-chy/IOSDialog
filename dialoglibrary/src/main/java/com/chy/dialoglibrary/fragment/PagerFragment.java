package com.chy.dialoglibrary.fragment;

import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chy.dialoglibrary.R;
import com.chy.dialoglibrary.bean.ContentBean;
import com.chy.dialoglibrary.databinding.ItemGridBinding;
import com.chy.dialoglibrary.databinding.PageRecyclerviewBinding;
import com.chy.dialoglibrary.listener.CHYOnGridClickListener;

import java.util.ArrayList;

public class PagerFragment extends Fragment {
    private PageRecyclerviewBinding mBinding;
    private ItemGridBinding itemGridBinding;
    private static CHYOnGridClickListener chyOnGridClickListener;
    private int color;
    private float size;
    private RecyclerView.Adapter adapter;

    public static PagerFragment getInstance(Bundle bundle, CHYOnGridClickListener listener) {
        chyOnGridClickListener = listener;
        PagerFragment fragment = new PagerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.page_recyclerview, container, false);
        init();
        return mBinding.getRoot();
    }

    private void init() {
        Bundle bundle = this.getArguments();
        ArrayList<ContentBean> content = (ArrayList<ContentBean>) bundle.getSerializable("content");
        //初始化适配器
        initAdapter(content);
        setTextSize(bundle.getFloat("size", 0f));
        setTextColor(bundle.getInt("color", 0));
    }

    /**
     * 初始化适配器
     *
     * @param content 数据源
     */
    private void initAdapter(final ArrayList<ContentBean> content) {
        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                itemGridBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.item_grid, parent, false);
                return new RecyclerView.ViewHolder(itemGridBinding.getRoot()) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
                ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
                lp.height = (int) ((getHeight() - getResources().getDimension(R.dimen.dimen_60_0dp)) / 2);
                holder.itemView.setLayoutParams(lp);
                itemGridBinding.imgIcon.setBackgroundResource(content.get(position).icon);
                itemGridBinding.tvTitle.setText(content.get(position).title);
                if (color != 0)
                    itemGridBinding.tvTitle.setTextColor(color);
                if (size != 0f)
                    itemGridBinding.tvTitle.setTextSize(size);
                itemGridBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chyOnGridClickListener != null) {
                            chyOnGridClickListener.onGridClick(v, ((TextView) ((LinearLayout) v).getChildAt(1)).getText().toString());
                        }
                    }
                });
            }

            @Override
            public int getItemCount() {
                return content.size();
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }
        };
        mBinding.rvGrid.setAdapter(adapter);
    }

    private int getHeight() {
        int dialogHeight;
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int height = dm.heightPixels;
        if (height * 0.45 < (1920 * 0.3)) {
            dialogHeight = (int) (1920 * 0.3);
        } else
            dialogHeight = (int) (height * 0.45);
        return dialogHeight;
    }

    /**
     * 设置颜色
     */
    private void setTextColor(@ColorInt int color) {
        this.color = color;
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置字体大小
     */
    private void setTextSize(float size) {
        this.size = size;
        adapter.notifyDataSetChanged();
    }

}
