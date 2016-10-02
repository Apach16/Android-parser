package com.example.apach.parser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by apach on 01.10.16.
 */

public class JSONDataAdapter extends ArrayAdapter<JSONData> {
    ArrayList<JSONData> DataList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public JSONDataAdapter(Context context, int resource, ArrayList<JSONData> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        DataList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);

            holder.title = (TextView) v.findViewById(R.id.title);
            holder.image = (ImageView) v.findViewById(R.id.image);
            holder.desc = (TextView) v.findViewById(R.id.desc);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        //holder.image.setImageResource(R.drawable.ic_launcher);
        new DownloadImageTask(holder.image).execute(DataList.get(position).getUrl());
        holder.title.setText(DataList.get(position).getTitle());
        holder.desc.setText(DataList.get(position).getDesc());
        if (DataList.get(position).getType().equals("person")){
            v.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGray));
            holder.desc.setTextColor(Color.WHITE);
        }
        if (DataList.get(position).getType().equals("news")){
            v.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
            holder.desc.setTextColor(Color.BLACK);
        }
        return v;

    }

    static class ViewHolder {
        public TextView title;
        public ImageView image;
        public TextView desc;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
                try {
                        InputStream in = new java.net.URL(urldisplay).openStream();
                        mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }
}
