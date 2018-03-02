package com.example.home.guessthatnumber;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    private static final String TAG = "GameOverActivity";
   // public static final String POINTS = "POINTS";*/

   TextView user_id;
   TextView user_points;
   String player_id;
   String playerPoints;
   String player_username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        user_id = findViewById(R.id.tv_username);
        user_points = findViewById(R.id.tv_points);

        player_id = String.valueOf(getIntent().getExtras().getLong("user_id"));
        playerPoints = String.valueOf(getIntent().getExtras().getInt("playersPoints"));
        player_username = getIntent().getExtras().getString("username");
        Log.d(TAG,"playerPoints registered: " + playerPoints);
        Log.d(TAG,"players username: "+player_username);

        user_id.setText(player_username);
        user_points.setText(playerPoints);
    }

}

