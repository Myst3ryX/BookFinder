package com.myst3ry.bookfinder.ui.adapter;

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
        if (book != null && book.getBookDetails() != null) {
            final String thumbnailUrl = book.getBookDetails().getCovers() != null
                    ? book.getBookDetails().getCovers().getThumbnail() : null;
            final String title = book.getBookDetails().getTitle();
            final List<String> authors = book.getBookDetails().getAuthors();
            final StringBuilder sb = new StringBuilder();

            GlideApp.with(holder.itemView.getContext())
                    .load(thumbnailUrl)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.color.color_placeholder)
                    .into(holder.bookSmallCover);

            holder.bookTitle.setText(title);

            if (authors != null) {
                for (int i = 0; i < authors.size(); i++) {
                    sb.append(authors.get(i));
                    if (i < authors.size() - 1) {
                        sb.append(", ");
                    }
                }
            }

            holder.bookAuthors.setText(sb.toString());
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
        @BindView(R.id.book_title)
        TextView bookTitle;
        @BindView(R.id.book_authors)
        TextView bookAuthors;

        @OnClick(R.id.book_card_container)
        public void onClick() {
            final int position = getLayoutPosition();
            onBookClickListener.onBookClick(getBook(position).getId());
        }

        BookHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}