package com.example.fittestlist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter2 extends RecyclerView.Adapter <RecyclerViewAdapter2.RecyclerViewHolder >
{
    Context parentContext;
    ArrayList<healthyRecipes.Recipes2> list;
    public static List<String> recipeList;
    private RecyclerViewAdapter2.RecyclerListener mRecyclerListener;

    public RecyclerViewAdapter2 (Context context, ArrayList<healthyRecipes.Recipes2> list, RecyclerViewAdapter2.RecyclerListener recyclerListener)
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
        View view = LayoutInflater.from(parentContext).inflate(R.layout.recycler_main2,parent, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(view, mRecyclerListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getName());
        holder.textView2.setText(list.get(position).getSummary());
        //holder.imageView.setImageBitmap(list.get(position).getImage());
        Picasso.get().load(list.get(position).getImage()).into(holder.imageView);

        //holder.textView.setText(list.get(position));
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
        ImageView imageView;
        RecyclerListener recyclerListener;

        public RecyclerViewHolder(@NonNull View itemView, RecyclerListener recyclerListener ) {
            super(itemView);
            //this is where we will do all of the findViewByIds
            //and declare variables
            textView = itemView.findViewById(R.id.id_holder_text);
            textView2 = itemView.findViewById(R.id.id_summary);
            imageView = itemView.findViewById(R.id.id_image);
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
