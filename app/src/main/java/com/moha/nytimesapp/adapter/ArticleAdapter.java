package com.moha.nytimesapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.moha.nytimesapp.modelDB.Favorite;
import com.moha.nytimesapp.modelDB.FavoriteViewModel;
import com.moha.nytimesapp.modal.Article;
import com.moha.nytimesapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    private List<Article> articleList;
    private Context context;
    private boolean isDark = false;
    private OnItemClickListener listener;

    public ArticleAdapter(List<Article> articlesList, Context context, boolean isDark) {
        this.articleList = articlesList;
        this.context = context;
        this.isDark = isDark;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.news_model, viewGroup, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ArticleViewHolder articleViewHolder, final int i) {
        Article article = articleList.get(i);
        articleViewHolder.imageView.setAnimation(AnimationUtils.loadAnimation
                (context, R.anim.fade_transition_anim));
        articleViewHolder.linearLayout.setAnimation(AnimationUtils.loadAnimation
                (context, R.anim.fade_scale_animation));
        String headLine = article.getHeadLine();
        String summary = article.getSummary();
        String publishDate = article.getPublishDate();
        articleViewHolder.txt_headline.setText(headLine);
        articleViewHolder.txt_summary.setText(summary);
        articleViewHolder.txt_date.setText(publishDate);
        if (article.mediaList.get(0).metadata.size() > 2) {
            Picasso.get().load(article.getMediaList().get(0).getMetadata().get(1).getImgUrl())
                    .placeholder(R.drawable.no_image_available)
                    .into(articleViewHolder.imageView);
        }

        final int position = articleViewHolder.getAdapterPosition();
        if (FavoriteViewModel.isFavorite((int) articleList.get(position).id) == 1) {
            articleViewHolder.btn_add_to_fav.setImageResource(R.drawable.ic_favorite);
        } else {
            articleViewHolder.btn_add_to_fav.setImageResource(R.drawable.ic_favorite_border);
        }
        articleViewHolder.btn_add_to_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FavoriteViewModel.isFavorite((int) articleList.get(position).id) != 1) {
                    addOrRemoveFavorite(articleList.get(position), true);
                    articleViewHolder.btn_add_to_fav.setImageResource(R.drawable.ic_favorite);
                    Toast.makeText(context, "Item added to favorites..", Toast.LENGTH_SHORT).show();
                } else {
                    addOrRemoveFavorite(articleList.get(position), false);
                    articleViewHolder.btn_add_to_fav.setImageResource(R.drawable.ic_favorite_border);
                    Toast.makeText(context, "Item removed from favorites..", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void addOrRemoveFavorite(Article article, boolean isAdd) {
        Favorite favorite = new Favorite();
        favorite.id = (int) article.id;
        favorite.webUrl = article.getWebUrl();
        favorite.headline = article.getHeadLine();
        favorite.summary = article.getSummary();
        favorite.publishDate = article.getPublishDate();
        favorite.imgUrl = article.mediaList.get(0).metadata.get(1).getImgUrl();
        Log.d("TAG", new Gson().toJson(favorite));

        if (isAdd) {
            FavoriteViewModel.insert(favorite);
        } else {
            FavoriteViewModel.delete(favorite);
        }
    }

    @Override
    public int getItemCount() {
        return (articleList == null) ? 0 : articleList.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_headline, txt_summary, txt_date;
        private ImageView imageView;
        private LinearLayout linearLayout;
        private ImageView btn_add_to_fav;

        private ArticleViewHolder(@NonNull final View itemView) {
            super(itemView);
            txt_headline = itemView.findViewById(R.id.txt_headline);
            txt_summary = itemView.findViewById(R.id.txt_summary);
            txt_date = itemView.findViewById(R.id.txt_date);
            imageView = itemView.findViewById(R.id.model_img);
            linearLayout = itemView.findViewById(R.id.anim_container);
            btn_add_to_fav = itemView.findViewById(R.id.btn_add_favorite);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {

                            listener.OnItemClick(position);
                        }
                    }
                }
            });
            if (isDark) {
                setDarkTheme();
            }
        }

        private void setDarkTheme() {
            linearLayout.setBackgroundResource(R.drawable.card_black_bg);
            txt_headline.setTextColor(Color.rgb(240, 248, 255));
            txt_summary.setTextColor(Color.WHITE);
            txt_date.setTextColor(Color.WHITE);
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

