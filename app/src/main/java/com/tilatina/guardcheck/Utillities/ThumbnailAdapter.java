package com.tilatina.guardcheck.Utillities;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.tilatina.guardcheck.R;

import java.util.ArrayList;

/**
 * Created by jaime on 30/04/16.
 */
public class ThumbnailAdapter extends BaseAdapter implements ListAdapter {

    private Context context;
    private ArrayList<NoveltyThumbnail> noveltyThumbnails;

    public ThumbnailAdapter(){}

    public ThumbnailAdapter(Context context, ArrayList<NoveltyThumbnail> noveltyThumbnails) {
        this.context = context;
        this.noveltyThumbnails = noveltyThumbnails;
    }


    @Override
    public int getCount() {
        return noveltyThumbnails.size();
    }

    @Override
    public Object getItem(int position) {
        return noveltyThumbnails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (null == row) {

            LayoutInflater layoutInflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.novelty_row, null);
            Button deleteItem = (Button) row.findViewById(R.id.deleteThumbnail);
            ImageView thumbnailView = (ImageView) row.findViewById(R.id.thumbnailView);
            final ImageButton takePicture = (ImageButton) ((Activity)context).findViewById(R.id.takePicture);

            thumbnailView.setImageBitmap(noveltyThumbnails.get(position).getThumbnail());

            deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noveltyThumbnails.remove(position);
                    notifyDataSetChanged();
                    takePicture.setEnabled(true);
                }
            });
        }

        return row;
    }
}
