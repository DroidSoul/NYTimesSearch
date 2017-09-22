package com.droidsoul.nytimessearch.adapters;

import android.content.Context;
import android.graphics.Movie;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.droidsoul.nytimessearch.R;
import com.droidsoul.nytimessearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by bear&bear on 9/9/2017.
 */
public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    private static class ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
//        TextView tvOverview;
    }
    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article, parent, false);
            viewHolder.ivImage = convertView.findViewById(R.id.ivImage);
            viewHolder.tvTitle = convertView.findViewById(R.id.tvTitle);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivImage.setImageResource(0);
        viewHolder.tvTitle.setText(article.getHeadline());
        String thumbnail = article.getThumbNail();
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).placeholder(R.mipmap.ic_launcher).transform(new RoundedCornersTransformation(10, 10)).into(viewHolder.ivImage);
        }

        return convertView;
    }
}
