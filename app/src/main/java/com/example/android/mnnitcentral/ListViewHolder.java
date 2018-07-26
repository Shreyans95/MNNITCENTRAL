package com.example.android.mnnitcentral;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by LENOVO on 4/21/2018.
 */

public class ListViewHolder extends RecyclerView.ViewHolder {
    View mView;
    public ListViewHolder(View item)
    {
        super(item);
        mView = item;
    }
    public void setDetails(String details)
    {
        TextView text = mView.findViewById(R.id.academic_details);
        text.setText(details);
    }

    //Attempt part
    public void setName(String name)
    {
        TextView text = mView.findViewById(R.id.list);
        text.setText(name);
    }



}
