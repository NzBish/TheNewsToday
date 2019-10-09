package com.example.theNewsToday;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

    String API_KEY = "8190df9eb51445228e397e4185311a66";
    String CHANNEL = "cnn";
    ListView listNews;
    ProgressBar progressBar;

    ArrayList<HashMap<String, String>> newsList = new ArrayList<>();
    static final String KEY_AUTHOR = "author";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_URL = "url";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_PUBLISHEDAT = "publishedAt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listNews = findViewById(R.id.listNews);
        progressBar = findViewById(R.id.progressBar);
        listNews.setEmptyView(progressBar);
        DownloadNews newsTask = new DownloadNews();
        newsTask.execute();
    }

    @SuppressLint("StaticFieldLeak")
    class DownloadNews extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            return Connection.startConnection("https://newsapi.org/v1/articles?source=" + CHANNEL + "&sortBy=top&apiKey=" + API_KEY);
        }

        @Override
        protected void onPostExecute(String news) {

            if (news.length() !=0) {
                try {
                    JSONObject jsonResponse = new JSONObject(news);
                    JSONArray jsonArray = jsonResponse.optJSONArray("articles");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<>();
                            map.put(KEY_AUTHOR, jsonObject.optString(KEY_AUTHOR));
                            map.put(KEY_TITLE, jsonObject.optString(KEY_TITLE));
                            map.put(KEY_DESCRIPTION, jsonObject.optString(KEY_DESCRIPTION));
                            map.put(KEY_URL, jsonObject.optString(KEY_URL));
                            map.put(KEY_URLTOIMAGE, jsonObject.optString(KEY_URLTOIMAGE));
                            map.put(KEY_PUBLISHEDAT, jsonObject.optString(KEY_PUBLISHEDAT));
                            newsList.add(map);
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "We had a problem with reading the news, Try again!", Toast.LENGTH_SHORT).show();
                }
                ListAdapter adapter = new ListAdapter(NewsActivity.this, newsList);
                listNews.setAdapter(adapter);
                listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent i = new Intent(NewsActivity.this, ViewActivity.class);
                        i.putExtra("url", newsList.get(+position).get(KEY_URL));
                        startActivity(i);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No news on this channel", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



