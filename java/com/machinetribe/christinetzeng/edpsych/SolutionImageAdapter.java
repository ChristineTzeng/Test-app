package com.machinetribe.christinetzeng.edpsych;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Christine
 * 1. Customize adapter and display SolutionGridView with assigned images
 * 2. Rotate imageView with recorded angles to reduce image storage
 */

public class SolutionImageAdapter extends BaseAdapter {
    private Context context;
    private int[] imageInfo;
    private int[] imageAngle;
    private final LayoutInflater inflater;

    public SolutionImageAdapter(Context context, int[] imageInfo, int[] angles) {
        this.context = context;
        this.imageInfo = imageInfo;
        this.imageAngle = angles;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        ImageView imageView;
//        TextView textView;

        if (v == null) {
            v = inflater.inflate(R.layout.solution_grid, parent, false);
            v.setTag(R.id.solutionImageView, v.findViewById(R.id.solutionImageView));
//            v.setTag(R.id.optionTextView, v.findViewById(R.id.optionTextView));
        }

        imageView = (ImageView) v.getTag(R.id.solutionImageView);
//        textView = (TextView) v.getTag(R.id.optionTextView);

        imageView.setRotation(0);
        imageView.setRotation(imageView.getRotation() + imageAngle[position]);

        imageView.setImageResource(imageInfo[position]);

//        textView.setText("" + (position + 1));
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
        return position;
    }

    protected void updateImages(int[] images, int[] angles)
    {
        imageInfo = images;
        imageAngle = angles;
        notifyDataSetChanged();
    }
}
