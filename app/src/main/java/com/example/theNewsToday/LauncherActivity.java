package com.example.theNewsToday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

public class LauncherActivity extends AppCompatActivity {

    GridLayout gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        TextView textView = findViewById(R.id.gridTitle);
        textView.setSelected(true);
        gridView = findViewById(R.id.gridView);
        for(int i=0;i<gridView.getChildCount();i++){
            View view = gridView.getChildAt(i);
            final int j = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  switch(j){
                      case 0: NewsActivity.channel = "bbc-news";
                      break;
                      case 1: NewsActivity.channel = "the-new-york-times";
                      break;
                      case 2: NewsActivity.channel = "engadget";
                      break;
                      case 3: NewsActivity.channel = "time";
                      break;
                      case 4: NewsActivity.channel = "cnn";
                      break;
                      case 5: NewsActivity.channel = "national-geographic";
                      break;

                  }
                  Intent i = new Intent(LauncherActivity.this,NewsActivity.class);
                  startActivity(i);
                }
            });
        }
    }
}
