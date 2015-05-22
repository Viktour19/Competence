package com.n4labs.competence;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set up our Lockscreen
        makeFullScreen();
        setContentView(R.layout.activity_main);

            final ArrayList<Word> WordList = readdailywordlist();
            int size = WordList.size();
            Random generator = new Random();
            final int randomIndex = generator.nextInt(size);
            TextView meaning = (TextView) findViewById(R.id.meaning);
            final EditText word = (EditText) findViewById(R.id.word);
            final String theword = WordList.get(randomIndex).word;
            final Button unlock = (Button) findViewById(R.id.unlockbutton);
            meaning.setText(String.valueOf(WordList.get(randomIndex).meaning));
            unlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        String inputwword = word.getText().toString();
                        int count=0;
                        if(theword.equalsIgnoreCase(inputwword))
                        {
                            unlockScreen(v);
                        }
                    else
                        {
                            Toast.makeText(getApplicationContext(), "Incorrect word", Toast.LENGTH_LONG).show();
                            count++;
                            if(count == 10 )
                                unlockScreen(v);
                        }
                }
            });


    }
    private ArrayList<Word> readdailywordlist() {
        ArrayList<Word> dailylist= new ArrayList<>();
        try {
            InputStream inputStream = openFileInput("dailywordlist.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String word;
                Word newword;
                Toast.makeText(getApplicationContext(), bufferedReader.readLine(), Toast.LENGTH_LONG).show();
                while ( (word = bufferedReader.readLine()) != null ) {
                    newword = new Word(bufferedReader.readLine(), word);
                    dailylist.add(newword);
                }

                inputStream.close();

            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return dailylist;
    }
    private ArrayList<Word> parsetext() throws  IOException{

        InputStream inputStream = getResources().openRawResource(R.raw.newwordlistmeaning);

        System.out.println(inputStream);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            ArrayList<Word> WordList = new ArrayList<>();
            if (inputStream!=null) {
                String str;
                Word word = null;

                while ((str = reader.readLine()) != null) {
                    if(!str.contains("::")) {
                        word = new Word();

                        String themeaning = reader.readLine();
                        if(!themeaning.contains("::")) {
                            str = themeaning;
                            themeaning = reader.readLine();
                            word.meaning = themeaning;
                        }
                        else
                        {
                            continue;
                        }

                        word.word = str;
                    }
                    WordList.add(word);
                }

                return WordList;
            }
        } finally {
            try { inputStream.close(); } catch (Throwable ignore) {}
        }

        return  null;
    }
//    private class ImageFetcher extends AsyncTask<Void, Void, String> {
//        private static final String TAG = "PostFetcher";
//        public static final String SERVER_URL = "http://kylewbanks.com/rest/posts";
//
//        @Override
//        protected String doInBackground(Void... params) {
//            try {
//                //Create an HTTP client
//                HttpClient client = new DefaultHttpClient();
//                HttpPost post = new HttpPost(SERVER_URL);
//
//                //Perform the request and check the status code
//                HttpResponse response = client.execute(post);
//                StatusLine statusLine = response.getStatusLine();
//                if (statusLine.getStatusCode() == 200) {
//                    HttpEntity entity = response.getEntity();
//                    InputStream content = entity.getContent();
//
//                    try {
//                        //Read the server response and attempt to parse it as JSON
//                        Reader reader = new InputStreamReader(content);
//
//                        GsonBuilder gsonBuilder = new GsonBuilder();
//                        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
//                        Gson gson = gsonBuilder.create();
//                        List<Post> posts = new ArrayList<Post>();
//                        posts = Arrays.asList(gson.fromJson(reader, Post[].class));
//                        content.close();
//
//                        handlePostsList(posts);
//                    } catch (Exception ex) {
//                        Log.e(TAG, "Failed to parse JSON due to: " + ex);
//                        failedLoadingPosts();
//                    }
//                } else {
//                    Log.e(TAG, "Server responded with status code: " + statusLine.getStatusCode());
//                    failedLoadingPosts();
//                }
//            } catch (Exception ex) {
//                Log.e(TAG, "Failed to send HTTP POST request due to: " + ex);
//                failedLoadingPosts();
//            }
//            return null;
//        }
//    }
//
//

    public void makeFullScreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(Build.VERSION.SDK_INT < 19) { //View.SYSTEM_UI_FLAG_IMMERSIVE is only on API 19+
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    @Override
    public void onBackPressed() {
        return; //Do nothing!
    }

    public void unlockScreen(View view) {
        //Instead of using finish(), this totally destroys the process
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
