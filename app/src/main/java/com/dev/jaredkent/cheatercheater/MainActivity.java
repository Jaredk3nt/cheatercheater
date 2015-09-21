package com.dev.jaredkent.cheatercheater;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class MainActivity extends FragmentActivity {
    private TreeMap<Character, Set<String>> dictMap;
    FragmentPagerAdapter adapterViewPager;
    SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager vp = (ViewPager) findViewById(R.id.vp);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(adapterViewPager);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vp);

        this.dictMap = new TreeMap<Character, Set<String>>();
        System.out.println("onCreate");
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("dictionary.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                char firstLet = line.charAt(0);
                if(dictMap.containsKey(firstLet))
                    dictMap.get(firstLet).add(line);
                else{
                    TreeSet<String> temp = new TreeSet<String>();
                    temp.add(line);
                    dictMap.put(line.charAt(0), temp);
                }
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void solve(View view){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        //onclick get data from edit text fields
        EditText letters = (EditText) findViewById(R.id.letters);
        EditText extraLet = (EditText) findViewById(R.id.extraLetters);
        String lett = letters.getText().toString();
        String extra = extraLet.getText().toString();

        View solvedView = (View)findViewById(R.id.solvedView);
        //safety checks
        if(lett.length() > 0 && extra.length() > 0) {

            lett.toLowerCase();
            extra.toLowerCase();


            Solver s = new Solver((lett + extra), dictMap, extra);

            List<DescSet> scoreSet = s.scoreSets();
            if(scoreSet.isEmpty()){
                Toast toast = Toast.makeText(this, "This hand has no words! Sorry!", Toast.LENGTH_LONG);
                toast.show();
            }else{
                ListView scoreList = (ListView)findViewById(R.id.scoreList);

                ArrayList<String> scores = new ArrayList<String>();
                for(int i = 0 ; i < scoreSet.size(); i++){
                    scores.add(scoreSet.get(i).toString());
                }
                if(!scores.isEmpty()){

                    TextView bestWord = (TextView) findViewById(R.id.bestword);
                    bestWord.setText(scores.get(0));

                    scores.remove(0);

                    scoreList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,scores));
                }else{

                }
                View input = (View)findViewById(R.id.bigbox);




                // get the center for the clipping circle
                int cx = solvedView.getWidth() / 2;
                int cy = solvedView.getHeight() / 2;

                // get the final radius for the clipping circle
                int finalRadius = Math.max(solvedView.getWidth(), solvedView.getHeight());

                // create the animator for this view (the start radius is zero)
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(solvedView, cx, cy, 0, finalRadius);

                // make the view visible and start the animation
                solvedView.setVisibility(View.VISIBLE);
                anim.start();
                input.setVisibility(View.GONE);
            }


        }else if(extra.length() < 1 && lett.length() > 0){
            Solver s = new Solver(lett, dictMap, null);

            List<DescSet> scoreSet = s.scoreSets();
            if(scoreSet.isEmpty()){
                Toast toast = Toast.makeText(this, "This hand has no words! Sorry!", Toast.LENGTH_LONG);
                toast.show();
            }else{
                ListView scoreList = (ListView)findViewById(R.id.scoreList);

                ArrayList<String> scores = new ArrayList<String>();
                for(int i = 0 ; i < scoreSet.size(); i++){
                    scores.add(scoreSet.get(i).toString());
                }
                if(!scores.isEmpty()){

                    TextView bestWord = (TextView) findViewById(R.id.bestword);
                    bestWord.setText(scores.get(0));

                    scores.remove(0);

                    scoreList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,scores));
                }else{

                }
                View input = (View)findViewById(R.id.bigbox);
                solvedView.setVisibility(View.VISIBLE);
                input.setVisibility(View.GONE);
            }


        }else{
            Toast toast = Toast.makeText(this, "You must input a hand!", Toast.LENGTH_LONG);
            toast.show();
        }


    }

    public void search(View view){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        EditText wordsearch = (EditText)findViewById(R.id.wordsearch);
        String word = wordsearch.getText().toString();
        if(word.length() > 0) {
            word.toLowerCase();

            Set<String> letSet = dictMap.get(word.charAt(0));
            boolean contained = letSet.contains(word);

            //View below search
            TextView wordView = (TextView) findViewById(R.id.word);
            wordView.setText(word);

            TextView found = (TextView) findViewById(R.id.found);

            if (contained) {
                found.setText("is a word in the dictionary!");
            } else {
                found.setText("is not found in the dictionary.");
            }

            View wordFound = (View) findViewById(R.id.wordFound);

            // get the center for the clipping circle
            int cx = wordFound.getWidth() / 2;
            int cy = wordFound.getHeight() / 2;

            // get the final radius for the clipping circle
            int finalRadius = Math.max(wordFound.getWidth(), wordFound.getHeight());

            // create the animator for this view (the start radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(wordFound, cx, cy, 0, finalRadius);

            // make the view visible and start the animation
            wordFound.setVisibility(View.VISIBLE);
            anim.start();
            wordFound.setVisibility(View.VISIBLE);
        }else{
            Toast toast = Toast.makeText(this, "You must input a word!", Toast.LENGTH_LONG);
            toast.show();
        }
    }


    public static class MyPagerAdapter extends FragmentPagerAdapter{
        private static int NUM_ITEMS = 2;
        private String[] tabTitles = new String[] {"Search", "Solve"};

        public MyPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        public int getCount(){
            return NUM_ITEMS;
        }

        public Fragment getItem(int position){
            switch(position){
                case 0:
                    return SearchFragment.newInstance(0, "search");
                case 1:
                    return SolveFragment.newInstance(1, "solve");
                default:
                    return null;
            }
        }

        public CharSequence getPageTitle(int position){
            return tabTitles[position];
        }
    }

    public void back(View view){
        View solvedView = (View)findViewById(R.id.solvedView);
        View input = (View)findViewById(R.id.bigbox);


// get the center for the clipping circle
        int cx = input.getWidth() / 2;
        int cy = input.getHeight() / 2;

// get the final radius for the clipping circle
        int finalRadius = Math.max(input.getWidth(), input.getHeight());

// create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(input, cx, cy, 0, finalRadius);

// make the view visible and start the animation
        input.setVisibility(View.VISIBLE);
        anim.start();
        solvedView.setVisibility(View.GONE);
    }

}
