package com.gaeasystem.devtestthemovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.expandable.ExpandableExtension;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardActivity extends AppCompatActivity {

    @BindView(R.id.btn__act_detail__favorite_movies)
    Button btnFavorite;
    @BindView(R.id.btn__act_detail__search)
    Button btnSearch;
    @BindView(R.id.rv__act_detail__list)
    RecyclerView mRecyclerView;

    FastItemAdapter<viewDashboardList> _ItemFastAdapter;
    EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    apiInterface apiTheMovieDB;
    MaterialDialog dialogLoading;
    Integer pageLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ButterKnife.bind(this);

        // Init Variable for Page Load
        pageLoad = 1;   // First Time

        // INIT Material Dialog for Loading
        dialogLoading = new MaterialDialog.Builder(this)
                .content("Retrieving Data from Server TheMovieDB.org...")
                .theme(Theme.LIGHT)
                .canceledOnTouchOutside(false)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();

        // INIT Button Favorite Click
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Menu Not Working", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(DashboardActivity.this, SearchMovieActivity.class));
            }
        });

        // INIT Button Search Click
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, SearchMovieActivity.class));
            }
        });

        // First Time
        initRecyclerListMoviesDetail(); // Load Init Recycler Voew
        getListMoviesItems(pageLoad);   // Load List Data Movies
    }

    protected void initRecyclerListMoviesDetail() {
        //create our FastAdapter which will manage everything
        _ItemFastAdapter = new FastItemAdapter();

        // linearLayoutManager for one column
//        itemProcessAdapter = items();
//        _ItemNewsFastAdapter.addAdapter(1, itemProcessAdapter);
//        _rvListArticle.setLayoutManager(new LinearLayoutManager(getContext()));

        // we want to have expandable for GridLayout
        ExpandableExtension expandableExtension = new ExpandableExtension();
        _ItemFastAdapter.addExtension(expandableExtension);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(_ItemFastAdapter);

        // SET ENDLESS Scroll Load
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore(final int currentPage) {

                pageLoad++;                     // Add First
                getListMoviesItems(pageLoad);   // Load New List Movies

            }
        };
        mRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);

        //configure our fastAdapter == ON CLICK
        _ItemFastAdapter.withOnClickListener(new OnClickListener<viewDashboardList>() {
            @Override
            public boolean onClick(@Nullable View v, IAdapter<viewDashboardList> adapter, viewDashboardList item, int position) {
                Toast.makeText(DashboardActivity.this, "Name: " + item.title + " | ID: " + item.getIdentifier(), Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(DashboardActivity.this, DetailMovieActivity.class).putExtra("idMovies", item.getIdentifier());
                return false;
            }
        });
    }

    protected void getListMoviesItems(Integer pageToLoad) {
        if (!dialogLoading.isShowing()) {
            dialogLoading.show();
        }

        // Initialize Retrofit First
        apiTheMovieDB = apiSetup.getTheMovieDB_RestAPI().create(apiInterface.class);
        Call<ResponseBody> getMoviesList = apiTheMovieDB.doGetPageListDetail(pageToLoad, getResources().getString(R.string.api_key_themoviesdb));
        getMoviesList.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseReceived = response.body().string();
//                    Log.i("DATA1",responseReceived);
//                    JsonObject convertToObjectJson = new Gson().fromJson(responseReceived, JsonObject.class);
//                    JsonObject objectJsonData = convertToObjectJson.getAsJsonObject("items");

                    JsonObject convertToObjectJson = new Gson().fromJson(responseReceived, JsonObject.class);
                    JsonArray itemsMovies = convertToObjectJson.getAsJsonArray("items");
                    for (int i=0; i<itemsMovies.size(); i++) {
                        JsonObject moviesData = itemsMovies.get(i).getAsJsonObject();
                        Log.i("DATA2",moviesData.get("title").getAsString());
                        _ItemFastAdapter.add(new viewDashboardList()
                                .withIdentifier(moviesData.get("id").getAsInt())
                                .setURL("https://image.tmdb.org/t/p"+moviesData.get("poster_path").getAsString())
                                .setImage("https://image.tmdb.org/t/p/w185"+moviesData.get("poster_path").getAsString())
                                .setTitle(moviesData.get("original_title").getAsString())
                        );
                    }

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Toast.makeText(DashboardActivity.this, "Success Get Data from REST TheMoviesDB",
                                            Toast.LENGTH_SHORT).show();

                                    dialogLoading.dismiss();
                                }
                            }, 1000);

                } catch (IOException e) {
                    Log.e("getListFromREST","FAILED BODY.STRING()");
                    dialogLoading.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) { }
        });
    }

}
