package com.gaeasystem.devtestthemovies;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface apiInterface {

    /**
     * Get the details of a list.
     * @link https://developers.themoviedb.org/3/lists/get-list-details
     * @author  Vergie Hadiana
     * @version 1.0
     * @since   2018-09-27
     *
     * @param listID string or integer
     * @param keyAPI 1 validations.  <<api_key_themoviesdb>>
     * @return
     */
    @GET("list/{list_id}?")
    Call<ResponseBody> doGetPageListDetail(@Path("list_id") Integer listID, @Query("api_key") String keyAPI);

    /**
     * Get the most newly created movie. This is a live response and will continuously change.
     * @link https://developers.themoviedb.org/3/movies/get-latest-movie
     * @author  Vergie Hadiana
     * @version 1.0
     * @since   2018-09-27
     *
     * @param keyAPI 1 validations.  <<api_key_themoviesdb>>
     * @return
     */
    @GET("movie/latest?")
    Call<ResponseBody> doGetLastestMovies(@Query("api_key") String keyAPI);

    /**
     * Get a list of the current popular movies on TMDb. This list updates daily.
     * @link https://developers.themoviedb.org/3/movies/get-popular-movies
     * @author  Vergie Hadiana
     * @version 1.0
     * @since   2018-09-27
     *
     * @param keyAPI 1 validations.  <<api_key_themoviesdb>>
     * @param page integer (Optional)
     * @return
     */
    @GET("movie/popular?")
    Call<ResponseBody> doGetPopularMovies(@Query("api_key") String keyAPI, @Query("page") Integer page);

    /**
     * Get the primary information about a movie.
     * @link https://developers.themoviedb.org/3/movies/get-movie-details
     * @author  Vergie Hadiana
     * @version 1.0
     * @since   2018-09-27
     *
     * @param moviesID integer
     * @param keyAPI 1 validations.  <<api_key_themoviesdb>>
     * @return
     */
    @GET("list/{movies_id}?")
    Call<ResponseBody> doGetMoviesDetail(@Path("movies_id") Integer moviesID, @Query("api_key") String keyAPI);

    /**
     * Search for movies.
     * @link https://developers.themoviedb.org/3/search/search-movies
     * @author  Vergie Hadiana
     * @version 1.0
     * @since   2018-09-27
     *
     * @param keyAPI 1 validations.  <<api_key_themoviesdb>>
     * @param querySearch Pass a text query to search. This value should be URI encoded. minLength: 1
     * @param pageSearch Specify which page to query. minimum: 1, maximum: 1000, default: 1 (Optional)
     * @return
     */
    @GET("search/movie?")
    Call<ResponseBody> doSearchMovies(@Query("api_key") String keyAPI, @Query("query") String querySearch, @Query("page") Integer pageSearch);

    /**
     * Search for keywords.
     * @link https://developers.themoviedb.org/3/search/search-keywords
     * @author  Vergie Hadiana
     * @version 1.0
     * @since   2018-09-27
     *
     * @param keyAPI 1 validations.  <<api_key_themoviesdb>>
     * @param querySearch Pass a text query to search. This value should be URI encoded. minLength: 1
     * @param pageSearch Specify which page to query. minimum: 1, maximum: 1000, default: 1 (Optional)
     * @return
     */
    @GET("search/keyword?")
    Call<ResponseBody> doSearchKeywords(@Query("api_key") String keyAPI, @Query("query") String querySearch, @Query("page") Integer pageSearch);

}
