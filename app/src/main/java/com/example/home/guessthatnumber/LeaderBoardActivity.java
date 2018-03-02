package com.example.home.guessthatnumber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class LeaderBoardActivity extends AppCompatActivity {

    public static final String URL = "http://10.168.160.100:8080/GuessThatNumberServer/";
    ListView lv_lb;
    Button btn_get_leaderboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        lv_lb = findViewById(R.id.lv_leader_board);
        btn_get_leaderboard = findViewById(R.id.btn_get_board);

        btn_get_leaderboard.setOnClickListener(OnGetBoardListener);

    }

    View.OnClickListener OnGetBoardListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
