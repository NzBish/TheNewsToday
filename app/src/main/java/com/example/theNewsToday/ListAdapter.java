package com.example.theNewsToday;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ListAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    ListAdapter(Activity mActivity, ArrayList<HashMap<String, String>> mData) {
        activity = mActivity;
        data=mData;
    }
    public int getCount() {
        return data.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.list_item , parent, false);
            vh.galleryImage = convertView.findViewById(R.id.galleryImage);
            vh.author = convertView.findViewById(R.id.author);
            vh.title = convertView.findViewById(R.id.title);
            vh.sdetails = convertView.findViewById(R.id.sdetails);
            vh.time = convertView.findViewById(R.id.time);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.galleryImage.setId(position);
        vh.author.setId(position);
        vh.title.setId(position);
        vh.sdetails.setId(position);
        vh.time.setId(position);

        HashMap<String, String> article = data.get(position);
        try{
            vh.author.setText(article.get(NewsActivity.KEY_AUTHOR));
            vh.title.setText(article.get(NewsActivity.KEY_TITLE));
            vh.time.setText(article.get(NewsActivity.KEY_PUBLISHEDAT));
            vh.sdetails.setText(article.get(NewsActivity.KEY_DESCRIPTION));

            if (Objects.requireNonNull(article.get(NewsActivity.KEY_URLTOIMAGE)).length() != 0) {
                Picasso.get()
                        .load(article.get(NewsActivity.KEY_URLTOIMAGE))
                        .resize(300, 200)
                        .centerCrop()
                        .into(vh.galleryImage);
            } else {
                vh.galleryImage.setVisibility(View.GONE);
            }
        }catch(Exception e) {
            Log.e("LoadThumbnail", String.valueOf(e));
        }
        return convertView;
    }
}

class ViewHolder {
    ImageView galleryImage;
    TextView author, title, sdetails, time;
}