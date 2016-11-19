package com.codepath.nytarticlesearchapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.nytarticlesearchapp.R;
import com.codepath.nytarticlesearchapp.activity.DetailsActivity;
import com.codepath.nytarticlesearchapp.model.Article;

import java.util.List;

import static com.codepath.nytarticlesearchapp.R.layout.article;

/**
 * Created by mallikaa on 11/18/16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Article> articles;
    private Context context;
    private OnItemClickListener listener;
    private int WITHOUTIMAGE = 1;
    private int WITHIMAGE = 0;

    public RecyclerViewAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
        this.listener = getOnItemClickListener();
    }

    private OnItemClickListener getOnItemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(Article article) {
                String url = article.getArticle_url();
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("url", url);
                context.startActivity(intent);
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View convertView;
        RecyclerView.ViewHolder viewHolder;
        if (viewType == WITHIMAGE) {
            convertView = inflater.inflate(article, parent, false);
            viewHolder = new ViewHolderWithImage(convertView);
        } else {
            convertView = inflater.inflate(R.layout.article_without_image, parent, false);
            viewHolder = new ViewHolderWithOutImage(convertView);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = articles.get(position);
        int viewType = viewHolder.getItemViewType();
        if (viewType == WITHIMAGE) {
            configureViewHolderWithImage((ViewHolderWithImage) viewHolder, article);
        } else {
            configureViewHolderWithOutImage((ViewHolderWithOutImage) viewHolder, article);
        }

        // Set item views based on your views and data model

    }

    private void configureViewHolderWithImage(ViewHolderWithImage viewHolder, Article article) {
        TextView textView = viewHolder.title;
        textView.setText(article.getHeadline());
        ImageView imageView = viewHolder.image;
        Glide.with(context).load(article.getImage_url()).into(imageView);
        viewHolder.bind(article, listener);
    }

    private void configureViewHolderWithOutImage(ViewHolderWithOutImage viewHolder, Article article) {
        TextView textView = viewHolder.title;
        textView.setText(article.getHeadline());
        viewHolder.bind(article, listener);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (articles.get(position).getImage_url() == null) {
            return WITHOUTIMAGE;
        }
        return WITHIMAGE;
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolderWithImage extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView title;
        public ImageView image;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolderWithImage(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tvArticleTitle);
            image = (ImageView) itemView.findViewById(R.id.iVArticleThumbnail);

        }

        public void bind(final Article article, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(article));
        }
    }

    public static class ViewHolderWithOutImage extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView title;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolderWithOutImage(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tvArticleTitle);

        }

        public void bind(final Article article, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(article));
        }
    }
}


