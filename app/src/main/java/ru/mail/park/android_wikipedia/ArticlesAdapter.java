package ru.mail.park.android_wikipedia;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import wikipedia.Article;
/**
 * Created by ivansemenov on 16.12.15.
 */
public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder> {
    private List<Article> articleInfoList;

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
        articleViewHolder.vImage.setImageBitmap(ar.getLogoBitmap());
        articleViewHolder.vTitle.setText(ar.getTitle());
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.article_card, viewGroup, false);

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

}
