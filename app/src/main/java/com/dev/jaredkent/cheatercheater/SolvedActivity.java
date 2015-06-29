package com.dev.jaredkent.cheatercheater;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


public class SolvedActivity extends ActionBarActivity {

    SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solved);
        //get data from intent
        Intent intent = getIntent();
        ListView scoreList = (ListView)findViewById(R.id.scoreList);

        ArrayList<DescSet> scoreSet = (ArrayList<DescSet>)intent.getExtras().get("solved");
        ArrayList<String> scores = new ArrayList<String>();
        for(int i = 1 ; i < scoreSet.size(); i++){
            scores.add(scoreSet.get(i).toString());
        }
        if(scoreSet != null){

            TextView bestWord = (TextView) findViewById(R.id.word);
            bestWord.setText(scoreSet.get(0).toString());

            scoreList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,scores));
        }else{

        }
        //set best choice word

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_solved, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void back(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
