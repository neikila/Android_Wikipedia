package ru.mail.park.android_wikipedia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import ru.mail.park.android_wikipedia.fragments.ArticleFragment;
import utils.BitmapReady;
import wikipedia.Article;
/**
 * Created by ivansemenov on 16.12.15.
 */
public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder> {
    private List<Article> articleInfoList;
    private static Bitmap defaultBitmap;

    public ArticlesAdapter() {
        this(new ArrayList<Article>());
    }

    public ArticlesAdapter(List<Article> articleList) {
        this.articleInfoList = articleList;
    }

    @Override
    public int getItemCount() {
        return articleInfoList.size();
    }

    public void setArticles(List<Article> articles) {
        articleInfoList.clear();
        articleInfoList.addAll(articles);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder articleViewHolder, int i) {

        Article ar = articleInfoList.get(i);
        File imgFile = new File(ar.getBody());
        // TODO get real logo
        if (ar.getLogoBitmap() != null) {
            articleViewHolder.vImage.setImageBitmap(ar.getLogoBitmap());
        } else {
            articleViewHolder.vImage.setImageBitmap(defaultBitmap);
        }
        articleViewHolder.vTitle.setText(ar.getTitle());
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.article_card, viewGroup, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(viewGroup.getContext(), BaseActivity.class);
                search.setAction("android.intent.action.OPEN_ARTICLE");
                search.putExtra(ArticleFragment.ARTICLE_TITLE_TAG, "Saved test article");
                viewGroup.getContext().startActivity(search);
            }
        });
        return new ArticleViewHolder(itemView);
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        protected TextView vTitle;
        protected ImageView vImage;

        public ArticleViewHolder(View v) {
            super(v);
            vImage =  (ImageView) v.findViewById(R.id.logo);
            vTitle = (TextView) v.findViewById(R.id.title);
        }
    }

    public static void setDefaultBitmap(Bitmap bitmap) {
        ArticlesAdapter.defaultBitmap = bitmap;
    }
}
