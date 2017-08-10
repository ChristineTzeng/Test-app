package com.machinetribe.christinetzeng.edpsych;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Christine
 * 1. Customize adapter and display promptGridView with assigned images
 * 2. Rotate imageView with recorded angles to reduce image storage
 */

public class PromptImageAdapter extends BaseAdapter {
    private Context context;
    private int[] imageInfo;
    private int[] imageAngle;
    private final LayoutInflater inflater;

    public PromptImageAdapter(Context context, int[] imageInfo, int[] angles) {
        this.context = context;
        this.imageInfo = imageInfo;
        this.imageAngle = angles;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        ImageView imageView;

        if (v == null) {
            v = inflater.inflate(R.layout.prompt_grid, parent, false);
            v.setTag(R.id.promptImageView, v.findViewById(R.id.promptImageView));
        }

        imageView = (ImageView) v.getTag(R.id.promptImageView);

        imageView.setRotation(0);
        imageView.setRotation(imageView.getRotation() + imageAngle[position]);

        imageView.setImageResource(imageInfo[position]);

        return v;
    }

    @Override
    public int getCount() {
        return imageInfo.length;
    }

    @Override
    public Object getItem(int position) {
        return imageInfo[position];
    }

    @Override
    public long getItemId(int position) {
        return imageInfo[position];
    }

    public void updateImage(int image, int angle)
    {
        imageInfo[8] = image;
        imageAngle[8] = angle;
        notifyDataSetChanged();
    }

    public void updateImages(int[] images, int[] angles)
    {
        imageInfo = images;
        imageAngle = angles;
        notifyDataSetChanged();
    }

    public int getSelectedImage()
    {
        return imageInfo[8];
    }
}
