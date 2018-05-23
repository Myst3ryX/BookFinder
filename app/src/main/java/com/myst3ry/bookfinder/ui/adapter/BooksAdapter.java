package com.myst3ry.bookfinder.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.myst3ry.bookfinder.R;
import com.myst3ry.bookfinder.model.Book;
import com.myst3ry.bookfinder.network.GlideApp;
import com.myst3ry.bookfinder.utils.OnBookClickListener;
import com.myst3ry.bookfinder.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public final class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookHolder> {

    private final OnBookClickListener onBookClickListener;
    private List<Book> books;

    public BooksAdapter(final OnBookClickListener onBookClickListener) {
        this.onBookClickListener = onBookClickListener;
        books = new ArrayList<>();
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        final Book book = getBook(position);
        final Context context = holder.itemView.getContext();

        if (book != null) {
            final String thumbnailUrl = book.getBookDetails().getCovers() != null
                    ? book.getBookDetails().getCovers().getThumbnail() : null;
            final String rating = String.format(context.getString(R.string.book_average_rating),
                    book.getBookDetails().getAverageRating());
            final String published = book.getBookDetails().getPublishedDate() != null
                    ? book.getBookDetails().getPublishedDate().substring(0, 4) : null;
            final double price = book.getBookSaleDetails().getRetailPrice() != null
                    ? book.getBookSaleDetails().getRetailPrice().getAmount() : 0.0;

            GlideApp.with(context)
                    .load(thumbnailUrl)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.color.color_placeholder)
                    .into(holder.bookSmallCover);

            holder.bookPublishedYear.setText(published);
            holder.bookTitle.setText(book.getBookDetails().getTitle());
            holder.bookRating.setText(rating);
            holder.bookAuthors.setText(StringUtils.getAuthorsString(book.getBookDetails().getAuthors()));
            holder.bookRetailPrice.setText(price != 0.0 ? String.format(context.getString(R.string.book_price),
                    price, book.getBookSaleDetails().getRetailPrice().getCurrencyCode())
                    : context.getString(R.string.book_not_for_sale));
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setBooks(@NonNull List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public void addMoreBooks(List<Book> moreBooks) {
        books.addAll(moreBooks);
        notifyDataSetChanged();
    }

    public void clear() {
        books.clear();
        notifyDataSetChanged();
    }

    private Book getBook(int position) {
        return books.get(position);
    }

    final class BookHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.book_cover_small)
        ImageView bookSmallCover;
        @BindView(R.id.book_published_year)
        TextView bookPublishedYear;
        @BindView(R.id.book_title)
        TextView bookTitle;
        @BindView(R.id.book_authors)
        TextView bookAuthors;
        @BindView(R.id.book_rating)
        TextView bookRating;
        @BindView(R.id.book_price_retail)
        TextView bookRetailPrice;

        @OnClick(R.id.book_card_container)
        public void onClick() {
            final Book book = getBook(getLayoutPosition());
            onBookClickListener.onBookClick(book.getId(), book.getBookDetails().getTitle());
        }

        BookHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}