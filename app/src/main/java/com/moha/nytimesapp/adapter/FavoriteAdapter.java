package com.moha.nytimesapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moha.nytimesapp.modelDB.Favorite;
import com.moha.nytimesapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private Context context;
    private List<Favorite> favoriteList;
    private OnItemClickListener listener;

    public FavoriteAdapter(Context context, List<Favorite> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fav_item_layout, viewGroup, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder favoriteViewHolder, int position) {
        Favorite favorite = favoriteList.get(position);
        favoriteViewHolder.txt_headline.setText(favorite.getHeadline());
        favoriteViewHolder.txt_summary.setText(favorite.getSummary());
        favoriteViewHolder.txt_date.setText(favorite.getPublishDate());
        Picasso.get().load(favorite.getImgUrl()).into(favoriteViewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView txt_headline, txt_summary, txt_date;
        public LinearLayout view_foreground;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.fav_img);
            txt_headline = itemView.findViewById(R.id.fav_headline);
            txt_summary = itemView.findViewById(R.id.fav_summary);
            txt_date = itemView.findViewById(R.id.fav_date);
            view_foreground = itemView.findViewById(R.id.view_foreground);

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
        }
    }

    public void removeItem(int position) {
        favoriteList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Favorite favorite,int position){
        favoriteList.add(position,favorite);
        notifyItemInserted(position);
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
