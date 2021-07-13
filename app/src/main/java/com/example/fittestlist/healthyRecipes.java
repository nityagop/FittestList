package com.example.fittestlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class healthyRecipes extends Fragment implements RecyclerViewAdapter2.RecyclerListener
{
    JSONObject jsonObject, recipeNumber;;
    String info = "";
    RecyclerView recyclerView;
    Context thisContext;
    String recipeName,  recipeSummary, recipeImage, recipeHealth;
    public static final int CODE_START = 1;
    ArrayList <healthyRecipes.Recipes2> recipeList = new ArrayList<Recipes2>();
    ArrayList <String> ingredients;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.healthy_fragment, container, false);
        thisContext = container.getContext();
        DownloadFilesTask task = new DownloadFilesTask();
        task.execute();
        task.onPostExecute(info);

        recyclerView = v.findViewById(R.id.id_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(thisContext, LinearLayoutManager.VERTICAL, false));

        ArrayList<String> list = new ArrayList<>();

        return v;
    }

    @Override
    public void onRecyclerClick(int position) {
        Intent startIntent = new Intent(thisContext, recipeDetails.class );
        startIntent.putExtra("RECIPE_NAME", recipeList.get(position).getName());
        startIntent.putExtra("RECIPE_LIST", recipeList.get(position).getIngredients());
        startIntent.putExtra("RECIPE_IMAGE", recipeList.get(position).getImage());
        startActivityForResult(startIntent, CODE_START);
    }

    public class Recipes2 {
        private String name;
        private String image;
        private String summary;
        private ArrayList ingredients;

        public Recipes2(String n, String i, String s, ArrayList ing) {
            name = n;
            image = i;
            summary = s;
            ingredients = ing;
        }

        public String getName() {
            return name;
        }

        public String getImage() {
            return image;
        }

        public String getSummary() {
            return summary;
        }

        public ArrayList getIngredients() {
            return ingredients;
        }
    }
    private class DownloadFilesTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... Voids) {

            URL myUrl = null;
            try {
                myUrl = new URL("https://api.spoonacular.com/recipes/random?number=30&instructionsRequired=true&apiKey=e2219c41da5f4cd38097cfd2d6a83dfa");
                Log.d("TAG_", "this is myurl: " + myUrl);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if (myUrl != null) {
                URLConnection myURLConnection = null;
                try {
                    myURLConnection = myUrl.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                InputStream myInputStream = new InputStream() {
                    @Override
                    public int read() throws IOException {
                        return 0;
                    }
                };

                try {
                    myInputStream = myURLConnection.getInputStream();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(myInputStream));

                try {
                    info = br.readLine();
                    Log.d("TAG_", "info string: " + info);
                } catch (IOException e) {
                    Log.d("TAG_", "catch of br.readline: " + e);
                    e.printStackTrace();
                }

                try {
                    jsonObject = new JSONObject(info);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return info;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                jsonObject = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jsonObject != null) {
                JSONArray jsonList = null;
                Log.d("TAG_", "in onPostExecute");

                try {
                    jsonList = jsonObject.getJSONArray("recipes");
                    Log.d("TAG_", "recipe name: "+recipeName);

                    for(int i = 0; i < 20; i++)
                    {
                        ingredients = new ArrayList <String>();
                        recipeNumber = jsonList.getJSONObject(i);
                        recipeName = recipeNumber.getString("title");
                        recipeSummary = "Ready in: "+recipeNumber.getString("readyInMinutes")+" minutes";

                        for (int j = 0; j < jsonList.length(); j++) {

                            if (recipeNumber.has("image")) {
                                recipeImage = recipeNumber.getString("image");
                            }

                            else {
                                recipeImage = "https://eatnstreet.com/images/NoImageAvailable.png";
                            }
                        }
                        recipeHealth = recipeNumber.getString("veryHealthy");

                        for(int x = 0; x < recipeNumber.getJSONArray("extendedIngredients").length(); x++)
                        {
                            ingredients.add(recipeNumber.getJSONArray("extendedIngredients").getJSONObject(x).getString("nameClean"));
                        }

                        if (recipeHealth.contains("true"))
                            recipeList.add(new Recipes2(recipeName, recipeImage, recipeSummary, ingredients));
                    }

                    RecyclerViewAdapter2 adapter = new RecyclerViewAdapter2(thisContext, recipeList, healthyRecipes.this::onRecyclerClick);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
