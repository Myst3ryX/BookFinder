package com.myst3ry.bookfinder.model;

import com.google.gson.annotations.SerializedName;

public final class Book {

    @SerializedName("id")
    private String id;

    @SerializedName("volumeInfo")
    private BookDetails bookDetails;

    @SerializedName("saleInfo")
    private BookSaleDetails bookSaleDetails;

    public String getId() {
        return id;
    }

    public BookDetails getBookDetails() {
        return bookDetails;
    }

    public BookSaleDetails getBookSaleDetails() {
        return bookSaleDetails;
    }

}