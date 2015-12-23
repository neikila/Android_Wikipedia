package ru.mail.park.android_wikipedia;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wikipedia.Article;
/**
 * Created by ivansemenov on 16.12.15.
 */
public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder> {
    private List<Article> articleInfoList;
    private static Bitmap defaultBitmap;
    private View.OnClickListener listener;
    private int layoutId;

    public ArticlesAdapter(View.OnClickListener listener) {
        this(listener, new ArrayList<Article>(), R.layout.article_card_horizontal);
    }

    public ArticlesAdapter(View.OnClickListener listener, int layoutId) {
        this(listener, new ArrayList<Article>(), layoutId);
    }

    public ArticlesAdapter(View.OnClickListener listener, List<Article> articleList) {
        this(listener, articleList, R.layout.article_card_horizontal);
    }

    public ArticlesAdapter(View.OnClickListener listener, List<Article> articleList, int layoutId) {
        this.listener = listener;
        this.articleInfoList = articleList;
        this.layoutId = layoutId;
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
                inflate(layoutId, viewGroup, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
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
