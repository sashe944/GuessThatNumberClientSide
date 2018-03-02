package com.example.home.guessthatnumber;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayGameFragment extends Fragment {


    public PlayGameFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_play_game, container, false);

        TextView welcome = view.findViewById(R.id.tv_play_game);

        Bundle bundleUser = getArguments();
        User user = (User) bundleUser.getSerializable("player");

        assert user != null;
        welcome.setText(String.format("Wanna play a game %s", user.username + "?"));

        return view;
    }

}
