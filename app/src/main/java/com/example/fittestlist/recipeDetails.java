package com.example.fittestlist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class recipeDetails extends AppCompatActivity {

    Button substitutions, analysis;
    TextView name, ingredients, instructions;
    String ingredientList;
    ImageView image;
    ArrayList<String> analysisIng = new ArrayList<>();
    public static final int CODE_START = 1;
    public static final String INTENTCODE_EDITTEXT =  "KEY1";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_START && resultCode == RESULT_OK)
        {
            //text.setText(data.getStringExtra(INTENTCODE_EDITTEXT));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        substitutions = findViewById(R.id.id_button_finish);
        analysis = findViewById(R.id.id_analysis);
        name = findViewById(R.id.id_recipe_name);
        ingredients = findViewById(R.id.id_ingredients);
        image = findViewById(R.id.id_image2);

        //sentValue.setText(getIntent().getStringExtra("TEST"));
        name.setText(getIntent().getStringExtra("RECIPE_NAME"));
        Log.d("TAG_", "recipeName in recipedetails: "+getIntent().getStringExtra("RECIPE_NAME"));

        ingredientList = "";
        for(int i = 1; i < getIntent().getStringArrayListExtra("RECIPE_LIST").size(); i++)
        {
            if (getIntent().getStringArrayListExtra("RECIPE_LIST").get(i) != null)
                ingredientList += getIntent().getStringArrayListExtra("RECIPE_LIST").get(i)+"\n";
            analysisIng.add(getIntent().getStringArrayListExtra("RECIPE_LIST").get(i));
            Log.d("TAG_", "ingredient list in recipeDetails: "+ingredientList);
        }
        ingredients.setText(ingredientList);

        Picasso.get().load(getIntent().getStringExtra("RECIPE_IMAGE")).into(image);

        substitutions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent sendback = new Intent();
                setResult(RESULT_OK, sendback);
                finish();*/

                Intent startIntent = new Intent(recipeDetails.this, Substitutions.class );
                startIntent.putExtra("RECIPE_ING", ingredientList);
                startActivityForResult(startIntent, CODE_START);
            }
        });

        analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent startIntent = new Intent(recipeDetails.this, nutritionalAnalysis.class );
                startIntent.putExtra("RECIPE_ING", analysisIng);
                startActivityForResult(startIntent, CODE_START);
            }
        });

    }
}
