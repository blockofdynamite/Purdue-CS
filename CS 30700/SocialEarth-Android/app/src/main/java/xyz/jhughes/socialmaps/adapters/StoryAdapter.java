package xyz.jhughes.socialmaps.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;

import objects.Command;
import objects.Comment;
import objects.Response;
import objects.Story;
import objects.User;
import xyz.jhughes.socialmaps.R;
import xyz.jhughes.socialmaps.activities.storySystem.MainActivity;
import xyz.jhughes.socialmaps.helpers.Alert;
import xyz.jhughes.socialmaps.server.Server;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private Story story;
    private int[] mDataSetTypes;

    private Context c;

    private String storyId;

    public static final int TITLE = 0;
    public static final int STORY = 1;
    public static final int PHOTO = 2;
    public static final int COMMENTS = 3;

    private TitleViewHolder title = null;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class TitleViewHolder extends ViewHolder {
        ImageView cover;
        TextView storyName;
        TextView date;
        TextView username;
        TextView likes;
        ImageButton imageButton;
        Button delete;

        int likeCount = 0;
        boolean likesStory = false;

        public TitleViewHolder(View v) {
            super(v);
            cover = (ImageView) v.findViewById(R.id.cover);
            storyName = (TextView) v.findViewById(R.id.storyTitle);
            date = (TextView) v.findViewById(R.id.date);
            username = (TextView) v.findViewById(R.id.username);
            imageButton = (ImageButton) v.findViewById(R.id.likeButton);
            likes = (TextView) v.findViewById(R.id.likeCount);
            delete = (Button) v.findViewById(R.id.delet_story_button);
        }
    }

    public class TextViewHolder extends ViewHolder {
        TextView textView;

        public TextViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.story_text_view);
        }
    }

    public class PhotoViewHolder extends ViewHolder {
        ImageView imageView;

        public PhotoViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.image_story);
        }
    }

    public class CommentViewHolder extends ViewHolder {
        RecyclerView recyclerView;
        EditText editText;
        Button button;
        CommentsAdapter adapter;

        public CommentViewHolder(View v, Story story) {
            super(v);
            this.recyclerView = (RecyclerView) v.findViewById(R.id.rvComments);
            this.editText = (EditText) v.findViewById(R.id.comment_edit);
            this.button = (Button) v.findViewById(R.id.submit_button);
            this.adapter = new CommentsAdapter(story, v.getContext());
        }
    }

    public StoryAdapter(Story story, Context c) {
        this.story = story;
        this.storyId = story.getId();

        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(TITLE); arrayList.add(STORY);
        if (story.getPhotos() != null) {
            for (int i = 0; i < story.getPhotos().size(); i++) {
                arrayList.add(PHOTO);
            }
        }
        arrayList.add(COMMENTS);
        mDataSetTypes = new int[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            mDataSetTypes[i] = arrayList.get(i);
        }

        this.c = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case TITLE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_title, parent, false);
                return new TitleViewHolder(v);
            case STORY:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_story_text, parent, false);
                return new TextViewHolder(v);
            case PHOTO:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_photo, parent, false);
                return new PhotoViewHolder(v);
            case COMMENTS:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_comments, parent, false);
                return new CommentViewHolder(v, story);
            default:
                throw new RuntimeException("Error 666: Unknown card identifier.");
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        switch(holder.getItemViewType()) {
            case TITLE:
                TitleViewHolder title = (TitleViewHolder) holder;
                initTitleCard(title);
                this.title = title;
                break;
            case STORY:
                TextViewHolder storyText = (TextViewHolder) holder;
                storyText.textView.setText(story.getDescription());
                break;
            case PHOTO:
                PhotoViewHolder photo = (PhotoViewHolder) holder;
                Glide.with(photo.itemView.getContext()).load(story.getPhotos().get(position - 2)).placeholder(R.drawable.loading_spinner).into(photo.imageView);
                break;
            case COMMENTS:
                final CommentViewHolder comments = (CommentViewHolder) holder;
                comments.recyclerView.setAdapter(comments.adapter);
                comments.recyclerView.setLayoutManager(new LinearLayoutManager(comments.recyclerView.getContext())); // Maybe the issue if you have context issues
                comments.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userId = c.getSharedPreferences("xyz.jhughes.socialmaps", Context.MODE_PRIVATE).getString("username", null);
                        String storyId = story.getId();
                        String commentText = comments.editText.getText().toString();
                        Comment comment = new Comment(null, userId, storyId, commentText, new Date(), comments.adapter.getItemCount());
                        Command command = new Command(Command.CommandType.POST_COMMENT_IN_STORY, null, null, null, comment);
                        new PostCommentTask(comments.adapter).execute(command);
                        comments.editText.setText("");
                    }
                });
                break;
            default:
                throw new RuntimeException("Error 666: Unknown card identifier.");
        }
    }

    private void initTitleCard(TitleViewHolder holder) {
        holder.storyName.setText(story.getStoryName());

        Date dateObject = story.getDateRange();
        String dateString = dateObject.toString();

        holder.date.setText(dateString);

        holder.username.setText(story.getUser());

        if (story.getCoverPhotoUrl() != null) {
            System.out.println("In here!");
            Glide.with(holder.itemView.getContext()).load(story.getCoverPhotoUrl()).placeholder(R.drawable.loading_spinner_small).into(holder.cover);
        }

        final TitleViewHolder[] titles = new TitleViewHolder[1];
        titles[0] = holder;

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("********** Storyid = " + storyId);
                if (titles[0].likesStory) {
                    SharedPreferences sharedPreferences = c.getSharedPreferences("xyz.jhughes.socialmaps", Context.MODE_PRIVATE);
                    User user = new User("id", sharedPreferences.getString("username", null), null, null, null, null, null);
                    Story story = new Story(storyId, 0, 0, null, user.getUsername(), null, null, null, null, null);
                    Command command = new Command(Command.CommandType.UNLIKE_STORY, story, user, null, null);
                    new UnlikeStoryTask().execute(command);
                    setNumberOfLikes(--titles[0].likeCount);
                    titles[0].likesStory = false;
                    titles[0].imageButton.setImageResource(R.drawable.like_outline);
                    titles[0].imageButton.refreshDrawableState();
                } else {
                    SharedPreferences sharedPreferences = c.getSharedPreferences("xyz.jhughes.socialmaps", Context.MODE_PRIVATE);
                    User user = new User("id", sharedPreferences.getString("username", null), null, null, null, null, null);
                    Story story = new Story(storyId, 0, 0, null, user.getUsername(), null, null, null, null, null);
                    Command command = new Command(Command.CommandType.LIKE_STORY, story, user, null, null);
                    new LikeStoryTask().execute(command);
                    setNumberOfLikes(++titles[0].likeCount);
                    titles[0].likesStory = true;
                    titles[0].imageButton.setImageResource(R.drawable.filled_like);
                    titles[0].imageButton.refreshDrawableState();
                }
            }
        });
        SharedPreferences sharedPreferences = c.getSharedPreferences("xyz.jhughes.socialmaps", Context.MODE_PRIVATE);
        User userr = new User("id", sharedPreferences.getString("username", null), null, null, null, null, null);
        if (story.getUser().equals(userr.getUsername())) {
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = c.getSharedPreferences("xyz.jhughes.socialmaps", Context.MODE_PRIVATE);
                    User user = new User("id", sharedPreferences.getString("username", null), null, null, null, null, null);
                    System.out.println("******************* storyId = " + storyId);
                    Story story = new Story(storyId, 0, 0, null, user.getUsername(), null, null, null, null, null);
                    Command command = new Command(Command.CommandType.DELETE_STORY, story, user, null, null);
                    new DeleteStoryTask().execute(command);
                }
            });
        } else {
            holder.delete.setVisibility(View.GONE);
        }
        User user = new User("id", c.getSharedPreferences("xyz.jhughes.socialmaps", Context.MODE_PRIVATE).getString("username", null), null, null, null, null, null);
        Story story = new Story(storyId, 0, 0, null, user.getUsername(), null, null, null, null, null);
        Command command2 = new Command(Command.CommandType.USER_LIKES_STORY, story, user, null, null);
        Command command3 = new Command(Command.CommandType.GET_LIKE_COUNT, story, user, null, null);

        new GetUserLikesStoryTask().execute(command2);
        new GetStoryLikeCountTask().execute(command3);
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSetTypes[position];
    }

    @Override
    public int getItemCount() {
        return mDataSetTypes.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setNumberOfLikes(int likes) {
        if (title == null) {
            System.out.println("null");
        }
        title.likeCount = likes;
        title.likes.setText(Integer.toString(likes));
    }

    public void userLikesStory() {
        title.imageButton.setImageResource(R.drawable.filled_like);
        title.imageButton.refreshDrawableState();
        title.likesStory = true;
    }

    public class DeleteStoryTask extends AsyncTask<Command, Void, Response> {

        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], c);
        }

        protected void onPostExecute(Response response) {
            c.startActivity(new Intent(c, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    public class LikeStoryTask extends AsyncTask<Command, Void, Response> {

        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], c);
        }

        protected void onPostExecute(Response response) {
            if (response == null)
                Alert.networkError(c);
        }
    }

    public class UnlikeStoryTask extends AsyncTask<Command, Void, Response> {

        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], c);
        }

        protected void onPostExecute(Response response) {
            if (response == null)
                Alert.networkError(c);
        }
    }

    public class GetUserLikesStoryTask extends AsyncTask<Command, Void, Response> {

        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], c);
        }

        protected void onPostExecute(Response response) {
            if (response == null)
                Alert.networkError(c);
            else if (response.isBool())
                userLikesStory();
        }
    }

    public class GetStoryLikeCountTask extends AsyncTask<Command, Void, Response> {

        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], c);
        }

        protected void onPostExecute(Response response) {
            if (response == null)
                Alert.networkError(c);
            else if (response.isBool())
                setNumberOfLikes(response.getInteger());
        }
    }

    public class PostCommentTask extends AsyncTask<Command, Void, Response> {
        private CommentsAdapter adapter;

        public PostCommentTask(CommentsAdapter adapter) {
            this.adapter = adapter;
        }

        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], c);
        }

        protected void onPostExecute(Response response) {
            if (response == null)
                Alert.networkError(c);
            else {
                adapter.updateComments();
            }
        }
    }
}
