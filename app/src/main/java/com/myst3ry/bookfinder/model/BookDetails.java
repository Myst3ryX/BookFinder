package com.myst3ry.bookfinder.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class BookDetails {

    @SerializedName("title")
    private String title;

    @SerializedName("subtitle")
    private String subtitle;

    @SerializedName("authors")
    private List<String> authors;

    @SerializedName("publisher")
    private String publisher;

    @SerializedName("publishedDate")
    private String publishedDate;

    @SerializedName("description")
    private String description;

    @SerializedName("pageCount")
    private int pageCount;

    @SerializedName("categories")
    private List<String> categories;

    @SerializedName("averageRating")
    private double averageRating;

    @SerializedName("ratingsCount")
    private int ratingsCount;

    @SerializedName("imageLinks")
    private Covers covers;

    @SerializedName("language")
    private String language;

    @SerializedName("infoLink")
    private String infoLink;

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public int getPageCount() {
        return pageCount;
    }

    public List<String> getCategories() {
        return categories;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public Covers getCovers() {
        return covers;
    }

    public String getLanguage() {
        return language;
    }

    public String getInfoLink() {
        return infoLink;
    }


    public final class Covers {

        @SerializedName("thumbnail")
        private String thumbnail;

        @SerializedName("large")
        private String large;

        public String getThumbnail() {
            return thumbnail;
        }

        public String getLarge() {
            return large;
        }
    }
}