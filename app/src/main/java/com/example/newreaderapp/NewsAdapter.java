package com.example.newreaderapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(@NonNull Context context, int resource, @NonNull List<News> objects) {
        super(context, resource, objects);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.dong,null);
        }
        News news = getItem(position);
        if (news!= null){
            TextView txtTitle = (TextView) view.findViewById(R.id.textviewTitle);
            ImageView  imgHinhAnh = (ImageView) view.findViewById(R.id.imageViewHinhAnh);

            txtTitle.setText(news.getTitle());
            Picasso.with(getContext()).load(news.getImage()).into(imgHinhAnh);
        }
        return view;
    }
}
