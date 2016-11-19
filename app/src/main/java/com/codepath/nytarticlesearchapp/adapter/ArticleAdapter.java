package com.codepath.nytarticlesearchapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.nytarticlesearchapp.R;
import com.codepath.nytarticlesearchapp.activity.DetailsActivity;
import com.codepath.nytarticlesearchapp.model.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.codepath.nytarticlesearchapp.R.id.tvArticleTitle;

/**
 * Created by mallikaa on 11/16/16.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {
    private static class ViewHolder {
        ImageView imageView;
        TextView title;
    }
    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Article article = getItem(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.article, parent, false);
            viewHolder = getViewFields(article, inflater, convertView, parent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(article.getHeadline());
        if (article.getImage_url() != null) {
            System.out.println(article.getImage_url());
            Picasso.with(getContext()).load(article.getImage_url()).into(viewHolder.imageView);
        }
        convertView.setClickable(true);
        convertView.setFocusable(true);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showArticleDetails(article);
            }
        });
        return convertView;
    }

    private ViewHolder getViewFields(Article article, LayoutInflater inflater, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iVArticleThumbnail);
        viewHolder.title = (TextView) convertView.findViewById(tvArticleTitle);
        return viewHolder;
    }
    private void showArticleDetails(Article article) {
        String url = article.getArticle_url();
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("url", url);
        getContext().startActivity(intent);
    }
}
