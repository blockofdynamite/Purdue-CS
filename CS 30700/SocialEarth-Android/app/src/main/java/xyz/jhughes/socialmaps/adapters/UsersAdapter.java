package xyz.jhughes.socialmaps.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import objects.Command;
import objects.Response;
import objects.User;
import xyz.jhughes.socialmaps.R;
import xyz.jhughes.socialmaps.activities.followersSystem.FollowersActivity;
import xyz.jhughes.socialmaps.activities.followersSystem.FollowingListActivity;
import xyz.jhughes.socialmaps.activities.followersSystem.UserActivity;
import xyz.jhughes.socialmaps.activities.storySystem.MainActivity;
import xyz.jhughes.socialmaps.enums.TypeOfUserList;
import xyz.jhughes.socialmaps.helpers.Alert;
import xyz.jhughes.socialmaps.server.Server;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.DataViewHolder> {

    public static class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView usernameTextView;
        public TextView realnameTextView;
        public ImageView profileImageView;
        public ImageView gotoMapImageView;
        private Context context;
        private TypeOfUserList type;

        public DataViewHolder(View itemView, Context context, TypeOfUserList type) {
            super(itemView);
            usernameTextView = (TextView) itemView.findViewById(R.id.list_user_name);
            realnameTextView = (TextView) itemView.findViewById(R.id.list_real_name);
            profileImageView = (ImageView) itemView.findViewById(R.id.list_profile_picture);
            gotoMapImageView = (ImageView) itemView.findViewById(R.id.list_goto_map);
            if (type == TypeOfUserList.NEW_FOLLOWER)
                gotoMapImageView.setBackgroundColor(Color.TRANSPARENT);
            this.context = context;
            this.type = type;
            this.gotoMapImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (DataViewHolder.this.type == TypeOfUserList.NEW_FOLLOWER) {
                        SharedPreferences sharedPreferences = DataViewHolder.this.context.getSharedPreferences("xyz.jhughes.socialmaps", DataViewHolder.this.context.MODE_PRIVATE);
                        String username = sharedPreferences.getString("username", null);
                        User thatUser = new User(null, DataViewHolder.this.usernameTextView.getText().toString(), null, null, null, null, null);
                        User myUser = new User(null, username, null, null, null, null, null);
                        Command command = new Command(Command.CommandType.FOLLOW_USER, null, myUser, thatUser, null);
                        new FollowUserTask(DataViewHolder.this.context).execute(command);
                    } else
                        DataViewHolder.this.context.startActivity(new Intent(DataViewHolder.this.context, MainActivity.class).putExtra("usernameForStories", DataViewHolder.this.usernameTextView.getText().toString()));
                }
            });
            itemView.getRootView().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            System.out.println("*************** clicked");
            switch(this.type) {
                case FOLLOWERS:
                case FOLLOWING:
                    this.context.startActivity(new Intent(this.context, UserActivity.class).putExtra("nameToGet", this.usernameTextView.getText()));
                    break;
                case NEW_FOLLOWER:
                    SharedPreferences sharedPreferences = DataViewHolder.this.context.getSharedPreferences("xyz.jhughes.socialmaps", DataViewHolder.this.context.MODE_PRIVATE);
                    String username = sharedPreferences.getString("username", null);
                    User thatUser = new User(null, DataViewHolder.this.usernameTextView.getText().toString(), null, null, null, null, null);
                    User myUser = new User(null, username, null, null, null, null, null);
                    Command command = new Command(Command.CommandType.FOLLOW_USER, null, myUser, thatUser, null);
                    new FollowUserTask(DataViewHolder.this.context).execute(command);
                    break;
            }
        }
    }

    private List<User> users;
    private TypeOfUserList type;
    private Context context;

    public UsersAdapter(List<User> users, TypeOfUserList type, Context context) {
        this.users = users;
        this.type = type;
        this.context = context;

    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_user, parent, false);

        // Return a new holder instance
        DataViewHolder dataViewHolder = new DataViewHolder(contactView, this.context, this.type);
        return dataViewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(DataViewHolder dataViewHolder, int position) {
        // Get the data model based on position
        User user = users.get(position);

        // Set item views based on the data model
        dataViewHolder.usernameTextView.setText(user.getUsername());
        dataViewHolder.realnameTextView.setText(user.getRealname());
        ImageView profileImage = dataViewHolder.profileImageView;
        Picasso.with(profileImage.getContext()).load(user.getProfilePictureUrl()).resize(200,200).into(profileImage);
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return users.size();
    }

    public void clear() {
        this.users = new ArrayList<>();
        notifyDataSetChanged();
    }

    private void setNewListOfUsers(ArrayList<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public void updateBasedOnPrefix(String prefix) {
        User user = new User(null, prefix, null, null, null, null, null);
        Command command = new Command(Command.CommandType.GET_USERS_THAT_BEGIN_WITH_SUBSTRING, null, user, null, null);
        new FindUserNameBasedOnPrefixTask().execute(command);
    }

    private class FindUserNameBasedOnPrefixTask extends AsyncTask<Command, Void, Response> {
        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], UsersAdapter.this.context);
        }

        protected void onPostExecute(Response response) {
            if (response == null)
                Alert.networkError(UsersAdapter.this.context);
            else if(!response.isBool())
                Alert.generalError(UsersAdapter.this.context);
            else {
                System.out.println(response.getListOfUsers().size());
                UsersAdapter.this.setNewListOfUsers(response.getListOfUsers());
                // context.finish();
            }
        }
    }
}

class FollowUserTask extends AsyncTask<Command, Void, Response> {
    private Context c;
    FollowUserTask(Context c) {
        this.c = c;
    }

    protected Response doInBackground(Command... commands) {
        return Server.sendCommandToServer(commands[0], c);
    }

    protected void onPostExecute(Response response) {
        if (response == null)
            Alert.networkError(c);
        else if(!response.isBool())
            Alert.generalError(c);
        else {
           c.startActivity(new Intent(c, FollowingListActivity.class));
        }
    }
}
