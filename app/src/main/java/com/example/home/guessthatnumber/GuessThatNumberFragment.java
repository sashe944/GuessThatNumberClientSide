package com.example.home.guessthatnumber;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class GuessThatNumberFragment extends Fragment {

    ListView lv_lboard;
    private static final String TAG = "GuessThatNumberFragment";

    //declaring few needed thing beforehand
    Button play;
    Button leader_board;
    GridLayout grid;
    ObjectAnimator anim;
    CountDownTimer gameTimer;
    ProgressBar progressBar;
    String username;

    public static final String URL = "http://10.168.160.102:8080/GuessThatNumberServer/";
    //public static final String URL = "http://10.168.160.100:8080/GuessThatNumberServer/";
    /*public static final String URL = "http://10.168.160.104:8080/GuessThatNumberServer/";*/
   // public static final String URL = "http://192.168.0.103:8080/GuessThatNumberServer/";
    // public static final String URL = "http://192.168.0.103:8080/GuessThatNumberServer/";

    TextView level;
    Button col00;
    Button col01;
    Button col02;
    Button col03;
    Button col10;
    Button col11;
    Button col12;
    Button col13;
    Button col20;
    Button col21;
    Button col22;
    Button col23;
    Button col30;
    Button col31;
    Button col32;
    Button col33;
    private List<String> randomChosenCellIndexes;
    private Map<String, Button> cellsMap;
    private CountDownTimer startGameTimer;
    Points point = new Points();

    public GuessThatNumberFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_guess_that_number, container, false);
        //after that initializing them in on create method with the proper cast of the view

        play = view.findViewById(R.id.btn_play);
        leader_board = view.findViewById(R.id.btn_leaderboard);
        grid = view.findViewById(R.id.gridLayout);
        progressBar = view.findViewById(R.id.progressBar);
        point.playerPoints = 0;
        point.user_id = getUserFromArgs().id;
        username = getUserFromArgs().username;
        lv_lboard = view.findViewById(R.id.lv_leader_board);


        level = view.findViewById(R.id.tv_level);
        col00 = view.findViewById(R.id.cell00);
        col01 = view.findViewById(R.id.cell01);
        col02 = view.findViewById(R.id.cell02);
        col03 = view.findViewById(R.id.cell03);
        col10 = view.findViewById(R.id.cell10);
        col11 = view.findViewById(R.id.cell11);
        col12 = view.findViewById(R.id.cell12);
        col13 = view.findViewById(R.id.cell13);
        col20 = view.findViewById(R.id.cell20);
        col21 = view.findViewById(R.id.cell21);
        col22 = view.findViewById(R.id.cell22);
        col23 = view.findViewById(R.id.cell23);
        col30 = view.findViewById(R.id.cell30);
        col31 = view.findViewById(R.id.cell31);
        col32 = view.findViewById(R.id.cell32);
        col33 = view.findViewById(R.id.cell33);

        //and putting them on HashMap with Key(String) and value(Button)
        cellsMap = new HashMap();
        cellsMap.put("cell00",col00);
        cellsMap.put("cell01",col01);
        cellsMap.put("cell02",col02);
        cellsMap.put("cell03",col03);
        cellsMap.put("cell10",col10);
        cellsMap.put("cell11",col11);
        cellsMap.put("cell12",col12);
        cellsMap.put("cell13",col13);
        cellsMap.put("cell20",col20);
        cellsMap.put("cell21",col21);
        cellsMap.put("cell22",col22);
        cellsMap.put("cell23",col23);
        cellsMap.put("cell30",col30);
        cellsMap.put("cell31",col31);
        cellsMap.put("cell32",col32);
        cellsMap.put("cell33",col33);

        //after that making OnClickListener for all the Buttons
        col00.setOnClickListener(onClickListener);
        col01.setOnClickListener(onClickListener);
        col02.setOnClickListener(onClickListener);
        col03.setOnClickListener(onClickListener);
        col10.setOnClickListener(onClickListener);
        col11.setOnClickListener(onClickListener);
        col12.setOnClickListener(onClickListener);
        col13.setOnClickListener(onClickListener);
        col20.setOnClickListener(onClickListener);
        col21.setOnClickListener(onClickListener);
        col22.setOnClickListener(onClickListener);
        col23.setOnClickListener(onClickListener);
        col30.setOnClickListener(onClickListener);
        col31.setOnClickListener(onClickListener);
        col32.setOnClickListener(onClickListener);
        col33.setOnClickListener(onClickListener);

        //on the play button starts the implementation of the algorithm
        play.setOnClickListener(onPlayListener);
        leader_board.setOnClickListener(OnGetLeaderboardListener);



        return view;
    }
    private int userInteractionCounter ;
    private int gameLevel = 3;
         //STEP FOR CHECKING THE REQUIREMENTS

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // v.setBackgroundColor(Color.parseColor("#7FFF00"));
                //before we start playing we have to prepare few things
                //for starter we indicate that the current button was pressed
                v.setSelected(true);
                // and after that for size of the randomly generated cells ( 3 for example ) we check if the id of the selected button == the key from the map
                // and follows the required sequence
                for (Map.Entry<String, Button> buttonEntry : cellsMap.entrySet()) {
                    if (buttonEntry.getValue().getId() == v.getId()) {
                        if (buttonEntry.getKey().equalsIgnoreCase(randomChosenCellIndexes.get(userInteractionCounter))){
                            //if yes then we save +1 point to "userInteractionCounter"
                            // TODO OK number Points + 1
                            point.playerPoints++;
                            userInteractionCounter++;
                            buttonEntry.getValue().setText(String.valueOf(userInteractionCounter));
                            if (userInteractionCounter == randomChosenCellIndexes.size()) {
                                // LEVEL COMPLETED!!!
                                // TODO Go to ext level
                                //if the size (3 example ) was attained and guessed correctly all times them we increment the level
                                cancelStartGameTimer();
                                incrementLevel();
                            }
                        } else {
                            //if we guess incorrect the 2nd timer stops and GameOver for us
                            cancelStartGameTimer();
                            gameOver();
                        }
                    }
                }

            }
        };

    //STEP 1
    View.OnClickListener onPlayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           Log.d(TAG, "startGame");
            //after the pressed button make the button disabled
            v.setEnabled(false);
            //show the player the level that they begin with with a TextView
            level.setText("level 1");
            //for the first time we call the method "generateRandomTextView"( which maybe will be better to be named "generateRandomCellsIndexes")
            generateRandomTextView();
            //after we took care ot picking random buttons and setting the proper text we call method "startRememberingTimer()"
            startRememberingTimer();
        }
    };

    private User getUserFromArgs(){

       return (User) getArguments().getSerializable("player");
    }

    private void incrementLevel() {
        Log.d(TAG, "increment Level");

        //here we increment the level
        gameLevel++;
        //then we show the user on witch level are at the moment
        level.setText("level " + (gameLevel - 2)); //why this way ???
        //we re-draw the game board
        resetCellViews();
        //(2 time )re-drawing the board for play
        generateRandomTextView();
        //and again we start a timer for remembering the numbers
        startRememberingTimer();
    }

    private void resetCellViews() {
        //here the re-draw is happening
        for (Map.Entry<String, Button> buttonEntry : cellsMap.entrySet()) {
            buttonEntry.getValue().setText("");
            //and resetting all the buttons ( a.k.a make them selectable again )
            buttonEntry.getValue().setSelected(false);
        }
    }

    //STEP 3
    private void generateRandomTextView(){
        int rows = 0;
        int cols = 0;
        Random random = new Random();
        randomChosenCellIndexes = new ArrayList();
        //here we cycle the columns and rows ( witch are randomly picked ) ind a Do while cycle
        //the idea here is to check if the randomly generated indexes overlaps themselves ind if they do
        //to skip the setText of the current button and pick another button
        //this happens wit a boolean flag that checks for the overlapping
        do {
            rows = random.nextInt(4);
            cols = random.nextInt(4);

            String newRandomIndex = "cell" + rows + cols;

            //and after that we store the 3 picks in arrayList ( which implements the interface List )
            //concatenates the string"cells" with randomly generated rows and columns
            boolean isPresent = false;
            for (String index : randomChosenCellIndexes) {
                if (index.equalsIgnoreCase(newRandomIndex)) {
                    isPresent = true;
                    break;
                }
            }
            if (!isPresent) randomChosenCellIndexes.add(newRandomIndex);
        } while (randomChosenCellIndexes.size() < gameLevel);

        // and if they don't overlap then we cycling the indexes and put them in the button's text ( little more understanding of the second line )
        //wield guess -> get the button stored in the cellMap beforehand, wit the randomly generated index for a cell and then set the text ????
        for (int cellIndex = 0; cellIndex < randomChosenCellIndexes.size(); cellIndex++) {
            cellsMap.get(randomChosenCellIndexes.get(cellIndex)).setText(String.valueOf(cellIndex + 1));
            //the whole thing is based on the gameLevel variable
        }
    }

    //STEP4
    //timer for remembering the numbers
    private void startRememberingTimer() {
        Log.d(TAG, "startRememberingTimer");

        gameTimer = new CountDownTimer((gameLevel + 2) * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //onTick we apply a progress bar to show the user the time he got left
                anim = ObjectAnimator.ofInt(progressBar, "progress", 100, 0);
                anim.setDuration(/*milsFuture*/(gameLevel + 2) * 1000);
                anim.setInterpolator(new DecelerateInterpolator());
                anim.start();
            }

            @Override
            public void onFinish() {

                Log.d(TAG, "Finished remembering time");
                for (Map.Entry<String, Button> buttonEntry : cellsMap.entrySet()) {
                    buttonEntry.getValue().setText("");
                    Log.d(TAG, "clear text for " + buttonEntry.getKey());
                }
                 //here we start the second timer for user interaction
                startPlayingLevel();
            }
        }.start();
    }

    //STEP5
    private void startPlayingLevel() {
        Log.d(TAG, "startPlayingLevel");
        userInteractionCounter = 0;
        startGameTimer = new CountDownTimer( (gameLevel + 5) * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                anim = ObjectAnimator.ofInt(progressBar, "progress", 100, 0);
                anim.setDuration((gameLevel + 5) * 1000);
                anim.setInterpolator(new DecelerateInterpolator());
                anim.start();
            }

            @Override
            public void onFinish() {
                //onFinish we start method gameOver where the points get stored in the remote DB
                Log.d(TAG, "finished playing level");
                gameOver();
            }
        }.start();
    }
     //STEP MIDDLE if something went wrong
    private void cancelStartGameTimer() {
        if (startGameTimer!=null)
            startGameTimer.cancel();
    }

    private void gameOver() {
        Log.d(TAG, "game over");
       // TODO show GAME OVER SCREEN and save the score in remote DB
                new RegisterPointsAsyncTask(point.user_id,point.playerPoints).execute();
   }

   //ADDITIONAL STEP FOR LEADERBOARD
    View.OnClickListener OnGetLeaderboardListener = new View.OnClickListener() {
       @Override
       public void onClick(View v) {

           Intent intent = new Intent(getContext(),LeaderBoardActivity.class);
           startActivity(intent);
          //new LeaderBoardAsyncTask().execute();
       }
   };

    private class RegisterPointsAsyncTask extends AsyncTask<Void, Void, Void> {

         long user_id;
         int point;
         String content;

        public RegisterPointsAsyncTask(long user_id, int point) {
            this.user_id = user_id;
            this.point = point;
            Log.d(TAG,"username: " + username);

        }

        ProgressDialog registerProgressDialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            registerProgressDialog.setTitle("Registering the points! Please wait...!");
            registerProgressDialog.setCanceledOnTouchOutside(false);
            registerProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            URL url;
            HttpURLConnection urlConnection;
            BufferedReader br;

            try{
                url = new URL(URL+"RegisterUserPointsServlet" +
                        "?points="+point+"&user_id="+user_id);
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
               Points point = new Points();
               point.playerPoints = res.getInt("points");
               point.user_id = res.getLong("user_id");
                if(point.user_id!=0){
                    Intent intent = new Intent(getContext(),GameOverActivity.class);
                    intent.putExtra("playersPoints", point.playerPoints);
                    intent.putExtra("user_id",point.user_id);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(), "Registration of Points not successful!", Toast.LENGTH_LONG).show();
                }
            }catch(JSONException e){
                Log.wtf("WRONG!", e.getMessage());
            }
        }
    }

    private class LeaderBoardAsyncTask extends AsyncTask<Void,Void,Void> {

        ProgressDialog LeaderBoardProgressDialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LeaderBoardProgressDialog.setTitle("Getting your score!");
            LeaderBoardProgressDialog.setCanceledOnTouchOutside(false);
            LeaderBoardProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            URL url;
            HttpURLConnection urlConnection;
            BufferedReader br;

            try{
                url = new URL(URL+"LeaderBoardServlet");
                urlConnection = (HttpURLConnection)url.openConnection();
                br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null){
                    sb.append(line);
                    line = br.readLine();
                }

                ArrayList<LeaderBoard> items = new ArrayList<>();
                JSONArray array = new JSONArray(sb.toString());

                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.getJSONObject(i);

                    LeaderBoard lb = new LeaderBoard();
                    lb.username = object.getString("username");
                    lb.points = object.getInt("points");

                    items.add(lb);
                }

              /*  LeaderBoardAdapter adapter = new LeaderBoardAdapter(LeaderBoardActivity.this, R.layout.l_board_row, items);*/
                lv_lboard.setAdapter(new LeaderBoardAdapter(getContext(),R.layout.l_board_row,items));

            }catch (MalformedURLException e){

            }catch (IOException e){

            }catch (JSONException e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LeaderBoardProgressDialog.dismiss();
        }
    }

}
