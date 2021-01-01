package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class searchRecipes extends AppCompatActivity implements View.OnClickListener{

    public static Client c;
    private EditText SearchFood;
    private EditText SearchByIngredients;
    private EditText SearchFoodVideo;
    private Button LoginButton;
    public static Boolean Login = false;
    private Runnable connectToServerThread = new Runnable() {
        @Override
        public void run() {
            c = new Client("10.0.2.2", 5678);
        }
    };
    private Runnable sendRecipeMessageThread = new Runnable() {
        @Override
        public void run() {
            c.SendMessage(c,"searchRecipe", SearchFood.getText().toString().trim());
        }
    };
    private Runnable searchIngredientsThread = new Runnable() {
        @Override
        public void run() {
            c.SendMessage(c, "searchByIngredients", SearchByIngredients.getText().toString().trim());
        }
    };
    private Runnable searchVideoThread = new Runnable() {
        @Override
        public void run() {
            c.SendMessage(c, "searchVideo", SearchFoodVideo.getText().toString().trim());
        }
    };


    @SuppressLint({"WrongViewCast", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipes);
        ((Button) findViewById(R.id.searchNutrientsButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.searchVieoButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.searchIngredientsButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.searchRecipeButton)).setOnClickListener(this);
        SearchFood = (EditText) findViewById(R.id.searchFood);
        SearchByIngredients = (EditText) findViewById(R.id.searchByIngredients);
        SearchFoodVideo = (EditText) findViewById(R.id.searchVideo);
        Thread connect = new Thread(connectToServerThread);
        connect.start();
        LinearLayout relative = (LinearLayout) findViewById(R.id.loginLayout);
        if (!Login) {
            LoginButton = new Button(this);
            LoginButton.setId(25);                                                //25 = login id
            LoginButton.setOnClickListener(this);
            LoginButton.setText("登入");
            LoginButton.setTextColor(getResources().getColorStateList(R.color.white));
            LoginButton.setBackgroundTintList(getResources().getColorStateList(R.color.button_color));
            relative.addView(LoginButton);
        } else {

        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()) {
            case R.id.searchNutrientsButton:
                intent = new Intent(searchRecipes.this, searchRecipesByNutrients.class);
                startActivity(intent);
                break;
            case R.id.searchVieoButton:
                try {
                    Thread sendSearchVideo = new Thread (searchVideoThread);
                    sendSearchVideo.start();
                    SearchFoodVideo.setText(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent = new Intent(searchRecipes.this, searchFoodVideo.class);
                startActivity(intent);
                break;
            case R.id.searchIngredientsButton:
                try {
                    Thread sendSearchIngredients = new Thread (searchIngredientsThread);
                    sendSearchIngredients.start();
                    SearchByIngredients.setText(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent = new Intent(searchRecipes.this, searchRecipesByIngredients.class);
                startActivity(intent);
                break;
            case R.id.searchRecipeButton:
                try {
                    Thread sendSearchRecipe = new Thread(sendRecipeMessageThread);
                    sendSearchRecipe.start();
                    SearchFood.setText(null);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                intent = new Intent(searchRecipes.this, recipesResult.class);
                startActivity(intent);
                break;
            case 25:
                intent = new Intent(searchRecipes.this, Login.class);
                startActivity(intent);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}