package com.myst3ry.bookfinder.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.myst3ry.bookfinder.BookFinderApp;
import com.myst3ry.bookfinder.R;
import com.myst3ry.bookfinder.model.Book;
import com.myst3ry.bookfinder.network.GoogleApi;
import com.myst3ry.bookfinder.ui.adapter.BooksAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public final class MainActivity extends BaseActivity {

    private BooksAdapter adapter;
    private SearchView searchView;

    @Inject
    GoogleApi googleApi;

    @BindView(R.id.books_rec_view)
    RecyclerView booksRecyclerView;
    @BindView(R.id.text_empty)
    TextView textEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BookFinderApp.getNetworkComponent(this).inject(this);
        initAdapter();

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        booksRecyclerView.setLayoutManager(linearLayoutManager);
        booksRecyclerView.setWillNotDraw(false);
        booksRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (searchView != null) {
            searchView.clearFocus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getBooksWithQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void initAdapter() {
        adapter = new BooksAdapter((String bookId) -> {
            final Intent intent = new Intent(MainActivity.this, BookDetailsActivity.class);
            intent.putExtra(BookDetailsActivity.EXTRA_BOOK_ID, bookId);
            startActivity(intent);
        });
    }

    @SuppressLint("StringFormatInvalid")
    private void getBooksWithQuery(final String queryText) {
        if (!TextUtils.isEmpty(queryText)) {
            disposables.add(googleApi.getBooksWithQuery(queryText, googleApi.API_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .cache()
                    .subscribe((response) -> {
                                final List<Book> booksList = response.getItems();
                                if (booksList == null) {
                                    Toast.makeText(this,
                                            String.format(getString(R.string.toast_books_not_found), queryText),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    updateUI(response.getItems());
                                }
                            },
                            error -> {
                            }));
        }
    }

    private void updateUI(@NonNull final List<Book> books) {
        if (textEmpty != null) {
            textEmpty.setVisibility(!books.isEmpty() ? View.GONE : View.VISIBLE);
        }

        if (adapter != null) {
            adapter.setBooks(books);
        }
    }
}
