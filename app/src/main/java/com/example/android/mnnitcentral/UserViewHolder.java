package com.example.android.mnnitcentral;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by LENOVO on 3/24/2018.
 */

public  class UserViewHolder extends RecyclerView.ViewHolder{
    View mView;
    public UserViewHolder(View item)
    {
        super(item);
        mView = item;

    }
    public void setName(String name)
    {
        TextView text = mView.findViewById(R.id.text);
        text.setText(name);
    }
    public void setDesc(String desc)
    {
        TextView text = mView.findViewById(R.id.editText2);
        text.setText(desc);
    }
    public void setImage(String i, Context c)
    {
        final String image =i;
        final Context context = c;
        final ImageView image_upload = mView.findViewById(R.id.image);
        if(image!=null) {

            try {
                Picasso.with(context).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.avatar_default).into(image_upload, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context).load(image).placeholder(R.drawable.avatar_default).into(image_upload);
                    }
                });
            } catch (Exception e) {
                Log.v("message","this is executed");
                image_upload.setImageResource(R.drawable.avatar_default);
            }
        }
    }
}
