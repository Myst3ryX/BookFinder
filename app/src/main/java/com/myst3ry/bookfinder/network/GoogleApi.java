package com.myst3ry.bookfinder.network;

import com.myst3ry.bookfinder.model.Book;
import com.myst3ry.bookfinder.model.BooksList;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GoogleApi {

    String API_BASE_URL = "https://www.googleapis.com/books/v1/";

    @GET("volumes")
    Single<BooksList> getBooksWithQuery(@Query("q") final String query);

    @GET("volumes/{id}")
    Single<Book> getBookFullDetails(@Path("id") final String id);

}