package com.example.home.guessthatnumber;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Home on 2.3.2018 г..
 */

class LeaderBoardAdapter extends ArrayAdapter<LeaderBoard> {
    public LeaderBoardAdapter(Context context, int resource, ArrayList<LeaderBoard> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.l_board_row, null);

           TextView username = convertView.findViewById(R.id.lb_tv_username);
           TextView points = convertView.findViewById(R.id.lb_tv_points);

           username.setText(getItem(position).username);
           points.setText(getItem(position).points);
        }
        return convertView;
    }
}
