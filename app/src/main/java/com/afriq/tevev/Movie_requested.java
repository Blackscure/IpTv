package com.afriq.tevev;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.chaos.view.PinView;
import com.google.android.material.textview.MaterialTextView;

import java.util.HashSet;
import java.util.Set;

public class Movie_requested extends AppCompatActivity {
    TextView about;
    TextView director;
    TextView otherStuff;
    ImageView favourites;
    ImageView movieBack;
    TextView playBtn;
    MaterialTextView channelName;
    TextView rating;
    TextView genre;
    TextView released;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_requested);
        favourites = findViewById(R.id.favlogo);
        movieBack = findViewById(R.id.movie_back);
        channelName = findViewById(R.id.channelName);
        about = findViewById(R.id.about);
        director = findViewById(R.id.director);
        otherStuff = findViewById(R.id.other_stuff);
        playBtn = findViewById(R.id.play);
        rating = findViewById(R.id.rating);
        genre = findViewById(R.id.genre);
        released = findViewById(R.id.released);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.primaryColor));
        }
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMovie();

            }
        });
        movieBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMovie();
            }
        });
        //Get data sent from previps activity
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String year = intent.getStringExtra("year");
        String rated = intent.getStringExtra("rated");
        String genr = intent.getStringExtra("genre");
        String releas = intent.getStringExtra("released");
        String runtime = intent.getStringExtra("runtime");
        String direct = intent.getStringExtra("director");
        String plot = intent.getStringExtra("plot");
        String rati = intent.getStringExtra("rating");
        String imageUrl = intent.getStringExtra("poster");
        final String jsonData = intent.getStringExtra("json");
        //Log.i("JSON RECEIVED", jsonData);
        Glide.with(getApplicationContext()).load(imageUrl).into(movieBack);
        channelName.setText(title);
        about.setText(plot);
        director.setText("Director: " + direct);
        otherStuff.setText(year + ", " + runtime);
        genre.setText("Genre: " + genr);
        released.setText("Released:" + releas);
        rating.setText("Rating: " + rati);
        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  favourites.setImageResource(R.drawable.ic_baseline_favorite);
                //Add the Json response to some sort of database
                //actually can add to the array list...then on the other side can get the data with a for loop to populate thee recycler view
                SharedPreferences preference = getSharedPreferences("credentials", MODE_PRIVATE);
                //check if user has previosly stored favourites data
                if (preference.contains("fav")) {
                    Log.i("SET CREATION", "DATA AVAILABLE");
                    Set<String> set = preference.getStringSet("fav", null);
                    set.add(jsonData);
                    SharedPreferences.Editor editor = preference.edit();
                    editor.putStringSet("fav", set);
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "Added to favourites", Toast.LENGTH_LONG).show();
                    Log.i("GENERATED SET", set.toString());

                } else {//no favourite video data was put in shared preferences
                    Log.i("SET CREATION", "NO DATA AVAILABLE");
                    Set<String> set = new HashSet<>();
                    set.add(jsonData);
                    SharedPreferences.Editor editor = preference.edit();
                    editor.putStringSet("fav", set);
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "Added to favourites", Toast.LENGTH_LONG).show();
                    Log.i("NEW SET", set.toString());

                }


            }
        });
    }


    private void loadMovie() {

        SharedPreferences preferences = getSharedPreferences("credentials", MODE_PRIVATE);
        //preferences is there...so get it and login
        String email = preferences.getString("email", "");
        String password = preferences.getString("password", "");
        Intent x = getIntent();
        final Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
        String rated = x.getStringExtra("rated");
        intent.putExtra("iconUrl", x.getStringExtra("poster"));
        intent.putExtra("itemUrl", x.getStringExtra("itemUrl") + "u=" + email + ":" + "p=" + password);
        intent.putExtra("actionName", x.getStringExtra("genre"));
        intent.putExtra("name", x.getStringExtra("name"));
        intent.putExtra("newTask", "");
        if (preferences.contains("pin")) {
            if (rated.contains("PG") || rated.contentEquals("N/A")) {
                final AlertDialog dialogBuilder = new AlertDialog.Builder(Movie_requested.this).create();
                LayoutInflater inflater = Movie_requested.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_enter_pin, null);
                dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                final TextView title = dialogView.findViewById(R.id.title);
                TextView secondary = dialogView.findViewById(R.id.secondaryText);
                final PinView pinView = dialogView.findViewById(R.id.secondPinView);
                pinView.setTextColor(
                        ResourcesCompat.getColor(getResources(), R.color.coloraccent, getTheme()));
                pinView.setTextColor(
                        ResourcesCompat.getColorStateList(getResources(), R.color.text_colors, getTheme()));
                pinView.setLineColor(
                        ResourcesCompat.getColor(getResources(), R.color.design_default_color_primary_variant, getTheme()));
                pinView.setLineColor(
                        ResourcesCompat.getColorStateList(getResources(), R.color.line_colors, getTheme()));
                pinView.setItemCount(4);
                pinView.setItemHeight(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
                pinView.setItemWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_size));
                pinView.setItemRadius(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_radius));
                pinView.setItemSpacing(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_spacing));
                pinView.setLineWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_item_line_width));
                pinView.setAnimationEnable(true);// start animation when adding text
                pinView.setCursorVisible(false);
                pinView.setCursorColor(
                        ResourcesCompat.getColor(getResources(), R.color.lb_list_item_unselected_text_color, getTheme()));
                pinView.setCursorWidth(getResources().getDimensionPixelSize(R.dimen.pv_pin_view_cursor_width));
                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Log.d("TYPED", "onTextChanged() called with: s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String pin = pinView.getText().toString().trim();
                        if (pinView.isFocused()) {
                            if (pin.length() != 4) {
                                title.setText("Please input a 4 digit pin");
                            } else {
                                final SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
                                String code = preferences.getString("pin", "");
                                if (!pin.contentEquals(code)) {
                                    title.setText("Ops! Check your pin");
                                } else {
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                        }
                    }
                };
                pinView.addTextChangedListener(textWatcher);
                pinView.setItemBackgroundColor(Color.BLACK);
                pinView.setItemBackground(getResources().getDrawable(R.drawable.item_background));
                pinView.setItemBackgroundResources(R.drawable.item_background);
                pinView.setHideLineWhenFilled(false);

                dialogBuilder.setView(dialogView);
                dialogBuilder.show();
            } else {//user subscribed to a password...but content does not require
                justGo(intent);
            }
        } else {//User did not subscribe to password

            justGo(intent);
        }


    }

    private void justGo(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    public void backtolist(View view) {
        startActivity(new Intent(getApplicationContext(), MovieList.class));
        finish();
    }

   /* @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), movieList.class));
        finish();

    }*/
}