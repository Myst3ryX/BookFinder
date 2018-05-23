package com.myst3ry.bookfinder.model;

import com.google.gson.annotations.SerializedName;

public final class BookSaleDetails {

    @SerializedName("buyLink")
    private String buyLink;

    @SerializedName("retailPrice")
    private RetailPrice retailPrice;

    public String getBuyLink() {
        return buyLink;
    }

    public RetailPrice getRetailPrice() {
        return retailPrice;
    }

    public final class RetailPrice {

        @SerializedName("amount")
        private double amount;

        @SerializedName("currencyCode")
        private String currencyCode;

        public double getAmount() {
            return amount;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }
    }
}