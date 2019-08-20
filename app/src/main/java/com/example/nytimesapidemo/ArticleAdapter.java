package com.example.nytimesapidemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ARViewHolder> {
    private List<Article> articleList;
    private Context context;
    public static List<Article> offlineArticles = new ArrayList<>();
    private boolean isDark = false;
    private OnItemClickListener listener;

    public ArticleAdapter(List<Article> articlesList, Context context, boolean isDark) {
        this.articleList = articlesList;
        this.context = context;
        this.isDark = isDark;
    }



    public ArticleAdapter(List<Article> articlesList, Context context) {
        this.articleList = articlesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ARViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.news_model, viewGroup, false);
        return new ARViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ARViewHolder arViewHolder, final int i) {


        final Article article = articleList.get(i);


        if (article != null) {
            arViewHolder.imageView.setAnimation(AnimationUtils.loadAnimation
                    (context, R.anim.fade_transition_anim));
            arViewHolder.linearLayout.setAnimation(AnimationUtils.loadAnimation
                    (context, R.anim.fade_scale_animation));
            arViewHolder.txt_headline.setText(article.getHeadLine());
            arViewHolder.txt_summary.setText(article.getSummary());
            arViewHolder.txt_date.setText(article.getPublishDate());
            arViewHolder.imageView.setImageResource(0);
            if (article.mediaList.get(0).metadata.size() > 2) {
                Picasso.get().load(article.getMediaList().get(0).getMetadata().get(1).getImgUrl())
                        .resize(70, 70).centerCrop().into(arViewHolder.imageView);

            }

            arViewHolder.toggleButton.setTextOff("Add");
            arViewHolder.toggleButton.setTextOn("Remove");
            arViewHolder.toggleButton.setChecked(article.isFavorite());
            arViewHolder.toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    article.isFavorite = !article.isFavorite;
                    if (!NetworkUtils.isNetworkAvailable(context)){
                        FavoriteDbHelper dbHelper= new FavoriteDbHelper(context);
                        dbHelper.updateArticle(article);
                    }

                    if (article.isFavorite){
                        arViewHolder.toggleButton.setTextOff("Add");
                        offlineArticles.add(article);
                        Toast.makeText(context, "Added to favorites..press on ♥ ", Toast.LENGTH_SHORT).show();

                    }else {
                        arViewHolder.toggleButton.setTextOn("Remove");
                        offlineArticles.clear();
                        offlineArticles.remove(article);
                        Toast.makeText(context, "Item removed..☺ ", Toast.LENGTH_SHORT).show();

                    }
                }
            });


        }

    }

    public void addArticles(Article articles){
        articleList.add(articles);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }


    public class ARViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_headline, txt_summary, txt_date;
        private ImageView imageView;
        private LinearLayout linearLayout;
        private ToggleButton toggleButton;


        private ARViewHolder(@NonNull final View itemView) {
            super(itemView);
            txt_headline = itemView.findViewById(R.id.txt_headline);
            txt_summary = itemView.findViewById(R.id.txt_summary);
            txt_date = itemView.findViewById(R.id.txt_date);
            imageView = itemView.findViewById(R.id.model_img);
            linearLayout = itemView.findViewById(R.id.anim_container);
            toggleButton=itemView.findViewById(R.id.btn_add);

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

        }
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
