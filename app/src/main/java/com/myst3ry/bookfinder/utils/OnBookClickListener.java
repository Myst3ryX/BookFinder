package com.myst3ry.bookfinder.utils;

import android.support.annotation.NonNull;

public interface OnBookClickListener {

    void onBookClick(@NonNull final String bookId, final String bookTitle);
}
