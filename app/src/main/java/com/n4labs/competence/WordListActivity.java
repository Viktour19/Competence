package com.n4labs.competence;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;

public class WordListActivity extends Activity {
    private int size;
    private ArrayList<Word> WordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startService(new Intent(this,LockScreenService.class));
        ArrayList<Word> dailywordlist = new ArrayList<>();
        setContentView(R.layout.activity_wordlist);

        try {
            WordList = parsetext();
            size = WordList.size();
        }catch (IOException e) {
            e.printStackTrace();
        }

        if(uptotimelimit()) {
            for (int i = 0; i < 10; i++) {
                Random generator = new Random(System.currentTimeMillis() + i);
                final int randomIndex = generator.nextInt(size);
                dailywordlist.add(new Word(WordList.get(randomIndex).word, WordList.get(randomIndex).meaning));
                writeetofiledailylist(dailywordlist);
            }
        }

            dailywordlist = readdailywordlist();
            if (dailywordlist.size() == 10) {
                ((TextView) findViewById(R.id.meaning1)).setText(dailywordlist.get(0).meaning);
                ((TextView) findViewById(R.id.word1)).setText(dailywordlist.get(0).word);
                ((TextView) findViewById(R.id.meaning2)).setText(dailywordlist.get(1).meaning);
                ((TextView) findViewById(R.id.word2)).setText(dailywordlist.get(1).word);
                ((TextView) findViewById(R.id.meaning3)).setText(dailywordlist.get(2).meaning);
                ((TextView) findViewById(R.id.word3)).setText(dailywordlist.get(2).word);
                ((TextView) findViewById(R.id.meaning4)).setText(dailywordlist.get(3).meaning);
                ((TextView) findViewById(R.id.word4)).setText(dailywordlist.get(3).word);
                ((TextView) findViewById(R.id.meaning5)).setText(dailywordlist.get(4).meaning);
                ((TextView) findViewById(R.id.word5)).setText(dailywordlist.get(4).word);
                ((TextView) findViewById(R.id.meaning6)).setText(dailywordlist.get(5).meaning);
                ((TextView) findViewById(R.id.word6)).setText(dailywordlist.get(5).word);
                ((TextView) findViewById(R.id.meaning7)).setText(dailywordlist.get(6).meaning);
                ((TextView) findViewById(R.id.word7)).setText(dailywordlist.get(6).word);
                ((TextView) findViewById(R.id.meaning8)).setText(dailywordlist.get(7).meaning);
                ((TextView) findViewById(R.id.word8)).setText(dailywordlist.get(7).word);
                ((TextView) findViewById(R.id.meaning9)).setText(dailywordlist.get(8).meaning);
                ((TextView) findViewById(R.id.word9)).setText(dailywordlist.get(8).word);
                ((TextView) findViewById(R.id.meaning10)).setText(dailywordlist.get(9).meaning);
                ((TextView) findViewById(R.id.word10)).setText(dailywordlist.get(9).word);


        }

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
                bufferedReader.readLine();
               // Toast.makeText(getApplicationContext(),, Toast.LENGTH_LONG).show();
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

    private boolean uptotimelimit() {

            InputStream inputStream = null;
            try {
                inputStream = openFileInput("dailywordlist.txt");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }

            ArrayList<Word> dailywordlist = null;
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                try {
                    String time  = bufferedReader.readLine();
                  //  Toast.makeText(getApplicationContext(), time, Toast.LENGTH_LONG).show();
                    long lasttime = Long.valueOf(time);
                    long currtime = System.currentTimeMillis();

                    if(currtime - lasttime > 43200000)
                        return true;
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            else {
                dailywordlist = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    Random generator = new Random(System.currentTimeMillis() + i);
                    final int randomIndex = generator.nextInt(size);
                    dailywordlist.add(new Word(WordList.get(randomIndex).meaning, WordList.get(randomIndex).word));

                }
                writeetofiledailylist(dailywordlist);
            }
        return false;
    }

    private void writeetofiledailylist(ArrayList<Word> dailywordlist) {

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("dailywordlist.txt", Context.MODE_PRIVATE));

            String time = String.valueOf(System.currentTimeMillis());

            outputStreamWriter.write(String.format(time+"%s",System.getProperty("line.separator")));

            String word,meaning;

            for(int i =0; i< dailywordlist.size(); i++)
            {
                word = dailywordlist.get(i).word;
                word = String.format(word+"%s",System.getProperty("line.separator"));
                meaning = dailywordlist.get(i).meaning;
                meaning = String.format(meaning+"%s",System.getProperty("line.separator"));
                outputStreamWriter.write(word);
                outputStreamWriter.write(meaning);
            }
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
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
