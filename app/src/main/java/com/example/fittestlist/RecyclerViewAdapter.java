package com.example.fittestlist;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.RecyclerViewHolder>
{
    Context parentContext;
    private ArrayList<normalRecipes.Recipes> list = new ArrayList<normalRecipes.Recipes>() ;
    public static List <String> recipeList;
    private RecyclerListener mRecyclerListener;

    public RecyclerViewAdapter (Context context, ArrayList<normalRecipes.Recipes> list, RecyclerListener recyclerListener)
    {
        parentContext = context;
        this.list = list;
        recipeList = new ArrayList<>();
        this.mRecyclerListener = recyclerListener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate xml here
        //and return ViewHolder
        View view = LayoutInflater.from(parentContext).inflate(R.layout.recycler_main,parent, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(view, mRecyclerListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getName());
        holder.textView2.setText(list.get(position).getSummary());
        Picasso.get().load(list.get(position).getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        else
            return  list.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        Button button;
        TextView textView, textView2;
        ImageView image;
        RecyclerListener recyclerListener;

        public RecyclerViewHolder(@NonNull View itemView, RecyclerListener recyclerListener) {
            super(itemView);
            //this is where we will do all of the findViewByIds
            //and declare variables
            textView = itemView.findViewById(R.id.id_holder_text);
            textView2 = itemView.findViewById(R.id.id_summary);
            image = itemView.findViewById(R.id.id_image);
            this.recyclerListener = recyclerListener;

            itemView.setOnClickListener((View.OnClickListener) this);

        }

        @Override
        public void onClick(View v) {
            recyclerListener.onRecyclerClick(getAdapterPosition());
        }
    }

    public interface RecyclerListener
    {
        public void onRecyclerClick (int position);
    }
}
