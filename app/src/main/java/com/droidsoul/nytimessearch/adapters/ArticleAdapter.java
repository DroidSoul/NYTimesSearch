package com.droidsoul.nytimessearch.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.droidsoul.nytimessearch.R;
import com.droidsoul.nytimessearch.models.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bear&bear on 9/21/2017.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private List<Article> mArticles;
    private Context mContext;

    // Define listener member variable
    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // View lookup cache
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivImage;
        public TextView tvTitle;

        public ViewHolder(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ivImage = (ImageView)itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }

    public ArticleAdapter(Context context, ArrayList<Article> aArticles) {
        mArticles = aArticles;
        mContext = context;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View articleView = inflater.inflate(R.layout.item_article, parent, false);

        // Return a new holder instance
        ArticleAdapter.ViewHolder viewHolder = new ArticleAdapter.ViewHolder(articleView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = mArticles.get(position);

        // Populate data into the template view using the data object
        viewHolder.tvTitle.setText(article.getHeadline());
        String thumbnail = article.getThumbNail();
        if (!TextUtils.isEmpty(thumbnail)) {
            Glide.with(getContext())
                    .load(Uri.parse(thumbnail))
                    .placeholder(R.mipmap.ic_launcher)
                    .into(viewHolder.ivImage);
        }
        // Return the completed view to render on screen
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }




}
