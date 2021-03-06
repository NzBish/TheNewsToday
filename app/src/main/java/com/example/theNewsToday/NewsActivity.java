package com.example.theNewsToday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class NewsActivity extends AppCompatActivity {
    //required for newsapi.org
    String API_KEY = "8190df9eb51445228e397e4185311a66";
    public static String channel = "";
    ListView listNews;
    ProgressBar progressBar;
    int mPosition = 0;
     private static final String POSITION = "p";
     //list of news highlights
    ArrayList<HashMap<String, String>> newsList = new ArrayList<>();
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String URL = "url";
    static final String URL_TO_IMAGE = "urlToImage";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState!=null){
            mPosition=savedInstanceState.getInt(POSITION);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listNews = findViewById(R.id.listNews);
        progressBar = findViewById(R.id.progressBar);
        listNews.setEmptyView(progressBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
        }
        loadTitle(actionBar);
        DownloadNews newsTask = new DownloadNews();
        newsTask.execute();
        listNews.setSelection(mPosition);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        mPosition = listNews.getFirstVisiblePosition();
        outState.putInt(POSITION,mPosition);
        super.onSaveInstanceState(outState);
    }
    //downloads news headlines for selected channel from newsapi.org
    @SuppressLint("StaticFieldLeak")
    class DownloadNews extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            return Connection.startConnection("https://newsapi.org/v1/articles?source=" + channel + "&sortBy=top&apiKey=" + API_KEY);
        }

        @Override
        protected void onPostExecute(String news) {

            if (news.length() !=0) {
                try {
                    JSONObject jsonResponse = new JSONObject(news);
                    JSONArray jsonArray = jsonResponse.optJSONArray("articles");
                    if (jsonArray != null) {
                        //parse jsonarray and put into hashmap
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<>();
                            map.put(TITLE, jsonObject.optString(TITLE));
                            map.put(DESCRIPTION, jsonObject.optString(DESCRIPTION));
                            map.put(URL, jsonObject.optString(URL));
                            map.put(URL_TO_IMAGE, jsonObject.optString(URL_TO_IMAGE));
                            newsList.add(map);
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Sorry we had a problem with reading the news, Try again!", Toast.LENGTH_SHORT).show();
                }
                ListAdapter adapter = new ListAdapter(NewsActivity.this, newsList);
                listNews.setAdapter(adapter);
                listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent i = new Intent(NewsActivity.this, ViewActivity.class);
                        i.putExtra("url", newsList.get(+position).get(URL));
                        startActivity(i);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No news on this channel", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //load title and set news icon in actionbar
    void loadTitle(ActionBar actionBar){
        switch(channel){
            case "bbc-news": setTitle("  BBC NEWS");
                actionBar.setLogo(R.drawable.bbcnewslogosmall);
                break;
            case "the-new-york-times": setTitle("  NEW YORK TIMES");
                actionBar.setLogo(R.drawable.newyorktimeslogosmall);
                break;
            case "engadget": setTitle("  ENGADGET");
                actionBar.setLogo(R.drawable.engadgetlogosmall);
                break;
            case "time": setTitle("  TIME");
                actionBar.setLogo(R.drawable.timelogosmall);
                break;
            case "cnn": setTitle("  CNN");
                actionBar.setLogo(R.drawable.cnnlogosmall);
                break;
            case "national-geographic": setTitle("  NATIONAL GEOGRAPHIC");
                actionBar.setLogo(R.drawable.nationalgeographiclogosmall);
                break;

        }
    }
    //add night/dark mode options to actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nightMode:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case R.id.dayMode:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

}



