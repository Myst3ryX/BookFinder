package com.myst3ry.bookfinder.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class BooksList {

    @SerializedName("items")
    private List<Book> books;

    @SerializedName("totalItems")
    private int totalBooksCount;


    public List<Book> getItems() {
        return books;
    }

    public int getTotalBooksCount() {
        return totalBooksCount;
    }

}