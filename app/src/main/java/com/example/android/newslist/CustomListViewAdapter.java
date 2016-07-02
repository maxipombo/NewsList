package com.example.android.newslist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by mpombos on 2/7/16.
 */

public class CustomListViewAdapter extends BaseAdapter {

    private static final String URL_FIXED = "http://content.guardianapis.com/search?show-fields=thumbnail&q=harry%20potter&api-key=test";
    JSONObject details = null;
    ArrayList<Article> detailedData = new ArrayList<>();
    Context context;
    private String result;

    public CustomListViewAdapter(Context context) {
        this.context = context;
        getData();
    }

    @Override
    public int getCount() {
        return detailedData.size();
    }

    @Override
    public Object getItem(int position) {
        return detailedData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.row_layout, parent, false);
        }

        TextView title = (TextView) row.findViewById(R.id.titleTV);
        TextView webPublicationDate = (TextView) row.findViewById(R.id.webPublicationDateTV);


        final Article temp_obj = detailedData.get(position);
        title.setText(temp_obj.getWebTitle());
        webPublicationDate.setText(temp_obj.getWebPublicationDate());
        title.setOnClickListener(new View.OnClickListener()

                                 {
                                     @Override
                                     public void onClick(View v) {
                                         Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(temp_obj.getThumbnail()));
                                         Intent browserChooserIntent = Intent.createChooser(browserIntent, "Choose browser of your choice");
                                         context.startActivity(browserChooserIntent);
                                     }
                                 }

        );
        DownloadImgTask imgData = new DownloadImgTask();
        try

        {
            Bitmap result = imgData.execute(temp_obj.getThumbnail()).get();
            ImageView view = (ImageView) row.findViewById(R.id.imageView);
            view.setImageBitmap(result);
        } catch (
                InterruptedException e
                )

        {
            e.printStackTrace();
        } catch (
                ExecutionException e
                )

        {
            e.printStackTrace();
        }


        return row;
    }

    public void getData() {
        try {
            DownloadFeedTask downloadData = new DownloadFeedTask();
            result = downloadData.execute(URL_FIXED).get();
            if (result != null) {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    details = jsonObj.getJSONObject("response");
                    if (details.getString("status").equals("ok")) {
                        JSONArray results = details.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject c = results.getJSONObject(i);
                            String webTitle = c.getString("webTitle");
                            String webPublicationDate = c.getString("webPublicationDate");
                            String webUrl = c.getString("webUrl");
                            JSONObject fieldObj = c.getJSONObject("fields");
                            String thumbnail = fieldObj.getString("thumbnail");
                            detailedData.add(new Article(webTitle, webPublicationDate, webUrl, thumbnail));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        }

    }
}
