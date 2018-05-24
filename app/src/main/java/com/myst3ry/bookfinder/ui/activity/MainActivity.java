package com.myst3ry.bookfinder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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

    private static final int MAX_RESULTS_PER_REQUEST = 40;

    private BooksAdapter adapter;
    private SearchView searchView;
    private String textQuery;
    private boolean isLoading;
    private int totalBooks;
    private int startIndex = 0;

    @Inject
    GoogleApi googleApi;

    @BindView(R.id.books_rec_view)
    RecyclerView booksRecyclerView;
    @BindView(R.id.text_empty)
    TextView textEmpty;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BookFinderApp.getNetworkComponent(this).inject(this);
        initAdapter();

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.main_title);
        }

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        booksRecyclerView.setLayoutManager(linearLayoutManager);
        booksRecyclerView.setWillNotDraw(false);
        booksRecyclerView.setAdapter(adapter);
        booksRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (!isLoading) {
                        if ((visibleItemCount + pastVisibleItems + 15) >= totalItemCount) {
                            startIndex += MAX_RESULTS_PER_REQUEST;
                            if (startIndex < totalBooks)
                                getBooksWithQuery(textQuery, startIndex);
                            else if (startIndex > totalBooks && startIndex - totalBooks < MAX_RESULTS_PER_REQUEST) {
                                startIndex = totalBooks;
                                getBooksWithQuery(textQuery, startIndex - MAX_RESULTS_PER_REQUEST);
                            }
                        }
                    }
                }
            }
        });
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

        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                textQuery = query;
                startIndex = 0;
                adapter.clear();
                getBooksWithQuery(textQuery, startIndex);
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
        adapter = new BooksAdapter((bookId, bookTitle) -> {
            final Intent intent = new Intent(MainActivity.this, BookDetailsActivity.class);
            intent.putExtra(BookDetailsActivity.EXTRA_BOOK_ID, bookId);
            intent.putExtra(BookDetailsActivity.EXTRA_BOOK_TITLE, bookTitle);
            startActivity(intent);
        });
    }

    private void getBooksWithQuery(final String queryText, final int startIndex) {
        if (!TextUtils.isEmpty(queryText)) {
            disposables.add(googleApi.getBooksWithQuery(queryText, MAX_RESULTS_PER_REQUEST, startIndex)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .cache()
                    .doOnSubscribe((subscribe) -> {
                        isLoading = true;
                        progressBar.setVisibility(adapter.getItemCount() == 0 ?
                                View.VISIBLE : View.GONE);
                    })
                    .subscribe((response) -> {
                        if (response.getItems() == null) {
                            Toast.makeText(this,
                                    String.format(getString(R.string.toast_books_not_found), queryText),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            totalBooks = response.getTotalBooksCount();
                            updateUI(response.getItems());
                        }
                    }, error -> {
                        Toast.makeText(this, String.format(getString(R.string.toast_error),
                                error.getMessage()), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        isLoading = false;
                    }));
        }
    }

    private void updateUI(@NonNull final List<Book> books) {
        if (adapter.getItemCount() == 0) {
            booksRecyclerView.scrollToPosition(0);
            adapter.setBooks(books);
            Toast.makeText(this, String.format(getString(R.string.toast_books_found_count), totalBooks),
                    Toast.LENGTH_SHORT).show();
        } else {
            adapter.addMoreBooks(books);
        }

        if (isLoading) {
            isLoading = false;
        }

        if (textEmpty != null) {
            textEmpty.setVisibility(!books.isEmpty() ? View.GONE : View.VISIBLE);
        }

        if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }
}
