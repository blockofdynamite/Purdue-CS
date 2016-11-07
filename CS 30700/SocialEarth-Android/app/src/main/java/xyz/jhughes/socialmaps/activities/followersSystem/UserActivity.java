package xyz.jhughes.socialmaps.activities.followersSystem;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import objects.Command;
import objects.Response;
import objects.User;
import xyz.jhughes.socialmaps.R;
import xyz.jhughes.socialmaps.activities.storySystem.MainActivity;
import xyz.jhughes.socialmaps.helpers.Alert;
import xyz.jhughes.socialmaps.server.PhotosServer;
import xyz.jhughes.socialmaps.server.Server;

public class UserActivity extends AppCompatActivity {

    @Bind(R.id.profile_picture) ImageView profilePicture;
    @Bind(R.id.profile_name) TextView name;
    @Bind(R.id.bioText) TextView bio;
    @Bind(R.id.edit_bio) TextView specialAction; // Either edit profile info or follow user / unfollow user
    private String photoUrl;
    private boolean weAreThisUser;
    private boolean weFollowThisUser;
    private String userString;

    private ProgressDialog progressDialog;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static int RESULT_LOAD_IMAGE = 2;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ButterKnife.bind(this);

        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            System.out.println("action bar null");
        }

        try {
            userString = getIntent().getExtras().getString("nameToGet");
        } catch (NullPointerException e) {
            userString = getSharedPreferences("xyz.jhughes.socialmaps", MODE_PRIVATE).getString("username", null);
        }


        SharedPreferences sharedPreferences = getSharedPreferences("xyz.jhughes.socialmaps", MODE_PRIVATE);
        String currentUsername = sharedPreferences.getString("username", null);
        if (currentUsername.equals(userString)) {
            weAreThisUser = true;
        } else {
            weAreThisUser = false;
            User user1 = new User(null, currentUsername, null, null, null, null, null);
            User user2 = new User(null, userString, null, null, null, null, null);
            Command command = new Command(Command.CommandType.USER_FOLLOWS_USER, null, user1, user2, null);
            new DetermineIfWeFollowThisUserTask().execute(command);
        }

        if (userString == null) {
            userString = getSharedPreferences("xyz.jhughes.socialmaps", MODE_PRIVATE).getString("username", null);
        }

        User userToGet = new User(null, userString, null, null, null, null, null);
        Command toSend = new Command(Command.CommandType.GET_USER, null, userToGet, null, null);

        new LoadTaskUser(userString).execute(toSend);

        specialAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserActivity.this.weAreThisUser)
                    updateBio();
                else {
                    SharedPreferences sharedPreferences = getSharedPreferences("xyz.jhughes.socialmaps", MODE_PRIVATE);
                    String currentUsername = sharedPreferences.getString("username", null);
                    if (weFollowThisUser) {
                        // unfollow
                        User user1 = new User(null, currentUsername, null, null, null, null, null);
                        User user2 = new User(null, UserActivity.this.userString, null, null, null, null, null);
                        Command command = new Command(Command.CommandType.UNFOLLOW_USER, null, user1, user2, null);
                        new SendObjectToServer().execute(command);
                        // set text to follow
                        UserActivity.this.specialAction.setText("Follow");
                    } else {
                        // follow
                        User user1 = new User(null, currentUsername, null, null, null, null, null);
                        User user2 = new User(null, UserActivity.this.userString, null, null, null, null, null);
                        Command command = new Command(Command.CommandType.FOLLOW_USER, null, user1, user2, null);
                        new SendObjectToServer().execute(command);
                        // set text to
                        UserActivity.this.specialAction.setText("Unfollow");
                    }
                }
            }
        });

        Button button = (Button) findViewById(R.id.viewMapButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("usernameForStories", UserActivity.this.user.getUsername()));
                } catch (Exception e) {
                    Alert.networkError(UserActivity.this);
                }
            }
        });

        name.setText(userString);

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPic(v);
            }
        });
    }

    public void onClickPic(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
            pickPhoto();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            pickPhoto();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1911);
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED) {
                pickPhoto();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void pickPhoto() {
        android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(this);
        //  LayoutInflater inflater = this.getLayoutInflater();
        b.setCancelable(true);
        b.setTitle("Attach Photos");
        b.setMessage("");
        // b.setView(v);
        b.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri  = Uri.parse("file:///sdcard/photo.jpg");
                photo.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);

                startActivityForResult(photo, REQUEST_IMAGE_CAPTURE);
            }
        });

        b.setNegativeButton("Library", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent choosePictureIntent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(choosePictureIntent, RESULT_LOAD_IMAGE);
            }
        });
        b.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //  b.setView(inflater.inflate(R.layout.dialog_edititem_photo, null));
        b.create().show();
    }

    private void photoLoadDialog() {
        progressDialog = new ProgressDialog(UserActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // this is for the taking camera pic
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                photoLoadDialog();
                PostPhotoToCloudinary post = new PostPhotoToCloudinary();
                post.execute(getPath(Uri.parse("file:///sdcard/photo.jpg")));
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                photoLoadDialog();
                PostPhotoToCloudinary post = new PostPhotoToCloudinary();
                post.execute(getPath(Uri.parse("file:///sdcard/photo.jpg")));
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }

        //this is for picture image from the Gallery

        if (requestCode == RESULT_LOAD_IMAGE){
            PostPhotoToCloudinary post = new PostPhotoToCloudinary();
            post.execute(getPath(data.getData()));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        // TODO: get MainActivity to display the same data as before
        startActivity(intent);
    }

    private void setEverything() {
        if (user.getProfilePictureUrl() != null) {
            Picasso.with(UserActivity.this).load(user.getProfilePictureUrl()).centerCrop().resize(300,300).into(profilePicture);
        }
        if (user.getBio() != null) {
            bio.setText(user.getBio());
        }
    }

    private void updateBio() {
        final EditText input = new EditText(this);
        input.setText(user.getBio());

        final AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle("Update Bio")
                .setMessage("Update your bio below...")
                .setView(input)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        User userToGet = new User(null, user.getUsername(), null, null, null, null, input.getText().toString());
                        Command toSend = new Command(Command.CommandType.UPDATE_BIO_AND_PROFILE_PICTURE, null, userToGet, null, null);
                        new UpdateBio(input.getText().toString()).execute(toSend);
                        bio.setText(input.getText().toString());
                    }
                })
                .show();
    }

    public String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    class LoadTaskUser extends AsyncTask<Command, Void, Response> {

        private String username;

        public LoadTaskUser(String username) {
            this.username = username;
        }

        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], getApplicationContext());
        }

        protected void onPostExecute(Response response) {
            if (response == null)
                Alert.generalError(UserActivity.this);
            else {
                user = response.getUser();
                if (user != null)
                    setEverything();
                else
                    System.out.println("Null user!");
            }
        }
    }

    class DetermineIfWeFollowThisUserTask extends AsyncTask<Command, Void, Response> {
        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], getApplicationContext());
        }

        protected void onPostExecute(Response response) {
            if (response == null)
                Alert.generalError(UserActivity.this);
            else {
                if (response.isBool()) {
                    UserActivity.this.weFollowThisUser = true;
                    UserActivity.this.specialAction.setText("Unfollow");
                } else {
                    UserActivity.this.weFollowThisUser = false;
                    UserActivity.this.specialAction.setText("Follow");
                }
            }
        }
    }

    class UpdateBio extends AsyncTask<Command, Void, Response> {

        private String bio;

        public UpdateBio(String bio) {
            this.bio = bio;
        }

        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], getApplicationContext());
        }

        protected void onPostExecute(Response response) {
            try {
                UserActivity.this.bio.setText(response.getUser().getBio());
            } catch (Exception e) {
                System.out.println("null");
            }
        }
    }

    class SendObjectToServer extends AsyncTask<Command, Void, Response> {
        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], getApplicationContext());
        }

        protected void onPostExecute(Response response) {
            if (response == null)
                Alert.generalError(UserActivity.this);
        }
    }

    private class PostPhotoToCloudinary extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return PhotosServer.pushPhotoToServer(params[0]);
        }

        protected void onPostExecute(String s) {
            progressDialog.cancel();
            if (s == null) {
                Alert.createAlertDialog("Upload Error", "The image upload failed!", UserActivity.this);
                return;
            }
            Picasso.with(UserActivity.this).load(s).centerCrop().resize(200,200).into(profilePicture);

            User userToGet = new User(null, user.getUsername(), null, null, null, s, bio.getText().toString());
            Command toSend = new Command(Command.CommandType.UPDATE_BIO_AND_PROFILE_PICTURE, null, userToGet, null, null);
            new UpdateBio(bio.getText().toString()).execute(toSend);

            user.setProfilePictureUrl(s);
        }
    }
}
