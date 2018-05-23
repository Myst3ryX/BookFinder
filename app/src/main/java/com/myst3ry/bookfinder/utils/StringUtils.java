package com.myst3ry.bookfinder.utils;

import java.util.List;

public final class StringUtils {

    //get authors from list as one string
    public static String getAuthorsString(final List<String> authors) {
        if (authors == null) {
            return "";
        }

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < authors.size(); i++) {
            sb.append(authors.get(i));
            if (i < authors.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    //clear all tags from description
    public static String reformatDescriptionString(final String description) {
        return description != null ? description.replaceAll("<[^>]*>", "") : "";
    }

    //split categories, get only one string from list
    public static String[] getCategoriesArray(final List<String> categories) {
        if (categories == null) {
            return null;
        }
        return categories.get(0).split("[/&]");
    }
}
