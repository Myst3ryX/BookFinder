package com.myst3ry.bookfinder.model;

import com.google.gson.annotations.SerializedName;

public final class BookSaleDetails {

    @SerializedName("country")
    private String country;

    @SerializedName("isEbook")
    private boolean isEbook;

    @SerializedName("saleability")
    private String saleability;

    @SerializedName("buyLink")
    private String buyLink;

    @SerializedName("retailPrice")
    private RetailPrice retailPrice;

    @SerializedName("listPrice")
    private ListPrice listPrice;


    public boolean isIsEbook() {
        return isEbook;
    }

    public String getSaleability() {
        return saleability;
    }

    public String getBuyLink() {
        return buyLink;
    }

    public RetailPrice getRetailPrice() {
        return retailPrice;
    }

    public ListPrice getListPrice() {
        return listPrice;
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

    public final class ListPrice {

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