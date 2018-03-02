package com.example.home.guessthatnumber;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG = "LogInActivity";

    EditText username_field;
    EditText password_field;
    Button logIn;
    Button register;
    public static final String URL = "http://10.168.160.102:8080/GuessThatNumberServer/";
    //public static final String URL = "http://10.168.160.100:8080/GuessThatNumberServer/";
    //public static final String URL= "http://10.168.160.104:8080/GuessThatNumberServer/";
    //public static final String URL= "http://192.168.1.36:8080/GuessThatNumberServer/";
     // public static final String URL = "http://192.168.0.103:8080/GuessThatNumberServer/";
      @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_log_in);

            username_field =  findViewById(R.id.et_username);
            password_field =  findViewById(R.id.et_password);
            logIn =  findViewById(R.id.btn_login);
            register =  findViewById(R.id.btn_register);

            logIn.setOnClickListener(onClickListener);
            register.setOnClickListener(onClickListener);
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();

                if (v.getId() == R.id.btn_register){
                    intent = new Intent(LogInActivity.this, RegisterActivity.class);
                    startActivity(intent);

                } else {
                    String username = username_field.getText().toString();
                    String password = password_field.getText().toString();



                   new LoginAsyncTask(username,password).execute();

                }

            }
        };

    private class LoginAsyncTask
            extends AsyncTask<Void, Void , Void>{

        ProgressDialog dialogLogIn =
                new ProgressDialog(LogInActivity.this);
        String username;
        String password;
        String result;

        public LoginAsyncTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "login in....");
            dialogLogIn.setTitle("Login in please wait!");
            dialogLogIn.setCanceledOnTouchOutside(false);
            dialogLogIn.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "communicating with server");
            URL url;
            HttpURLConnection urlConnection;
            BufferedReader br;

            try{
                url = new URL( URL + "LoginServlet?username=" + username + "&password="+ password);
                Log.d(TAG, "url: " + url.toString());
                urlConnection = (HttpURLConnection)
                        url.openConnection();

                br = new BufferedReader
                        (new InputStreamReader(
                                urlConnection.getInputStream()
                        ));

                StringBuilder content = new StringBuilder();

                String line = br.readLine();
                while(line != null){
                    content.append(line);
                    line = br.readLine();
                }

                result = content.toString();
            }catch (MalformedURLException e){
                Log.wtf("WRONG!", e.getMessage());
            }catch(IOException e){
                Log.wtf("WRONG!", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG, "taking data");
            super.onPostExecute(aVoid);
            dialogLogIn.dismiss();

            Log.d(TAG, "result: " + result);

            try{
                JSONObject resultJson =
                        new JSONObject(result);

                Log.d(TAG, "json: " + resultJson);

                User user = new User();
                user.username = resultJson.getString("username");
                user.password = resultJson.getString("password");
                user.email = resultJson.getString("email");
                user.id = resultJson.getLong("id");


                if(user.id != 0){
                    Intent intent = new Intent(LogInActivity.this,
                            MainActivity.class);

                    intent.putExtra("player", user);


                    startActivity(intent);
                }else{
                    Toast.makeText(LogInActivity.this,
                            "Wrong password or username!",
                            Toast.LENGTH_LONG).show();
                }

            }catch (JSONException e){
                Log.wtf("WRONG!", e.getMessage());
            }

        }
    }
}
