package com.example.android.mnnitcentral;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by LENOVO on 3/26/2018.
 */

public class ViewPagerAdapter extends PagerAdapter {
    Activity activity;
    String[] images;
    LayoutInflater inflater;

    public ViewPagerAdapter(Activity activity, String[] images) {
        this.activity = activity;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater =(LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item,container,false);
        ImageView image = itemView.findViewById(R.id.image);
        DisplayMetrics dis = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);
        image.setMinimumHeight(dis.heightPixels);
        image.setMinimumWidth(dis.widthPixels);

        try{
            Log.v("message","hi" + position);
            Picasso.with(activity.getApplicationContext()).load(images[position]).networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.avatar_default)
                    .into(image);

            Log.v("try","hi");
        }catch (Exception ex)
        {
            Log.v("exception","hi");
            Picasso.with(activity.getApplicationContext()).load(R.drawable.avatar_default).into(image);
        }
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
