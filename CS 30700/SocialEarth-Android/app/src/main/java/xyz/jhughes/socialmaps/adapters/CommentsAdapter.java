package xyz.jhughes.socialmaps.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import objects.Command;
import objects.Comment;
import objects.Response;
import objects.Story;

import objects.User;
import xyz.jhughes.socialmaps.R;
import xyz.jhughes.socialmaps.helpers.Alert;
import xyz.jhughes.socialmaps.server.Server;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.DataViewHolder> {

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        public ImageView profileImage;
        public TextView realName;
        public Button deleteButton;
        public TextView commentText;

        public DataViewHolder(View itemView, Context context) {
            super(itemView);
            this.profileImage = (ImageView) itemView.findViewById(R.id.comment_profile_picture);
            this.realName = (TextView) itemView.findViewById(R.id.comment_real_name);
            this.deleteButton = (Button) itemView.findViewById(R.id.delete_button);
            this.commentText = (TextView) itemView.findViewById(R.id.comment_text);
        }
    }

    private List<Comment> comments;
    private List<User> usersForComments;
    private Story story;
    private Context context;

    public CommentsAdapter(Story story, Context context) {
        this.story = story;
        this.context = context;
        updateComments();
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View commentView = inflater.inflate(R.layout.item_comment, parent, false);
        return new DataViewHolder(commentView, this.context);
    }

    @Override
    public void onBindViewHolder(DataViewHolder dataViewHolder, int position) {
        final Comment comment = this.comments.get(position);
        final User user = this.usersForComments.get(position);
        dataViewHolder.commentText.setText(comment.getCommentText());
        dataViewHolder.realName.setText(user.getRealname());
        Picasso.with(dataViewHolder.profileImage.getContext()).load(user.getProfilePictureUrl()).centerCrop().resize(200, 200).into(dataViewHolder.profileImage);
        SharedPreferences sharedPreferences = context.getSharedPreferences("xyz.jhughes.socialmaps", context.MODE_PRIVATE);
        String currentUsername = sharedPreferences.getString("username", null);
        System.out.println("This user: " + user.getUsername());
        System.out.println("My user: " + currentUsername);
        if (!user.getUsername().equals(currentUsername))
            dataViewHolder.deleteButton.setVisibility(View.INVISIBLE);
        else
            dataViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Command command = new Command(Command.CommandType.DELETE_COMMENT, null, null, null, comment);
                new DeleteCommentTask().execute(command);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(this.comments.size(), this.usersForComments.size());
    }

    public void updateComments() {
        this.comments = new ArrayList<>();
        this.usersForComments = new ArrayList<>();
        Command command = new Command(Command.CommandType.GET_COMMENTS_FOR_STORY, this.story, null, null, null);
        System.out.println("The Commands Id:" + command.getStory().getId());
        new GetCommentsTask().execute(command);
    }

    private void setComments(List<Comment> comments) {
        this.comments = comments;
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            System.out.println("UserID: " + comment.getUserId());
            User user = new User(null, comment.getUserId(), null, null, null, null, null);
            Command command = new Command(Command.CommandType.GET_USER, null, user, null, null);
            new GetUserForCommentTask(i).execute(command);
        }
    }

    private void setUserForComment(User user, int i) {
        System.out.println("User: " + user);
        usersForComments.add(i, user);
        if (!usersForComments.contains(null))
            this.notifyDataSetChanged();
    }

    private class GetCommentsTask extends AsyncTask<Command, Void, Response> {
        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], CommentsAdapter.this.context);
        }

        protected void onPostExecute(Response response) {
            if (response == null)
                Alert.networkError(CommentsAdapter.this.context);
            else
                setComments(response.getListOfComments());
        }
    }

    private class DeleteCommentTask extends AsyncTask<Command, Void, Response> {
        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], CommentsAdapter.this.context);
        }

        protected void onPostExecute(Response response) {
            if (response == null)
                Alert.networkError(CommentsAdapter.this.context);
            else
                updateComments();
        }
    }

    private class GetUserForCommentTask extends AsyncTask<Command, Void, Response> {
        private int i;

        public GetUserForCommentTask(int i) {
            this.i = i;
        }

        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], context);
        }

        protected void onPostExecute(Response response) {
                if (response == null)
                    Alert.networkError(CommentsAdapter.this.context);
                else
                    setUserForComment(response.getUser(), i);
        }
    }
}
