package com.example.fittestlist;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import java.util.ArrayList;
import java.util.List;

public class Substitutions extends AppCompatActivity
{
    TextView ingredientText, resultText, subText;
    String ingredient = "";
    ListView listView;
    ArrayList<String> arrayList;
    ArrayList<String> optionArray;
    ListViewAdapter adapter;
    String info = "";
    String message;
    JSONObject jsonObject;
    DownloadFilesTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_substitutions);

        listView = findViewById(R.id.id_listview);
        ingredientText = findViewById(R.id.id_sent_text);
        subText = findViewById(R.id.id_sub);
        ingredientText.setText(getIntent().getStringExtra("RECIPE_ING"));

        arrayList = new ArrayList<String>();
        adapter = new ListViewAdapter(Substitutions.this, R.layout.listview_main, arrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                listView.setSelected(true);
                PopupMenu popupMenu = new PopupMenu(Substitutions.this, view );
                popupMenu.getMenuInflater().inflate(R.menu.menu2, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.item_update:
                                AlertDialog.Builder builder = new AlertDialog.Builder(Substitutions.this);
                                View v = LayoutInflater.from(Substitutions.this).inflate(R.layout.listview_main, null, false);
                                builder.setTitle("Update Item");
                                final EditText editText = v.findViewById(R.id.id_list_edit);
                                editText.setText(arrayList.get(position));

                                //set custome view to dialog
                                builder.setView(v);

                                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!editText.getText().toString().isEmpty()) {
                                            arrayList.set(position, editText.getText().toString().trim());
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(Substitutions.this, "Item Updated!", Toast.LENGTH_SHORT).show();

                                        } else {
                                            editText.setError("add item here !");
                                        }
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                builder.show();

                                break;

                            case R.id.item_delete:
                                Toast.makeText(Substitutions.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                                arrayList.remove(position);
                                adapter.notifyDataSetChanged();
                                subText.setText(" ");
                                break;
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.add_item:
                addItem();
                break;
        }
        return true;
    }

    private void addItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Substitutions.this);
        builder.setTitle("Add New Item");
        View v = LayoutInflater.from(Substitutions.this).inflate(R.layout.listview_main, null, false);
        builder.setView(v);
        final EditText editText = v.findViewById(R.id.id_list_edit);

        builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!editText.getText().toString().isEmpty()) {
                    arrayList.add(editText.getText().toString().trim());
                    adapter.notifyDataSetChanged();
                    ingredient = editText.getText().toString();

                    if (task == null) {
                        task = new DownloadFilesTask();
                        task.execute();
                    }
                    DownloadFilesTask task = new DownloadFilesTask();
                    task.execute();
                    task.onPostExecute(info);

                    Log.d("TAG_", "ingredient editText: "+ingredient);
                }
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    private class DownloadFilesTask extends AsyncTask <String, Void, String> {


        @Override
        protected String doInBackground(String... Voids) {

            URL myUrl = null;
            try {
                myUrl = new URL("https://api.spoonacular.com/food/ingredients/substitutes?apiKey=e2219c41da5f4cd38097cfd2d6a83dfa&ingredientName="+ingredient);
                Log.d("TAG_", "this is myurl: " + myUrl);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if (myUrl != null) {
                URLConnection myURLConnection = null;
                try {
                    myURLConnection = (URLConnection) myUrl.openConnection();
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
                    Log.d("TAG_", "catch: "+e);

                }

                BufferedReader br = null;
                br = new BufferedReader(new InputStreamReader(myInputStream));

                try {
                    info = br.readLine();
                    br.close();
                } catch (IOException e) {
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

        protected void onPostExecute (String result) {
            super.onPostExecute(result);
            try {
                jsonObject = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jsonObject != null)
            {
                JSONArray optionList = null;

                try {

                    String options = "";
                    String finalSub = "";

                    optionArray = new ArrayList <String> ();
                    Log.d("TAG_", "status: "+jsonObject.getString("status"));
                    if (jsonObject.getString("status").contains("success"))
                    {
                        message = jsonObject.getString("message");
                        optionList = jsonObject.getJSONArray("substitutes");
                        for (int i = 0; i < optionList.length(); i++)
                        {
                            options += optionList.getString(i)+"\n";
                        }
                        subText.setText(options);
                        //optionArray.add(options);
                        //Toast myMessage = Toast.makeText(Substitutions.this, message,Toast.LENGTH_SHORT );
                        //myMessage.show();
                        Log.d("TAG_", "options: "+options);
                    }
                    else
                    {
                        message = jsonObject.getString("message");
                        //Toast myMessage2 = Toast.makeText(Substitutions.this, message,Toast.LENGTH_SHORT );
                        //myMessage2.show();
                        subText.setText(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public class ListViewAdapter extends ArrayAdapter<String>
    {
        Context mainContext;
        int xml;
        List<String> list;

        public ListViewAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            mainContext = context;
            xml = resource;
            list = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)mainContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View adapterLayout = inflater.inflate(xml, null);

            TextView inputText = adapterLayout.findViewById(R.id.id_list_text);
            ingredient = list.get(position);
            inputText.setText(ingredient);

            EditText editText = adapterLayout.findViewById(R.id.id_list_edit);
            editText.setVisibility(View.INVISIBLE);

            return adapterLayout;
        }
    }
}
