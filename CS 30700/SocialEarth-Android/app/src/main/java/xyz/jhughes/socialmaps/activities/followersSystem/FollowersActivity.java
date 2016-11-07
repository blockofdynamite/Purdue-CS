package xyz.jhughes.socialmaps.activities.followersSystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import objects.Command;
import objects.Response;
import objects.User;
import xyz.jhughes.socialmaps.R;
import xyz.jhughes.socialmaps.enums.TypeOfUserList;
import xyz.jhughes.socialmaps.fragments.ListOfUsersFragment;
import xyz.jhughes.socialmaps.helpers.Alert;
import xyz.jhughes.socialmaps.server.Server;

public class FollowersActivity extends AppCompatActivity implements ListOfUsersFragment.OnFragmentInteractionListener  {

    private ListView listOfFollowers;
    private ArrayAdapter listOfFollowersAdapter;
    private ArrayList<String> followersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        Fragment newFragment = ListOfUsersFragment.newInstance(TypeOfUserList.FOLLOWERS,
                getSharedPreferences("xyz.jhughes.socialmaps", MODE_PRIVATE).getString("username", null));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.list_of_users_inlay, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(String username) {
        // TODO
    }
}
