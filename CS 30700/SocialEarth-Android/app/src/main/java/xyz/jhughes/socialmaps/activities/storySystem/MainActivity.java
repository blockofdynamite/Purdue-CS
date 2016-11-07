package xyz.jhughes.socialmaps.activities.storySystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import objects.BasicStoryInfo;
import objects.Command;
import objects.Response;
import objects.User;
import xyz.jhughes.socialmaps.R;
import xyz.jhughes.socialmaps.activities.followersSystem.FollowersActivity;
import xyz.jhughes.socialmaps.activities.followersSystem.FollowingListActivity;
import xyz.jhughes.socialmaps.activities.followersSystem.UserActivity;
import xyz.jhughes.socialmaps.activities.loginSystem.LoginActivity;
import xyz.jhughes.socialmaps.helpers.Alert;
import xyz.jhughes.socialmaps.server.Server;

public class MainActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String username;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private GoogleApiClient client;

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void goToMyProfile() {
        System.out.println("Profile");
        startActivity(new Intent(getApplicationContext(), UserActivity.class));
    }

    private void goToMyFriends() {
        startActivity(new Intent(getApplicationContext(), FollowingListActivity.class));
    }

    private void goToMyFollowers() {
        startActivity(new Intent(getApplicationContext(), FollowersActivity.class));
    }

    private void goToSettings() {
        startActivity(new Intent(getApplicationContext(), FollowingListActivity.class));
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("xyz.jhughes.socialmaps", MODE_PRIVATE);
        sharedPreferences.edit().putString("username", null).apply();
        sharedPreferences.edit().putString("establishedPassword", null).apply();
        sharedPreferences.edit().putBoolean("loggedIn", false);
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    private void addDrawerItems() {
        String[] array = {"My Profile", "My Following", "My Followers", "Settings", "Logout"};
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, array);
        System.out.println(mAdapter);
        System.out.println(mDrawerList);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.bringToFront();
        mDrawerLayout.requestLayout();

        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                        goToMyProfile();
                        break;
                    case 1:
                        goToMyFriends();
                        break;
                    case 2:
                        goToMyFollowers();
                        break;
                    case 3:
                        goToSettings();
                        break;
                    case 4:
                        logout();
                        break;
                    default:
                        break;
                }
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        try {
            this.username = getIntent().getExtras().getString("usernameForStories", null);
        } catch (NullPointerException e) {
                this.username = null;
        }

        // setup the drawer
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        setupDrawer();

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        addDrawerItems();

        try {
            getSupportActionBar().setTitle("SocialMaps - ALPHA");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "ActionBar NULL", Toast.LENGTH_LONG).show();
        }

        setUpMapIfNeeded();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NewStoryActivity.class));
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        User user;
        if (this.username == null)
            user = new User(null, getSharedPreferences("xyz.jhughes.socialmaps", MODE_PRIVATE).getString("username", null), null, null,
                getSharedPreferences("xyz.jhughes.socialmaps", MODE_PRIVATE).getString("password", null), null, null);
        else
            user = new User(null, this.username, null, null, null, null, null);

        Command command;
        User myUser = new User(null, getSharedPreferences("xyz.jhughes.socialmaps", MODE_PRIVATE).getString("username", null), null, null,
                getSharedPreferences("xyz.jhughes.socialmaps", MODE_PRIVATE).getString("password", null), null, null);
        if (this.username == null)
            command = new Command(Command.CommandType.GET_FOLLOWING_USERS_STORIES, null, user, myUser, null);
        else
            command = new Command(Command.CommandType.GET_SPECIFIC_USERS_STORIES, null, user, myUser, null);

        new MapUpdate().execute(command);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                startActivity((new Intent(getApplicationContext(), StoryActivity.class)).putExtra("storyid", marker.getSnippet()));
                return true;
            }
        });
    }

    private void addPointsToMap(ArrayList<MarkerOptions> options) {
        //Temporary for testing
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        if (options == null) {
            //You're a boring person or have no friends
            System.out.println("No points");
            Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show();
            return;
        } else if (options.size() == 0) {
            Toast.makeText(this, "No stories to show", Toast.LENGTH_LONG).show();
            return;
        }
        for (MarkerOptions option : options) {
            mMap.addMarker(option);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://xyz.jhughes.socialmaps/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://xyz.jhughes.socialmaps/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    class MapUpdate extends AsyncTask<Command, Void, Response> {

        @Override
        protected Response doInBackground(Command... params) {
            return Server.sendCommandToServer(params[0], getApplicationContext());
        }

        @Override
        protected void onPostExecute(Response response) {
            if (response == null) {
                Alert.networkError(getApplicationContext());
                return;
            }
            try {
                ArrayList<MarkerOptions> markerOptions = new ArrayList<>();
                for (BasicStoryInfo basicStoryInfo : response.getListOfStories()) {
                    markerOptions.add(new MarkerOptions()
                            .position(new LatLng(basicStoryInfo.getLatitude(), basicStoryInfo.getLongitude()))
                            .title(basicStoryInfo.getStoryName())
                            .snippet(basicStoryInfo.getId()));
                }
                addPointsToMap(markerOptions);
            } catch (Exception e) {
                Alert.generalError(getApplicationContext());
            }
        }
    }

}
