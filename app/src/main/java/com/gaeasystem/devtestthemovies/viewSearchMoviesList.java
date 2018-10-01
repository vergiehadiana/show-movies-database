package com.gaeasystem.devtestthemovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class viewSearchMoviesList extends AbstractItem<viewSearchMoviesList, viewSearchMoviesList.ViewHolder> {

    public String posterMovies;
    public String titleMovies;
    public String releaseDateMovies;
    public String overviewMovies;
    public Float voteRatingMovies;

    public viewSearchMoviesList() {
    }

    public viewSearchMoviesList setPosterImage(String imageMovies) {
        this.posterMovies = imageMovies;
        return this;
    }

    public viewSearchMoviesList setTitle(String titleMovies) {
        this.titleMovies = titleMovies;
        return this;
    }

    public viewSearchMoviesList setReleaseDate(String releaseMovies) {
        this.releaseDateMovies = releaseMovies;
        return this;
    }

    public viewSearchMoviesList setOverview(String overviewMovies) {
        this.overviewMovies = overviewMovies;
        return this;
    }

    public viewSearchMoviesList setRating(Float voteRating) {
        this.voteRatingMovies = voteRating;
        return this;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.fastadapter_item_search_movies_list_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.row_list_search_movies;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        if (posterMovies == null) {
            // For Not Found Image
            Glide.with(holder.view.getContext())
                    .load(R.drawable.no_image_found)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.imageMov);
        } else {
            Glide.with(holder.view.getContext())
                    .load(posterMovies)
                    .thumbnail(0.2f)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.imageMov);
        }

        holder.ratingMov.setMax(10);
        holder.ratingMov.setRating(voteRatingMovies.floatValue());

        holder.titleMov.setText(titleMovies);
        holder.releaseDateMov.setText(releaseDateMovies);
        holder.overviewMov.setText(overviewMovies);

    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.imageMov.setImageDrawable(null);
        holder.ratingMov.setRating(0.0f);
        holder.titleMov.setText(null);
        holder.releaseDateMov.setText(null);
        holder.overviewMov.setText(null);
    }

    /**
     * our ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        @BindView(R.id.iv__row_list_search_movies__image)
        protected ImageView imageMov;
        @BindView(R.id.tv__row_list_search_movies__title)
        protected TextView titleMov;
        @BindView(R.id.tv__row_list_search_movies__release_date)
        protected TextView releaseDateMov;
        @BindView(R.id.mrb__row_list_search_movies__rating)
        protected RatingBar ratingMov;
        @BindView(R.id.tv__row_list_search_movies__overview)
        protected TextView overviewMov;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.view = itemView;
        }
    }

}
