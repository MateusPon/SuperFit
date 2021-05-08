package com.example.superfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.ToggleButton;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.xml.sax.Parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RecipesList extends AppCompatActivity {

    private ToggleButton balancedButton;
    private ToggleButton highFiberButton;
    private ToggleButton highProteinButton;

    private ListView rec;
    private RecipeAdapter adapter;
    private ArrayList<Recipe> recipes;

    private HttpURLConnection connection = null;
    private String data;

    SearchView searchView;
    String prodName = "chicken";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        balancedButton = findViewById(R.id.balanced_button);
        highFiberButton = findViewById(R.id.high_fiber_button);
        highProteinButton = findViewById(R.id.high_protein_button);
        rec = findViewById(R.id.recycle_View);
        recipes = new ArrayList<>();
        adapter = new RecipeAdapter(this, recipes);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    data = GetDownloadData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Parsing(data);
                            rec.setAdapter(adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        searchView = findViewById(R.id.search_bar);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                prodName = query;
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            data = GetDownloadData();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    Parsing(data);
                                    rec.setAdapter(adapter);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    private String GetDownloadData() {
        StringBuilder result = new StringBuilder();
        try {
            connection = (HttpURLConnection) new URL("https://api.edamam.com/search?q=chiken&app_id=4da5a427&app_key=6dd6f99730da1737e964379d886e607d&diet=high-protein").openConnection();
            // установка метода запроса
            connection.setRequestMethod("GET");
            // поключение
            connection.connect();
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                // считывание данных
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line; // строка для чтения
                // цикл чтения данных
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                    result.append("\n");
                }
            }
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
        // отключение
        if (connection != null) {
            connection.disconnect();
        }
        return result.toString();
    }

    private void Parsing(String data) {
        try {
            // парсинг json
            Object object = new JSONParser().parse(data);
            org.json.simple.JSONObject jsonObject = (JSONObject) object;
            // получение списка рецептов
            org.json.simple.JSONArray jsonArray = (org.json.simple.JSONArray) jsonObject.get("hits");

            for (Object obj : jsonArray) {
                org.json.simple.JSONObject item = (org.json.simple.JSONObject) obj;
                org.json.simple.JSONObject recipeObject = (org.json.simple.JSONObject) item.get("recipe");
                String label = recipeObject.get("label").toString();
                String image =recipeObject.get("image").toString();
                Log.d("LABEL", label);
                String calories = recipeObject.get("calories").toString();
                Log.d("CALORIES", calories);
                org.json.simple.JSONObject totalNutrients = (org.json.simple.JSONObject) recipeObject.get("totalNutrients");
                org.json.simple.JSONObject proteinsObject = (org.json.simple.JSONObject) totalNutrients.get("PROCNT");
                String proteinsValue = proteinsObject.get("quantity").toString();
                Log.d("PROTEINS", proteinsValue);
                org.json.simple.JSONObject fatsObject = (org.json.simple.JSONObject) totalNutrients.get("FAT");
                String fatsValue = fatsObject.get("quantity").toString();
                Log.d("FATS", fatsValue);
                org.json.simple.JSONObject carbsObject = (org.json.simple.JSONObject) totalNutrients.get("CHOCDF");
                String carbsValue = carbsObject.get("quantity").toString();
                Log.d("CARBS", carbsValue);



                Recipe recipe = new Recipe();
                recipe.setLabel(label);
                recipe.setCalories(String.valueOf( Math.round(Double.parseDouble(calories))));
                recipe.setProtein(String.valueOf(Math.round(Double.parseDouble(proteinsValue))));
                recipe.setCarbs(String.valueOf(Math.round(Double.parseDouble(carbsValue))));
                recipe.setFats(String.valueOf( Math.round(Double.parseDouble(fatsValue))));
                recipe.setImageFoot(image);

                recipes.add(recipe);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick_highProtein(View v) {

        highFiberButton.setChecked(false);
        balancedButton.setChecked(false);

    }

    public void onMainScreen(View view) {
        Intent intent = new Intent(RecipesList.this,mainScreen.class);
        startActivity(intent);
    }

    public void onClick_Balanced(View v) {

        highFiberButton.setChecked(false);
        highProteinButton.setChecked(false);

    }

    public void onClick_HighFiber(View v) {

        balancedButton.setChecked(false);
        highProteinButton.setChecked(false);

    }
}