package com.myst3ry.bookfinder.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.myst3ry.bookfinder.BuildConfig;
import com.myst3ry.bookfinder.R;


public final class BookDetailsActivity extends BaseActivity {

    public static final String EXTRA_BOOK_ID = BuildConfig.APPLICATION_ID + "extra.book.id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        final String bookId = getIntent().getStringExtra(EXTRA_BOOK_ID);
        getBookFullDetails(bookId);
    }

    private void getBookFullDetails(@NonNull final String bookId) {
    }
}
