package com.myst3ry.bookfinder.network;

import com.myst3ry.bookfinder.model.BooksList;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleApi {

    String API_BASE_URL = "https://www.googleapis.com/books/v1/";
    String API_KEY = "AIzaSyCE4vuEPvkzBTGTGeowN93KQcv5fecS7PU";

    @GET("volumes")
    Single<BooksList> getBooksWithQuery(@Query("q") final String query,
                                        @Query("key") final String key);

//    @GET("volumes/{id}")
//    Single<BooksList> getBookFullDetails(@Path("id") final String id, @Query("key") final String key);

}