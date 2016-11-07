package xyz.jhughes.socialmaps.activities.followersSystem;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import xyz.jhughes.socialmaps.R;
import xyz.jhughes.socialmaps.activities.storySystem.MainActivity;
import xyz.jhughes.socialmaps.enums.TypeOfUserList;
import xyz.jhughes.socialmaps.fragments.ListOfUsersFragment;

public class FollowingListActivity extends AppCompatActivity implements ListOfUsersFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.myFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddNewFollowerActivity.class));
            }
        });

        Fragment newFragment = ListOfUsersFragment.newInstance(TypeOfUserList.FOLLOWING,
                getSharedPreferences("xyz.jhughes.socialmaps", MODE_PRIVATE).getString("username", null));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.list_of_users_linlay, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(String username) {
        // TODO
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
