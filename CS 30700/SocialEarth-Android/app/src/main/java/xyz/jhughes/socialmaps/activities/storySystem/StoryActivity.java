package xyz.jhughes.socialmaps.activities.storySystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import objects.Command;
import objects.Response;
import objects.Story;
import objects.User;
import xyz.jhughes.socialmaps.R;
import xyz.jhughes.socialmaps.helpers.Alert;
import xyz.jhughes.socialmaps.adapters.StoryAdapter;
import xyz.jhughes.socialmaps.server.Server;

public class StoryActivity extends AppCompatActivity {

    @Bind(R.id.rec_view) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        Bundle extras = getIntent().getExtras();
        String storyId = extras.getString("storyid");
        SharedPreferences sharedPreferences = getSharedPreferences("xyz.jhughes.socialmaps", MODE_PRIVATE);

        ButterKnife.bind(this);

        try {
            getSupportActionBar().setTitle("Story");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            System.out.println("Toolbar is null");
        }

        User user = new User("id", sharedPreferences.getString("username", null), null, null, null, null, null);
        Story story = new Story(storyId, 0, 0, null, user.getUsername(), null, null, null, null, null);
        Command command1 = new Command(Command.CommandType.GET_STORY, story, user, null, null);
        new GetStoryTask().execute(command1);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.onBackPressed(); //since there can be nested submenus, back needs to be used.
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayStory(Story story) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        StoryAdapter storyAdapter = new StoryAdapter(story, this);
        storyAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(storyAdapter);
    }

    public class GetStoryTask extends AsyncTask<Command, Void, Response> {

        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], getApplicationContext());
        }

        protected void onPostExecute(Response response) {
            if (response == null)
                Alert.networkError(getApplicationContext());
            else if (!response.isBool())
                Alert.generalError(getApplicationContext());
            else
                displayStory(response.getStory());
        }
    }
}