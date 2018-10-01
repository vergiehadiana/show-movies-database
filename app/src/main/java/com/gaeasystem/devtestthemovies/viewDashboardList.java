package com.gaeasystem.devtestthemovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class viewDashboardList extends AbstractItem<viewDashboardList, viewDashboardList.ViewHolder> {

    public String image;
    public String title;
    public String urlLink;

    public viewDashboardList() {
    }

    public viewDashboardList setImage(String imageURL) {
        this.image = imageURL;
        return this;
    }

    public viewDashboardList setTitle(String titleName) {
        this.title = titleName;
        return this;
    }

    public viewDashboardList setURL(String url) {
        this.urlLink = url;
        return this;
    }


    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.fastadapter_item_dashboard_list_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.row_list_dashboard;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        Glide.with(holder.view.getContext())
                .load(image)
                .thumbnail(0.2f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageLoad);

        holder.titleName.setText(title);
        holder.uRLtoWebView = urlLink;
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.imageLoad.setImageDrawable(null);
        holder.titleName.setText(null);
        holder.uRLtoWebView = null;
    }

    /**
     * our ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        @BindView(R.id.ll__row_list)
        protected LinearLayout contentContainer;
        @BindView(R.id.iv__row_list__image)
        protected ImageView imageLoad;
        @BindView(R.id.tv__row_list__title)
        protected TextView titleName;

        private String uRLtoWebView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // Set Up Click Listener for Showing WebView on This Article Row
//            contentContainer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    new FinestWebView.Builder(v.getContext())
//                            .titleDefault(titleName.getText().toString())
//                            .webViewBuiltInZoomControls(true)
//                            .webViewDisplayZoomControls(true)
//                            .showSwipeRefreshLayout(true)
//                            .show(uRLtoWebView);
//                }
//            });

            this.view = itemView;
        }
    }
}
