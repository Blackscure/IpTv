package com.afriq.tevev;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
public class LoginActivity extends Activity {
    private EditText email;
    private EditText password;
    CheckBox checkBox;
    Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        checkBox = findViewById(R.id.checkBox);
        login_button =  findViewById(R.id.login_button);

        login_button.setOnClickListener(view -> {
            String userName= "wekesa";
            String pass = "wekesa";
            login(userName, pass);
            Log.d("clicked","yiotijrii");
        });



        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.endcolor));
        }
        SharedPreferences loginCredentials = getSharedPreferences("credentials", MODE_PRIVATE);

        if (loginCredentials.contains("email")) {
            String mail = loginCredentials.getString("email", "");
            String pass = loginCredentials.getString("password", "");
            password.getText();
            email.getText();

            password.setText(pass);
            email.setText(mail);
            checkBox.setChecked(true);
            login(mail, pass);
            Snackbar.make(checkBox, "please wait", Snackbar.LENGTH_LONG).show();

        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    Snackbar.make(checkBox, "Next time we will automatically log you in", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(checkBox, "Okay we'll not automatically log you in", Snackbar.LENGTH_LONG).show();

                }
            }
        });

    }

    public void validateInput(View v) {
        if (!validateMail() | !validatePasswordl()) {
            return;
        } else {
            String userName = email.getText().toString().trim();
            String pass = password.getText().toString().trim();

            SharedPreferences preferences = getSharedPreferences("credentials", MODE_PRIVATE);
            if (preferences.contains("email")) {
                //preferences is there...so get it and login
                String email = preferences.getString("email", "");
                String password = preferences.getString("password", "");
                login(email, password);
            } else {
                if (checkBox.isChecked()) {//create one
                    SharedPreferences preferences2 = getSharedPreferences("credentials", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences2.edit();
                    editor.putString("email", userName);
                    editor.putString("password", pass);
                    editor.apply();
                } else {//force autoremember by adding remembering credential anywhere

                    SharedPreferences preferences2 = getSharedPreferences("credentials", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences2.edit();
                    editor.putString("email", userName);
                    editor.putString("password", pass);
                    editor.apply();
                    login(userName, pass);
                }

                //preference is not there....checkif the user want to create one
            }
        }
    }

    private void login(String userName, String pass) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(LoginActivity.this).create();
        LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_login, null);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);


        SharedPreferences pref = getSharedPreferences("lastViewed", MODE_PRIVATE);
        if (pref.contains("itemName")) {
            String iconUrl = pref.getString("iconUrl", "");
            String vidUrl = pref.getString("itemUrl", "");
            String itemname = pref.getString("itemName", "");
            String actionName = pref.getString("actionName", "");
            intentFunction(iconUrl, vidUrl, itemname, actionName, userName, pass, dialogBuilder);

        } else {
            String iconUrl = "https://c.blackbird.host/ico/bbc.ico";
            String vidUrl = "http://102.69.224.246:8056/BBC?u=wekesa:p=wekesa";
            String itemname = "BBC";
            String actionName = "NEWS";

            intentFunction(iconUrl, vidUrl, itemname, actionName, userName, pass, dialogBuilder);
        }


    }

    private void intentFunction(final String iconUrl, final String vidUrl, final String itemname, String actionName, final String userName, final String password, final AlertDialog dialogBuilder) {
        dialogBuilder.show();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 3; i++) {
                    //GET CHANNEL LIST
                    if (i == 1) {
                        toast("Could not connect, trying again");
                    }
                    if (i == 2) {
                        toast("This taking too long check your internet connection");
                    }
                    if (i == 3) {
                        toast("After attempting to connect three times we could not connect, check your internet connection, then try again");
                        dialogBuilder.dismiss();
                        break;
                    }
                    try {//

                        String channelUrl = "http://102.69.224.246:8056/getlink?username=" + userName + ":" + "password=" + password + ":type=m3u";
                        Request request = new Request.Builder()
                                .url(channelUrl)
                                .build();
                        OkHttpClient client = new OkHttpClient();
                        Response response = client.newCall(request).execute();
                        final String res = response.body().string();
                        Log.i("SERVER RESPONSE", res);

                        //GET MOVIE LIST
                        String movieUrl = "http://102.69.224.246:8056/getlink?username=" + userName + ":" + "password=" + password + ":type=m3u:content=movie";
                        Request request2 = new Request.Builder()
                                .url(movieUrl)
                                .build();
                        OkHttpClient client2 = new OkHttpClient();
                        Response response2 = client2.newCall(request2).execute();
                        final String res2 = response2.body().string();
                        Log.i("SERVER RESPONSE 2", res2);

                        if (res.startsWith("#EXTM3U")) {//If the response starts with a particular style we have valid data
                            //valid response
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //put the channel list as a preference
                                    SharedPreferences preferences2 = getSharedPreferences("tv", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences2.edit();
                                    editor.putString("movieList", res2);
                                    editor.putString("channelList", res);
                                    editor.apply();
                                    Intent intent = new Intent(getApplicationContext(), MovieList.class);
                                    intent.putExtra("name", itemname);
                                    intent.putExtra("iconUrl", iconUrl);
                                    intent.putExtra("itemUrl", vidUrl);
                                    intent.putExtra("userName", userName);
                                    intent.putExtra("password", password);
                                    dialogBuilder.dismiss();
                                    startActivity(intent);
                                    finish();

                                }
                            });

                        } else {
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialogBuilder.dismiss();
                                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                                    materialAlertDialogBuilder.setTitle("OPS!").setMessage("We could not verify your credentials. Check your username and password then try again")
                                            .setIcon(R.drawable.live_tv).setPositiveButton("OKAY THANKS", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    }).show();
                                }
                            });

                        }
                        break;

                    } catch (final IOException e) {
                    }
                }
            }
        });
        thread.start();
    }

    private void toast(final String s) {
        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

            }
        });

    }


    private boolean validateMail() {
        String x = email.getText().toString().trim();
        if (x.isEmpty()) {
            email.setError("field cannot be empty");
            return false;
        } else {
            email.setError(null);
            return true;
        }

    }

    private boolean validatePasswordl() {
        String x = password.getText().toString().trim();
        if (x.isEmpty()) {
            password.setError("field cannot be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void forgotPassword(View view) {
        SharedPreferences loginCredentials = getSharedPreferences("credentials", MODE_PRIVATE);
        if (loginCredentials.contains("email")) {
            String mail = loginCredentials.getString("email", "");
            final String pass = loginCredentials.getString("password", "");
            password.getText();
            email.getText();

            password.setText(pass);
            email.setText(mail);
            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
            materialAlertDialogBuilder.setTitle("Your credentials").setMessage("Here are the credentials you used to login to you account last time. Keep it safe" + "\n" + "User name: " + mail + "\n" + "password: " + pass).setPositiveButton("COPY PASSWORD", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("saved to clip", pass);
                    clipboard.setPrimaryClip(clip);
                    Snackbar.make(checkBox, "Password copied to clipboard", Snackbar.LENGTH_LONG).show();

                }
            });

        } else {
            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
            materialAlertDialogBuilder.setTitle("Your credentials").setMessage("We could not find the credentials you used to login to you account last time.").setPositiveButton("OKAY, THANKS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

        }

    }
}
