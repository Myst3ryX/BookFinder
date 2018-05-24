package com.myst3ry.bookfinder.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.myst3ry.bookfinder.BookFinderApp;
import com.myst3ry.bookfinder.BuildConfig;
import com.myst3ry.bookfinder.R;
import com.myst3ry.bookfinder.model.Book;
import com.myst3ry.bookfinder.network.GlideApp;
import com.myst3ry.bookfinder.network.GoogleApi;
import com.myst3ry.bookfinder.utils.StringUtils;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class BookDetailsActivity extends BaseActivity {

    public static final String EXTRA_BOOK_ID = BuildConfig.APPLICATION_ID + "extra.book.id";
    public static final String EXTRA_BOOK_TITLE = BuildConfig.APPLICATION_ID + "extra.book.title";

    private Book book;

    @Inject
    GoogleApi googleApi;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.book_cover_large)
    ImageView bookLargeCover;
    @BindView(R.id.book_details_title)
    TextView bookTitle;
    @BindView(R.id.book_details_subtitle)
    TextView bookSubtitle;
    @BindView(R.id.book_details_authors)
    TextView bookAuthors;
    @BindView(R.id.book_details_publisher)
    TextView bookPublisher;
    @BindView(R.id.book_details_page_count)
    TextView bookPageCount;
    @BindView(R.id.book_details_language)
    TextView bookLanguage;

    @BindView(R.id.book_details_rating)
    TextView bookRating;
    @BindView(R.id.book_details_price)
    TextView bookPrice;
    @BindView(R.id.book_details_buy_btn)
    TextView bookBuyBtn;

    @BindView(R.id.book_details_category_one)
    TextView bookCategoryOne;
    @BindView(R.id.book_details_category_two)
    TextView bookCategoryTwo;
    @BindView(R.id.book_details_category_three)
    TextView bookCategoryThree;

    @BindView(R.id.book_details_description)
    TextView bookDescription;

    @BindView(R.id.divider_1)
    View dividerOne;
    @BindView(R.id.divider_2)
    View dividerTwo;
    @BindView(R.id.divider_3)
    View dividerThree;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        BookFinderApp.getNetworkComponent(this).inject(this);

        final String bookId = getIntent().getStringExtra(EXTRA_BOOK_ID);
        final String bookTitle = getIntent().getStringExtra(EXTRA_BOOK_TITLE);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(bookTitle);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        getBookFullDetails(bookId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_share:
                shareBookLink(book);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getBookFullDetails(@NonNull final String bookId) {
        disposables.add(googleApi.getBookFullDetails(bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .doOnSubscribe((subscribe) -> progressBar.setVisibility(View.VISIBLE))
                .subscribe((response) -> {
                    if (response != null) {
                        updateUI(response);
                    }
                }, error -> {
                    Toast.makeText(this, String.format(getString(R.string.toast_error),
                            error.getMessage()), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }));
    }

    private void updateUI(@NonNull Book book) {
        if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }

        final String thumbnailCoverUrl = book.getBookDetails().getCovers() != null
                ? book.getBookDetails().getCovers().getThumbnail() : null;
        final String largeCoverUrl = book.getBookDetails().getCovers() != null
                ? book.getBookDetails().getCovers().getLarge() : null;
        final String date = book.getBookDetails().getPublishedDate() != null
                ? book.getBookDetails().getPublishedDate() : "";
        final double price = book.getBookSaleDetails().getRetailPrice() != null
                ? book.getBookSaleDetails().getRetailPrice().getAmount() : 0.0;
        final int pages = book.getBookDetails().getPageCount();
        final String lang = book.getBookDetails().getLanguage();
        final String description = book.getBookDetails().getDescription();
        final String[] categories = StringUtils.getCategoriesArray(book.getBookDetails().getCategories());

        GlideApp.with(this)
                .load(largeCoverUrl != null ? largeCoverUrl : thumbnailCoverUrl)
                .centerInside()
                .transition(DrawableTransitionOptions.withCrossFade(400))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.color.color_placeholder)
                .into(bookLargeCover);

        bookTitle.setText(book.getBookDetails().getTitle());
        bookSubtitle.setText(book.getBookDetails().getSubtitle());
        bookAuthors.setText(StringUtils.getAuthorsString(book.getBookDetails().getAuthors()));
        bookPublisher.setText(String.format(getString(R.string.book_published_by), book.getBookDetails().getPublisher(), date));

        bookLanguage.setText(String.format(getString(R.string.book_language), TextUtils.equals(lang, "ru")
                ? getString(R.string.language_russian) : TextUtils.equals(lang, "en")
                ? getString(R.string.language_english) : lang));

        bookPageCount.setText(String.format(getString(R.string.book_page_count), pages));

        bookRating.setText((String.format(getString(R.string.book_average_rating),
                book.getBookDetails().getAverageRating()) + " (" + book.getBookDetails().getRatingsCount() + ")"));

        bookPrice.setText(price != 0.0 ? String.format(getString(R.string.book_price),
                price, book.getBookSaleDetails().getRetailPrice().getCurrencyCode())
                : getString(R.string.book_not_for_sale));

        bookBuyBtn.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(book.getBookSaleDetails().getBuyLink()))));

        bookDescription.setText(String.format(getString(R.string.book_description),
                StringUtils.reformatDescriptionString(description)));

        //set visibility for dividers and fields with description or with border
        bookDescription.setVisibility(description != null ? View.VISIBLE : View.GONE);
        bookLanguage.setVisibility(lang != null ? View.VISIBLE : View.GONE);
        bookPageCount.setVisibility(pages != 0 ? View.VISIBLE : View.GONE);
        bookBuyBtn.setVisibility(price != 0.0 ? View.VISIBLE : View.GONE);
        bookRating.setVisibility(View.VISIBLE);
        bookPrice.setVisibility(View.VISIBLE);

        dividerOne.setVisibility(pages != 0 || lang != null ? View.VISIBLE : View.GONE);
        dividerTwo.setVisibility(categories != null ? View.VISIBLE : View.GONE);
        dividerThree.setVisibility(description != null ? View.VISIBLE : View.GONE);

        setCategories(categories);

        this.book = book;
    }

    private void setCategories(final String[] categories) {
        if (categories == null) {
            return;
        }

        final int size = categories.length > 3 ? 3 : categories.length;
        switch (size) {
            case 3:
                bookCategoryThree.setText(categories[2]);
                bookCategoryThree.setVisibility(View.VISIBLE);
            case 2:
                bookCategoryTwo.setText(categories[1]);
                bookCategoryTwo.setVisibility(View.VISIBLE);
            case 1:
                bookCategoryOne.setText(categories[0]);
                bookCategoryOne.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void shareBookLink(@NonNull Book book) {
        if (book.getBookDetails() != null) {
            final String bookLink = book.getBookDetails().getInfoLink();
            if (bookLink != null) {
                final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, bookLink.split("&")[0]);
                startActivity(Intent.createChooser(shareIntent, book.getBookDetails().getTitle()));
            }
        }
    }
}
