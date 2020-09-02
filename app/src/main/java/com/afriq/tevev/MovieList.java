package com.afriq.tevev;

import android.app.AlertDialog;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class MovieList extends FragmentActivity implements com.afriq. tevev.adapter_chip.ClickedListener, NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    RecyclerView recyclerChip;
    private List<Object> chips = new ArrayList<>();
    private List<Object> viewItems = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter adapter_chip;
    TextView play;
    TextView about;
    TextView director;
    TextView otherStuff;
    CollapsingToolbarLayout collapsingToolbarLayout;
    //AppBarLayout appBarLayout;
    com.google.android.material.chip.Chip chip;
    NestedScrollView nestedScrollView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        nestedScrollView = findViewById(R.id.nested);
        chip = findViewById(R.id.morelogo);
        recyclerChip = findViewById(R.id.recycler_chip);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        toolbar = findViewById(R.id.mainToolbar);
        play = findViewById(R.id.playBTN);
        about = findViewById(R.id.about);
        director = findViewById(R.id.director);
        otherStuff = findViewById(R.id.other_stuff);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerChip.setHasFixedSize(true);
        recyclerChip.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        addChips();
        String fiter = "Action";
        addItemsFromJSON(fiter);
        /*
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    //collapsed
                    collapsingToolbarLayout.setTitle("MOVIES");
                    appBarLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                } else if (verticalOffset == 0) {
                    //expanded
                    collapsingToolbarLayout.setTitle("");

                } else {
                    //somewhere in between

                }
            }
        });

         */
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // appBarLayout.setExpanded(false, true);
                nestedScrollView.scrollBy(0, 1);
                nestedScrollView.smoothScrollTo(0, play.getBottom());


            }
        });
        //color the nav bar
        prepareUI();
        // toolbar = findViewById(R.id.mainToolbar);
        //  setSupportActionBar(toolbar);
        /*
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem movieTitle = menu.findItem(R.id.movieTitle);
        MenuItem accTitle = menu.findItem(R.id.accountTitle);
        SpannableString s = new SpannableString(movieTitle.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.textAppearance), 0, s.length(), 0);
        movieTitle.setTitle(s);
        SpannableString s1 = new SpannableString(accTitle.getTitle());
        s1.setSpan(new TextAppearanceSpan(this, R.style.textAppearance), 0, s1.length(), 0);
        accTitle.setTitle(s1);

        final ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.openNavDrawer, R.string.closeNavDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CLICKED", "Now toggle");
               // drawerLayout.openDrawer(Gravity.START);
            }
        });

         */
        View headerView = navigationView.getHeaderView(0);
        de.hdodenhof.circleimageview.CircleImageView circleImageView = headerView.findViewById(R.id.dp);
        RelativeLayout profile = headerView.findViewById(R.id.profile);
        TextView userName = headerView.findViewById(R.id.about);

        SharedPreferences sharedPreferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("picUri")) {
            String picString = sharedPreferences.getString("picUri", "");
            byte[] decodedByte = Base64.decode(picString, 0);
            Bitmap bitmap = BitmapFactory
                    .decodeByteArray(decodedByte, 0, decodedByte.length);
            Glide.with(getApplicationContext()).load(bitmap).into(circleImageView);
        } else {
            //circleImageView.setImageResource(R.drawable.ic_outline_account_circle_24_white);

            //Glide.with(getApplicationContext()).load(R.drawable.ic_outline_account_circle_24_white).into(circleImageView);
        }
        if (sharedPreferences.contains("email")) {
            String mail = sharedPreferences.getString("email", "");
            userName.setText(mail);


        } else {
            userName.setText("My user name");
        }

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("dp", "onNavigationItemSelected: ");
                startActivity(new Intent(getApplicationContext(), Person.class));
            }
        });


    }

    private void prepareUI() {
        /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.endcolorgradient));
        }*/

        //SET IMERSIVE MODE
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            /*View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    //View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
            //set up custom preferences for advanced mode
           */
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.design_default_color_primary_dark));
            }
        }

    }

    private void addChips() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Action");
        list.add("Adventure");
        list.add("Animation");
        list.add("Comedy");
        list.add("Action");
        list.add("Crime");
        list.add("Documentary");
        list.add("Family");
        list.add("Fantasy");
        list.add("Fiction");
        list.add("History");
        list.add("Horror");
        list.add("Music");
        list.add("Mystery");
        list.add("Romance");
        list.add("Sci-Fi");
        list.add("Thriller");
        list.add("Tv-Show");
        list.add("War");
        list.add("Western");
        list.add("X-mas");

        for (int i = 0; i < list.size(); i++) {
            String x = list.get(i);
            Model_chips model = new Model_chips(x);
            chips.add(model);
            adapter_chip = new adapter_chip(getApplicationContext(), chips, this);
            recyclerChip.setAdapter(adapter_chip);
        }


    }

    private void addItemsFromJSON(final String filter) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String mediaurl = "http://102.69.224.246:5555/api/filtermovies/" + filter;
                    Request request = new Request.Builder()
                            .url(mediaurl)
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    Response response = client.newCall(request).execute();
                    final String res = response.body().string();
                    //Log.i("SERVER RESPONSE", res);
                    MovieList.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                JSONObject jObj = new JSONObject(res);
                                JSONArray jsonArry = jObj.getJSONArray("movies");
                                //Log.i("JSON",res);
                                HashMap<Integer, HashMap<String, String>> outerMap = new HashMap<>();
                                viewItems.clear();
                                int initialValue = 0;
                                for (int i = 0; i < jsonArry.length(); i++) {
                                    initialValue = i;
                                    JSONObject obj = jsonArry.getJSONObject(i);
                                    String title = obj.getString("title");
                                    String year = obj.getString("year");
                                    String rated = obj.getString("rated");
                                    String genre = obj.getString("genre");
                                    String released = obj.getString("released");
                                    String runtime = obj.getString("runtime");
                                    String director = obj.getString("director");
                                    String plot = obj.getString("plot");
                                    String poster = obj.getString("poster");
                                    String rating = obj.getString("rating");
                                    String url = obj.getString("url");
                                    //put in a hashmap..........
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("title", title);
                                    map.put("year", year);
                                    map.put("rated", rated);
                                    map.put("genre", genre);
                                    map.put("released", released);
                                    map.put("runtime", runtime);
                                    map.put("director", director);
                                    map.put("plot", plot);
                                    map.put("poster", poster);
                                    map.put("rating", rating);
                                    map.put("url", url);
                                    outerMap.put(i, map);
                                    //load it inside the inner hashap
                                    Model_movie model_movie = new Model_movie(title, year, rated, genre, released, runtime, director, plot, poster, rating, url);
                                    viewItems.add(model_movie);
                                    mAdapter = new adapter(getApplicationContext(), viewItems);
                                    mAdapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(mAdapter);
                                    //Get the fist eight;
                                }
                                //Log.i("MAP", outerMap.toString());
                                Random rand = new Random(); //instance of random class
                                int low = 0;
                                int high = 8;
                                int int_random = rand.nextInt(high - low) + low;
                                Log.i("RANDOM:->", String.valueOf(int_random));
                                if (int_random == 0) {
                                    int value0 = 0;
                                    HashMap<String, String> randomMap = outerMap.get(value0);
                                    //Log.i("RANDOM MAP", randomMap.toString());
                                    final String title = randomMap.get("title");
                                    String poster = randomMap.get("poster");
                                    final String plot = randomMap.get("plot");
                                    final String direct = randomMap.get("director");
                                    final String released = randomMap.get("released");
                                    final String runtime = randomMap.get("runtime");
                                    final String link = randomMap.get("url");
                                    final String genre = randomMap.get("genre");

                                    Log.i("title:", title);
                                    Log.i("poster:", poster);
                                    Log.i("plot:", plot);
                                    Log.i("direct:", direct);
                                    Log.i("released:", released);
                                    Log.i("runtime:", runtime);
                                    final String ico = "http://102.69.224.246:5555/" + poster;
                                    //String value = ((HashMap<String, String>)outerMap.get("OuterKey")).get("InnerKey").toString();
                                    Thread thread1 = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                final Drawable drawable = Drawable.createFromStream(new URL(ico).openConnection().getInputStream(), "src");
                                                MovieList.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        collapsingToolbarLayout.setBackground(drawable);
                                                        about.setText(plot);
                                                        director.setText(direct);
                                                        otherStuff.setText(released + ", " + runtime);

                                                    }
                                                });
                                            } catch (Exception e) {

                                            }
                                        }
                                    });
                                    thread1.start();
                                    SharedPreferences preferences = getSharedPreferences("credentials", MODE_PRIVATE);
                                    //preferences is there...so get it and login
                                    final String email = preferences.getString("email", "");
                                    final String password = preferences.getString("password", "");
                                    final String itemUrl = link + "u=" + email + "p=" + password;
                                    postTheFeatureMovie(ico, itemUrl, genre, title, email, password);

                                }
                                if (int_random == 1) {
                                    int value0 = 1;
                                    HashMap<String, String> randomMap = outerMap.get(value0);
                                    // Log.i("RANDOM MAP", randomMap.toString());
                                    final String title = randomMap.get("title");
                                    String poster = randomMap.get("poster");
                                    final String plot = randomMap.get("plot");
                                    final String direct = randomMap.get("director");
                                    final String released = randomMap.get("released");
                                    final String runtime = randomMap.get("runtime");
                                    final String link = randomMap.get("url");
                                    final String genre = randomMap.get("genre");

                                    Log.i("title:", title);
                                    Log.i("poster:", poster);
                                    Log.i("plot:", plot);
                                    Log.i("direct:", direct);
                                    Log.i("released:", released);
                                    Log.i("runtime:", runtime);
                                    final String ico = "http://102.69.224.246:5555/" + poster;
                                    //String value = ((HashMap<String, String>)outerMap.get("OuterKey")).get("InnerKey").toString();
                                    Thread thread1 = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                final Drawable drawable = Drawable.createFromStream(new URL(ico).openConnection().getInputStream(), "src");
                                                MovieList.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        collapsingToolbarLayout.setBackground(drawable);
                                                        about.setText(plot);
                                                        director.setText(direct);
                                                        otherStuff.setText(released + ", " + runtime);

                                                    }
                                                });
                                            } catch (Exception e) {

                                            }
                                        }
                                    });
                                    thread1.start();
                                    SharedPreferences preferences = getSharedPreferences("credentials", MODE_PRIVATE);
                                    //preferences is there...so get it and login
                                    final String email = preferences.getString("email", "");
                                    final String password = preferences.getString("password", "");
                                    final String itemUrl = link + "u=" + email + "p=" + password;
                                    postTheFeatureMovie(ico, itemUrl, genre, title, email, password);


                                }
                                if (int_random == 2) {
                                    int value0 = 2;
                                    HashMap<String, String> randomMap = outerMap.get(value0);
                                    //Log.i("RANDOM MAP", randomMap.toString());
                                    final String title = randomMap.get("title");
                                    String poster = randomMap.get("poster");
                                    final String plot = randomMap.get("plot");
                                    final String direct = randomMap.get("director");
                                    final String released = randomMap.get("released");
                                    final String runtime = randomMap.get("runtime");
                                    final String link = randomMap.get("url");
                                    final String genre = randomMap.get("genre");

                                    Log.i("title:", title);
                                    Log.i("poster:", poster);
                                    Log.i("plot:", plot);
                                    Log.i("direct:", direct);
                                    Log.i("released:", released);
                                    Log.i("runtime:", runtime);
                                    final String ico = "http://102.69.224.246:5555/" + poster;

                                    //String value = ((HashMap<String, String>)outerMap.get("OuterKey")).get("InnerKey").toString();
                                    Thread thread1 = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                final Drawable drawable = Drawable.createFromStream(new URL(ico).openConnection().getInputStream(), "src");
                                                MovieList.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        collapsingToolbarLayout.setBackground(drawable);
                                                        about.setText(plot);
                                                        director.setText(direct);
                                                        otherStuff.setText(released + ", " + runtime);

                                                    }
                                                });
                                            } catch (Exception e) {

                                            }
                                        }
                                    });
                                    SharedPreferences preferences = getSharedPreferences("credentials", MODE_PRIVATE);
                                    //preferences is there...so get it and login
                                    final String email = preferences.getString("email", "");
                                    final String password = preferences.getString("password", "");
                                    final String itemUrl = link + "u=" + email + "p=" + password;
                                    postTheFeatureMovie(ico, itemUrl, genre, title, email, password);


                                }
                                if (int_random == 3) {
                                    int value0 = 3;
                                    HashMap<String, String> randomMap = outerMap.get(value0);
                                    //Log.i("RANDOM MAP", randomMap.toString());
                                    final String title = randomMap.get("title");
                                    String poster = randomMap.get("poster");
                                    final String plot = randomMap.get("plot");
                                    final String direct = randomMap.get("director");
                                    final String released = randomMap.get("released");
                                    final String runtime = randomMap.get("runtime");
                                    final String link = randomMap.get("url");
                                    final String genre = randomMap.get("genre");

                                    Log.i("title:", title);
                                    Log.i("poster:", poster);
                                    Log.i("plot:", plot);
                                    Log.i("direct:", direct);
                                    Log.i("released:", released);
                                    Log.i("runtime:", runtime);
                                    final String ico = "http://102.69.224.246:5555/" + poster;

                                    //String value = ((HashMap<String, String>)outerMap.get("OuterKey")).get("InnerKey").toString();
                                    Thread thread1 = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                final Drawable drawable = Drawable.createFromStream(new URL(ico).openConnection().getInputStream(), "src");
                                                MovieList.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        collapsingToolbarLayout.setBackground(drawable);
                                                        about.setText(plot);
                                                        director.setText(direct);
                                                        otherStuff.setText(released + ", " + runtime);

                                                    }
                                                });
                                            } catch (Exception e) {

                                            }
                                        }
                                    });
                                    thread1.start();
                                    SharedPreferences preferences = getSharedPreferences("credentials", MODE_PRIVATE);
                                    //preferences is there...so get it and login
                                    final String email = preferences.getString("email", "");
                                    final String password = preferences.getString("password", "");
                                    final String itemUrl = link + "u=" + email + "p=" + password;
                                    postTheFeatureMovie(ico, itemUrl, genre, title, email, password);

                                }
                                if (int_random == 4) {
                                    int value0 = 4;
                                    HashMap<String, String> randomMap = outerMap.get(value0);
                                    //Log.i("RANDOM MAP", randomMap.toString());
                                    final String title = randomMap.get("title");
                                    String poster = randomMap.get("poster");
                                    final String plot = randomMap.get("plot");
                                    final String direct = randomMap.get("director");
                                    final String released = randomMap.get("released");
                                    final String runtime = randomMap.get("runtime");
                                    final String link = randomMap.get("url");
                                    final String genre = randomMap.get("genre");

                                    Log.i("title:", title);
                                    Log.i("poster:", poster);
                                    Log.i("plot:", plot);
                                    Log.i("direct:", direct);
                                    Log.i("released:", released);
                                    Log.i("runtime:", runtime);
                                    final String ico = "http://102.69.224.246:5555/" + poster;

                                    //String value = ((HashMap<String, String>)outerMap.get("OuterKey")).get("InnerKey").toString();
                                    Thread thread1 = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                final Drawable drawable = Drawable.createFromStream(new URL(ico).openConnection().getInputStream(), "src");
                                                MovieList.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        collapsingToolbarLayout.setBackground(drawable);
                                                        about.setText(plot);
                                                        director.setText(direct);
                                                        otherStuff.setText(released + ", " + runtime);

                                                    }
                                                });
                                            } catch (Exception e) {

                                            }
                                        }
                                    });
                                    thread1.start();
                                    SharedPreferences preferences = getSharedPreferences("credentials", MODE_PRIVATE);
                                    //preferences is there...so get it and login
                                    final String email = preferences.getString("email", "");
                                    final String password = preferences.getString("password", "");
                                    final String itemUrl = link + "u=" + email + "p=" + password;
                                    postTheFeatureMovie(ico, itemUrl, genre, title, email, password);


                                }
                                if (int_random == 5) {
                                    int value0 = 5;
                                    HashMap<String, String> randomMap = outerMap.get(value0);
                                    //Log.i("RANDOM MAP", randomMap.toString());
                                    final String title = randomMap.get("title");
                                    String poster = randomMap.get("poster");
                                    final String plot = randomMap.get("plot");
                                    final String direct = randomMap.get("director");
                                    final String released = randomMap.get("released");
                                    final String runtime = randomMap.get("runtime");
                                    final String link = randomMap.get("url");
                                    final String genre = randomMap.get("genre");

                                    Log.i("title:", title);
                                    Log.i("poster:", poster);
                                    Log.i("plot:", plot);
                                    Log.i("direct:", direct);
                                    Log.i("released:", released);
                                    Log.i("runtime:", runtime);
                                    final String ico = "http://102.69.224.246:5555/" + poster;

                                    //String value = ((HashMap<String, String>)outerMap.get("OuterKey")).get("InnerKey").toString();
                                    Thread thread1 = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                final Drawable drawable = Drawable.createFromStream(new URL(ico).openConnection().getInputStream(), "src");
                                                MovieList.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        collapsingToolbarLayout.setBackground(drawable);
                                                        about.setText(plot);
                                                        director.setText(direct);
                                                        otherStuff.setText(released + ", " + runtime);

                                                    }
                                                });
                                            } catch (Exception e) {

                                            }
                                        }
                                    });
                                    thread1.start();
                                    SharedPreferences preferences = getSharedPreferences("credentials", MODE_PRIVATE);
                                    //preferences is there...so get it and login
                                    final String email = preferences.getString("email", "");
                                    final String password = preferences.getString("password", "");
                                    final String itemUrl = link + "u=" + email + "p=" + password;
                                    postTheFeatureMovie(ico, itemUrl, genre, title, email, password);

                                }
                                if (int_random == 6) {
                                    int value0 = 6;
                                    HashMap<String, String> randomMap = outerMap.get(value0);
                                    //Log.i("RANDOM MAP", randomMap.toString());
                                    final String title = randomMap.get("title");
                                    String poster = randomMap.get("poster");
                                    final String plot = randomMap.get("plot");
                                    final String direct = randomMap.get("director");
                                    final String released = randomMap.get("released");
                                    final String runtime = randomMap.get("runtime");
                                    final String link = randomMap.get("url");
                                    final String genre = randomMap.get("genre");

                                    Log.i("title:", title);
                                    Log.i("poster:", poster);
                                    Log.i("plot:", plot);
                                    Log.i("direct:", direct);
                                    Log.i("released:", released);
                                    Log.i("runtime:", runtime);
                                    final String ico = "http://102.69.224.246:5555/" + poster;

                                    //String value = ((HashMap<String, String>)outerMap.get("OuterKey")).get("InnerKey").toString();
                                    Thread thread1 = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                final Drawable drawable = Drawable.createFromStream(new URL(ico).openConnection().getInputStream(), "src");
                                                MovieList.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        collapsingToolbarLayout.setBackground(drawable);
                                                        about.setText(plot);
                                                        director.setText(direct);
                                                        otherStuff.setText(released + ", " + runtime);

                                                    }
                                                });
                                            } catch (Exception e) {

                                            }
                                        }
                                    });
                                    thread1.start();
                                    SharedPreferences preferences = getSharedPreferences("credentials", MODE_PRIVATE);
                                    //preferences is there...so get it and login
                                    final String email = preferences.getString("email", "");
                                    final String password = preferences.getString("password", "");
                                    final String itemUrl = link + "u=" + email + "p=" + password;
                                    postTheFeatureMovie(ico, itemUrl, genre, title, email, password);

                                }
                                if (int_random == 7) {
                                    int value0 = 7;
                                    HashMap<String, String> randomMap = outerMap.get(value0);
                                    //Log.i("RANDOM MAP", randomMap.toString());
                                    final String title = randomMap.get("title");
                                    String poster = randomMap.get("poster");
                                    final String plot = randomMap.get("plot");
                                    final String direct = randomMap.get("director");
                                    final String released = randomMap.get("released");
                                    final String runtime = randomMap.get("runtime");
                                    final String link = randomMap.get("url");
                                    final String genre = randomMap.get("genre");

                                    Log.i("title:", title);
                                    Log.i("poster:", poster);
                                    Log.i("plot:", plot);
                                    Log.i("direct:", direct);
                                    Log.i("released:", released);
                                    Log.i("runtime:", runtime);
                                    final String ico = "http://102.69.224.246:5555/" + poster;

                                    //String value = ((HashMap<String, String>)outerMap.get("OuterKey")).get("InnerKey").toString();
                                    Thread thread1 = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                final Drawable drawable = Drawable.createFromStream(new URL(ico).openConnection().getInputStream(), "src");
                                                MovieList.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        collapsingToolbarLayout.setBackground(drawable);
                                                        about.setText(plot);
                                                        director.setText(direct);
                                                        otherStuff.setText(released + ", " + runtime);

                                                    }
                                                });
                                            } catch (Exception e) {

                                            }
                                        }
                                    });
                                    thread1.start();
                                    SharedPreferences preferences = getSharedPreferences("credentials", MODE_PRIVATE);
                                    //preferences is there...so get it and login
                                    final String email = preferences.getString("email", "");
                                    final String password = preferences.getString("password", "");
                                    final String itemUrl = link + "u=" + email + "p=" + password;
                                    postTheFeatureMovie(ico, itemUrl, genre, title, email, password);

                                }
                                if (int_random == 8) {
                                    int value0 = 8;
                                    HashMap<String, String> randomMap = outerMap.get(value0);
                                    //Log.i("RANDOM MAP", randomMap.toString());
                                    final String title = randomMap.get("title");
                                    String poster = randomMap.get("poster");
                                    final String plot = randomMap.get("plot");
                                    final String direct = randomMap.get("director");
                                    final String released = randomMap.get("released");
                                    final String runtime = randomMap.get("runtime");
                                    final String link = randomMap.get("url");
                                    final String genre = randomMap.get("genre");

                                    Log.i("title:", title);
                                    Log.i("poster:", poster);
                                    Log.i("plot:", plot);
                                    Log.i("direct:", direct);
                                    Log.i("released:", released);
                                    Log.i("runtime:", runtime);
                                    final String ico = "http://102.69.224.246:5555/" + poster;

                                    //String value = ((HashMap<String, String>)outerMap.get("OuterKey")).get("InnerKey").toString();
                                    Thread thread1 = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                final Drawable drawable = Drawable.createFromStream(new URL(ico).openConnection().getInputStream(), "src");
                                                MovieList.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        collapsingToolbarLayout.setBackground(drawable);
                                                        about.setText(plot);
                                                        director.setText(direct);
                                                        otherStuff.setText(released + ", " + runtime);

                                                    }
                                                });
                                            } catch (Exception e) {

                                            }
                                        }
                                    });
                                    thread1.start();
                                    SharedPreferences preferences = getSharedPreferences("credentials", MODE_PRIVATE);
                                    //preferences is there...so get it and login
                                    final String email = preferences.getString("email", "");
                                    final String password = preferences.getString("password", "");
                                    final String itemUrl = link + "u=" + email + "p=" + password;
                                    postTheFeatureMovie(ico, itemUrl, genre, title, email, password);

                                }


                            } catch (Exception e) {
                                Log.i("ERROR", "FAILED TO POPULATE");
                            }
                        }
                    });


                } catch (Exception e) {
                    Log.i("----SERVER ERROR-----", e.getMessage());
                }

            }
        });
        thread.start();

    }

    private void postTheFeatureMovie(final String ico, final String itemUrl, final String genre, final String title, String email, String password) {
        TextView play = findViewById(R.id.playBTN);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieList.this, PlayerActivity.class);
                intent.putExtra("iconUrl", ico);
                intent.putExtra("itemUrl", itemUrl);
                intent.putExtra("actionName", genre);
                intent.putExtra("name", title);
                intent.putExtra("newTask", "");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Log.i("CLICKED FIRED", "postTheFeatureMovie: ");
            }
        });

    }

    @Override
    public void onClicked(String filter) {
        Log.i("CLICKED-> ", filter);
        addItemsFromJSON(filter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.movies:
                Log.i("movies", "onNavigationItemSelected: ");
                break;
            case R.id.series:
                Log.i("series", "onNavigationItemSelected: ");
                startActivity(new Intent(getApplicationContext(), SeriesListActivity.class));
                break;

            case R.id.tv:
                Toast.makeText(this, "Press back to dismiss this", Toast.LENGTH_SHORT).show();
                final PlaylistAdapter mAdapter;
                final AlertDialog dialogBuilder = new AlertDialog.Builder(MovieList.this).create();
                LayoutInflater inflater = MovieList.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_recycler, null);
                dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                RecyclerView recyclerViewDialog = dialogView.findViewById(R.id.dialogRecycler);
                recyclerViewDialog.setHasFixedSize(true);
                recyclerViewDialog.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerViewDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBuilder.dismiss();
                    }
                });
                mAdapter = new PlaylistAdapter(getApplicationContext());
                recyclerViewDialog.setAdapter(mAdapter);
                SharedPreferences pref = getSharedPreferences("tv", MODE_PRIVATE);
                String channelString = pref.getString("channelList", "");

                try {
                    InputStream is = new ByteArrayInputStream(channelString.getBytes("UTF-8"));
                    M3UParser parser = new M3UParser();
                    M3UPlaylist playlist = parser.parseFile(is);
                    mAdapter.update(playlist.getPlaylistItems());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MovieList.this, "Sorry we could not get your channel list because: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
                dialogBuilder.setView(dialogView);
                WindowManager.LayoutParams wlp = dialogBuilder.getWindow().getAttributes();
                wlp.gravity = Gravity.TOP | Gravity.LEFT;
                wlp.x = 0;
                wlp.y = 0;
                dialogBuilder.setCancelable(true);
                dialogBuilder.show();
                Log.i("tv", "onNavigationItemSelected: ");
                break;
            case R.id.pg:
                Log.i("PARENTAL CONTROL", "onNavigationItemSelected: ");
                break;
            case R.id.fav:
                Log.i("FAVORITES", "onNavigationItemSelected: ");

                break;


        }

        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    public class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {
        private final Context context;
        private final List<Object> listRecyclerItem;

        public adapter(Context context, List<Object> listRecyclerItem) {
            this.context = context;
            this.listRecyclerItem = listRecyclerItem;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_movie_display, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Model_movie model = (Model_movie) listRecyclerItem.get(position);
            final String ico = "http://102.69.224.246:5555/" + model.getPoster();
            Glide.with(context).load(ico).into(holder.movieBack);
            holder.movieBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Movie_requested.class);
                    intent.putExtra("poster", ico);
                    intent.putExtra("itemUrl", model.getUrl());
                    intent.putExtra("name", model.getTitle());
                    intent.putExtra("rated", model.getRated());
                    intent.putExtra("genre", model.getGenre());
                    intent.putExtra("year", model.getYear());
                    intent.putExtra("released", model.getReleased());
                    intent.putExtra("runtime", model.getRuntime());
                    intent.putExtra("director", model.getDirector());
                    intent.putExtra("rating", model.getRating());
                    intent.putExtra("plot", model.getPlot());
                    JSONObject object = new JSONObject();
                    try {

                        object.put("poster", ico);
                        object.put("itemUrl", model.getUrl());
                        object.put("title", model.getTitle());
                        object.put("rated", model.getRated());
                        object.put("genre", model.getGenre());
                        object.put("year", model.getYear());
                        object.put("released", model.getReleased());
                        object.put("runtime", model.getRuntime());
                        object.put("director", model.getDirector());
                        object.put("rating", model.getRated());
                        object.put("plot", model.getPlot());
                        Log.i("PICTURE", object.getString("poster"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i(e.getLocalizedMessage(), ":ERROR");
                    }

                    intent.putExtra("json", object.toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            });
        }

        @Override
        public int getItemCount() {
            return listRecyclerItem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView movieBack;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                movieBack = itemView.findViewById(R.id.movie_back);
            }


        }
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Exiting", Toast.LENGTH_LONG).show();
        finish();
        System.exit(0);

    }

}