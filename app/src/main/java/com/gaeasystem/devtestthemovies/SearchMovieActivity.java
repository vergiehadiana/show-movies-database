package com.gaeasystem.devtestthemovies;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
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

import static com.mikepenz.fastadapter.adapters.ItemAdapter.items;

public class SearchMovieActivity extends AppCompatActivity {

    @BindView(R.id.tb__act_search_movie__toolbar)
    Toolbar _tbSearchActivity;
    @BindView(R.id.et__act_search_movie__query)
    EditText etSearchQuery;
    @BindView(R.id.btn__act_search_movie__search)
    Button btnSearchMovie;
    @BindView(R.id.rv__act_search_movie__list)
    RecyclerView rvSearchMovieList;

    ItemAdapter _itemFooterAdapter;
    FastItemAdapter<viewSearchMoviesList> _ItemSearchFastAdapter;
    EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    apiInterface apiTheMovieDB;
    MaterialDialog dialogLoading;
    Integer pageBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        ButterKnife.bind(this);

        // Handle Toolbar
        setSupportActionBar(_tbSearchActivity);
        //set disable title name
        getSupportActionBar().setTitle("Search Movies");
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //set the back arrow in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        // INIT Material Dialog for Loading
        dialogLoading = new MaterialDialog.Builder(this)
                .content("Waiting Retrieving Data from Server TheMovieDB.org...")
                .theme(Theme.LIGHT)
                .canceledOnTouchOutside(false)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();

        // INIT Button Search Click
        btnSearchMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SearchMovieActivity.this, "Searching Movies " + etSearchQuery.getText().toString(), Toast.LENGTH_SHORT).show();
                // Clearing List Data On RecycleView
                _itemFooterAdapter.clear();
                _ItemSearchFastAdapter.clear();
                // Init Variable for Page Load
                pageBuffer = 1;   // First Time
                // Execute Get Search
                getSearchMoviesItems();
            }
        });

        // First Time Execute
        initListSearchMovie();
        dialogLoading.dismiss();

    }

    protected void initListSearchMovie() {
        _ItemSearchFastAdapter = new FastItemAdapter();

        // linearLayoutManager for one column
        _itemFooterAdapter = items();
        _ItemSearchFastAdapter.addAdapter(1, _itemFooterAdapter);
        rvSearchMovieList.setLayoutManager(new LinearLayoutManager(this));

        // we want to have expandable for GridLayout
//        ExpandableExtension expandableExtension = new ExpandableExtension();
//        _ItemSearchFastAdapter.addExtension(expandableExtension);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
//        rvSearchMovieList.setLayoutManager(gridLayoutManager);

        rvSearchMovieList.setItemAnimator(new DefaultItemAnimator());
        rvSearchMovieList.setAdapter(_ItemSearchFastAdapter);

        // SET ENDLESS Scroll Load on Search
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore(final int currentPage) {
                pageBuffer++;             // Add First Variable
                getSearchMoviesItems();   // Load New List Movies Search
            }
        };
        rvSearchMovieList.addOnScrollListener(endlessRecyclerOnScrollListener);

        //configure our fastAdapter == ON CLICK
        _ItemSearchFastAdapter.withOnClickListener(new OnClickListener<viewSearchMoviesList>() {
            @Override
            public boolean onClick(@Nullable View v, IAdapter<viewSearchMoviesList> adapter, viewSearchMoviesList item, int position) {
                Toast.makeText(SearchMovieActivity.this, "Name: " + item.titleMovies + " | Rating: " + item.voteRatingMovies.toString() + " | ID: " + item.getIdentifier(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    protected void getSearchMoviesItems() {
        dialogLoading.show();

        // Initialize Retrofit First
        apiTheMovieDB = apiSetup.getTheMovieDB_RestAPI().create(apiInterface.class);
        Call<ResponseBody> getMoviesList = apiTheMovieDB.doSearchMovies(
                getResources().getString(R.string.api_key_themoviesdb),
                etSearchQuery.getText().toString(),
                pageBuffer
        );
        getMoviesList.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseReceived = response.body().string();
//                    JsonObject convertToObjectJson = new Gson().fromJson(responseReceived, JsonObject.class);
//                    JsonObject objectJsonData = convertToObjectJson.getAsJsonObject("items");

                    JsonObject convertToObjectJson = new Gson().fromJson(responseReceived, JsonObject.class);
                    JsonArray itemsMovies = convertToObjectJson.getAsJsonArray("results");
                    Log.i("DATA1", "Get Total Result : " + convertToObjectJson.get("total_results").getAsNumber());
                    Log.i("DATA1", "Get Total Pages : " + convertToObjectJson.get("total_pages").getAsNumber());
                    for (int i=0; i<itemsMovies.size(); i++) {
                        JsonObject moviesData = itemsMovies.get(i).getAsJsonObject();
                        Log.i("DATA2",moviesData.get("title").getAsString());
                        _ItemSearchFastAdapter.add(new viewSearchMoviesList()
                                .withIdentifier(moviesData.get("id").getAsInt())
                                .setPosterImage(moviesData.get("poster_path").isJsonNull() ? null : "https://image.tmdb.org/t/p/w185"+moviesData.get("poster_path").getAsString())
                                .setRating(moviesData.get("vote_average").getAsFloat())
                                .setTitle(moviesData.get("title").getAsString())
                                .setReleaseDate(moviesData.get("release_date").getAsString())
                                .setOverview(moviesData.get("overview").getAsString())
                        );
                    }

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Toast.makeText(SearchMovieActivity.this, "Success Get Data from REST TheMoviesDB",
                                            Toast.LENGTH_SHORT).show();
                                    dialogLoading.dismiss();
                                }
                            }, 1000);

                } catch (IOException e) {
                    Log.e("getSearchMoviesFromREST","FAILED BODY.STRING()");
                    dialogLoading.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) { }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
