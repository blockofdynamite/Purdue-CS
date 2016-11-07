package xyz.jhughes.socialmaps.activities.followersSystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import objects.Command;
import objects.Response;
import objects.User;
import xyz.jhughes.socialmaps.R;
import xyz.jhughes.socialmaps.activities.storySystem.MainActivity;
import xyz.jhughes.socialmaps.enums.TypeOfUserList;
import xyz.jhughes.socialmaps.fragments.ListOfUsersFragment;
import xyz.jhughes.socialmaps.helpers.Alert;
import xyz.jhughes.socialmaps.server.Server;

public class AddNewFollowerActivity extends AppCompatActivity implements ListOfUsersFragment.OnFragmentInteractionListener {
    private EditText editTextNames;
    private ListOfUsersFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_follower);

        fragment = ListOfUsersFragment.newInstance(TypeOfUserList.NEW_FOLLOWER,
                getSharedPreferences("xyz.jhughes.socialmaps", MODE_PRIVATE).getString("username", null));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.list_of_possible_users, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        //getting the button editText and listView
        editTextNames = (EditText) findViewById(R.id.edit_text_search_name);
        editTextNames.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchName = editTextNames.getText().toString();
                if(count  < 3) {
                    fragment.clear();
                } else {
                    fragment.updateListOfUsers(searchName);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onFragmentInteraction(String username) {

    }
}