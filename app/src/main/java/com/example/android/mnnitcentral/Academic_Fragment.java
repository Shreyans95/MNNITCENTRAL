package com.example.android.mnnitcentral;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class Academic_Fragment extends Fragment {
    private DatabaseReference ref;

    private RecyclerView list;
    public Academic_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final String path = "academic";
        View rootView =inflater.inflate(R.layout.item_list,container,false);
        list = rootView.findViewById(R.id.lists);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        ref = FirebaseDatabase.getInstance().getReference().child("academic");
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
            protected void populateViewHolder(UserViewHolder viewHolder, Users m, final int position) {
                final Users model = m;
               viewHolder.setName(model.getName());
               viewHolder.setDesc(model.getDesc());
               viewHolder.setImage(model.getImage(),getContext());
               viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
//                       Toast.makeText(getActivity(),"name is "+model.getName(),Toast.LENGTH_SHORT).show();
                        String name = model.getName();
                        Intent intent;
                        if((name.equalsIgnoreCase("TLB")))
                            intent = new Intent(getActivity(),TLB.class);
                        else
                        if((name.equalsIgnoreCase("Registeration")))
                            intent = new Intent(getActivity(),Registration.class);
                        else
                        if((name.equalsIgnoreCase("Time Table")))
                            intent = new Intent(getActivity(),TimeTable.class);
                        else
                        if((name.equalsIgnoreCase("Scholarship")))
                            intent = new Intent(getActivity(),Scholarship.class);
                        else
                            intent = new Intent(getActivity(),Computer_Club.class);
                        intent.putExtra("class","academic/"+name.toLowerCase());
                        intent.putExtra("title",name);
                        startActivity(intent);
                   }
               });
            }
        };
        list.setAdapter(firebaseRecyclerAdapter);
    }
}
