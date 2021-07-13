package com.example.fittestlist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class nutritionalAnalysis extends AppCompatActivity
{
    public static final int CODE_START = 1;
    TextView recipeIng, cal_text, fat_text, carb_text, sugar_text, fiber_text, protein_text;
    String info = "";
    Button results;
    String input, calories, weight;
    JSONObject jsonObject;
    EditText ingredientInput;
    DownloadFilesTask task = new DownloadFilesTask();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        ingredientInput = findViewById(R.id.id_ing_input);
        results = findViewById(R.id.id_getResults);
        cal_text = findViewById(R.id.id_calories);
        fat_text = findViewById(R.id.id_fats);
        carb_text = findViewById(R.id.id_carbs);
        sugar_text = findViewById(R.id.id_sugar);
        fiber_text = findViewById(R.id.id_fiber);
        protein_text = findViewById(R.id.id_protein);

        //recipeIng.setText(getIntent().getStringExtra("RECIPE_ING"));

        ingredientInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (task == null){
                    task = new DownloadFilesTask();
                    task.execute();
                }
                task.cancel(true);
                DownloadFilesTask task2 = new DownloadFilesTask();
                task2.execute();
            }
        });

    }

    private class DownloadFilesTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... Voids) {

            URL myUrl = null;
            try {
                myUrl = new URL("https://api.edamam.com/api/nutrition-data?app_id=0ade7514&app_key=4d713b1957b8dbe555351393e957f619&ingr=1%20"+input);
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
                    JSONObject nutrients = jsonObject.getJSONObject("totalNutrients");

                    calories = jsonObject.getString("calories");

                    JSONObject fats = nutrients.getJSONObject("FAT");
                    String fatAmount = fats.getString("quantity");

                    JSONObject carbs = nutrients.getJSONObject("CHOCDF");
                    String carbAmount = carbs.getString("quantity");

                    JSONObject fiber = nutrients.getJSONObject("FIBTG");
                    String fiberAmount = fiber.getString("quantity");

                    JSONObject sugars = nutrients.getJSONObject("SUGAR");
                    String sugarAmount = sugars.getString("quantity");

                    JSONObject proteins = nutrients.getJSONObject("PROCNT");
                    String proteinAmount = proteins.getString("quantity");

                    cal_text.setText("Calories: "+calories+" calories");
                    fat_text.setText("Fats: "+fatAmount+" grams");
                    carb_text.setText("Carbs: "+carbAmount+" grams");
                    fiber_text.setText("Fiber: "+fiberAmount+" grams");
                    sugar_text.setText("Sugars: "+sugarAmount+" grams");
                    protein_text.setText("Protein: "+proteinAmount+" grams");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            }
        }
}

