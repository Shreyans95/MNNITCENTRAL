package com.example.android.mnnitcentral;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class Locality_Fragment extends Fragment {


    private DatabaseReference ref;

    private RecyclerView list;
    public Locality_Fragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView =inflater.inflate(R.layout.item_list,container,false);
        list = rootView.findViewById(R.id.lists);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        ref = FirebaseDatabase.getInstance().getReference().child("locality");
        ref.keepSynced(true);
        return rootView;
    }


    public void onStart()
    {
        super.onStart();
        FirebaseRecyclerAdapter<Users,UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(
                Users.class,
                R.layout.word,
                UserViewHolder.class,
                ref
        ) {
            @Override
            protected void populateViewHolder(final UserViewHolder viewHolder, Users m, final int position) {
                final Users model = m;
                viewHolder.setName(model.getName());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(model.getImage(),getContext());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(getActivity(),"name is "+model.getName(),Toast.LENGTH_SHORT).show();
                        Intent intent;
                        if(model.getName().equalsIgnoreCase("mnnit campus"))
                        {
                            intent = new Intent(getActivity(),List_of_Places.class);
                        }
                        else
                        {
                            intent = new Intent(getActivity(),MapsActivity.class);

                        }
                        intent.putExtra("class","locality/");
                        intent.putExtra("title",model.getName());
                        startActivity(intent);
                    }
                });
                Log.v("position",""+position);
            }
        };
        list.setAdapter(firebaseRecyclerAdapter);
    }


}
