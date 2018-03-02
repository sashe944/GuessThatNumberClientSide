package com.example.home.guessthatnumber;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    public static final String URL = "http://10.168.160.102:8080/GuessThatNumberServer/";
    //public static final String URL = "http://10.168.160.100:8080/GuessThatNumberServer/";
    //public static final String URL = "http://10.168.160.104:8080/GuessThatNumberServer/";
  // public static final String URL = "http://192.168.1.36:8080/GuessThatNumberServer/";


    ImageView avatar;
    EditText usernameEditText;
    EditText passwordEditText;
    EditText repeatPasswordEditText;
    EditText emailEditText;
    RadioGroup sexRadioGroup;
    Button ok;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        avatar =  findViewById(R.id.avatarImageView);
        usernameEditText = findViewById(R.id.et_username);
        passwordEditText =  findViewById(R.id.et_password);
        repeatPasswordEditText =  findViewById(R.id.et_repeat_password);
        emailEditText =  findViewById(R.id.et_email);
        sexRadioGroup =  findViewById(R.id.genderRadioGroup);
        ok =  findViewById(R.id.btn_ok);
        cancel = findViewById(R.id.btn_cancel);

        ok.setOnClickListener(onClickListener);
        cancel.setOnClickListener(onClickListener);

        sexRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_male) {
                    avatar.setImageResource(R.drawable.male);
                } else {
                    avatar.setImageResource(R.drawable.female);
                }
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_cancel:
                    finishAffinity();
                    break;
                case R.id.btn_ok:
                    if (passwordEditText.getText().toString()
                            .equals(repeatPasswordEditText
                                    .getText().toString())) {
                        User user = new User();
                        user.username =
                                usernameEditText.getText().toString();
                        user.password =
                                passwordEditText.getText().toString();
                        user.email =
                                emailEditText.getText().toString();
                        user.imagePath = "noimage.png";

                        RadioButton rb =  findViewById(
                                sexRadioGroup.getCheckedRadioButtonId());

                        user.gender = rb.getText().toString();

                        new RegisterAsyncTask(user.username,user.password,user.email).execute();
                    }

            }
        }
    };


    private class RegisterAsyncTask extends AsyncTask<Void, Void, Void> {

        String username;
        String password;
        String email;
        String content;

        public RegisterAsyncTask(String username, String password, String email) {
            this.username = username;
            this.password = password;
            this.email = email;
        }

        ProgressDialog registerProgressDialog = new ProgressDialog(RegisterActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            registerProgressDialog.setTitle("Registering the player! Please wait...!");
            registerProgressDialog.setCanceledOnTouchOutside(false);
            registerProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            URL url;
            HttpURLConnection urlConnection;
            BufferedReader br;

            try{
                url = new URL(URL+"RegisterUserServlet" +
                        "?username=" + username + "&password=" + password + "&email=" +email
                +"&gender=male" + "&avatar=image.png");
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.d(TAG, "url: " + url.toString());
                br = new BufferedReader( new InputStreamReader( urlConnection.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while(line != null) {
                    sb.append(line);
                    line = br.readLine();
                }

                content = sb.toString();

            }catch(MalformedURLException e){
                Log.wtf("WRONG!", e.getMessage());
            }catch(IOException e){
                Log.wtf("WRONG!", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            registerProgressDialog.dismiss();

            Log.d(TAG, "content: " + content);

            try{
                JSONObject res = new JSONObject(content);
                User u = new User();
                u.username = res.getString("username");
                u.password = res.getString("password");
                u.id = res.getLong("id");
                if(u.id!=0){
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    intent.putExtra("player", u);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegisterActivity.this, "Registration not successful!", Toast.LENGTH_LONG).show();
                }
            }catch(JSONException e){
                Log.wtf("WRONG!", e.getMessage());
            }
        }
    }


}
