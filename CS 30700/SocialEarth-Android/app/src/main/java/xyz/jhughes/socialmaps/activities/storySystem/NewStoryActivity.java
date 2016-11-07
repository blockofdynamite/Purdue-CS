package xyz.jhughes.socialmaps.activities.storySystem;

import android.Manifest;
import android.app.AlertDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.Date;

import objects.Command;
import objects.Response;
import objects.Story;
import objects.User;
import xyz.jhughes.socialmaps.R;
import xyz.jhughes.socialmaps.helpers.Alert;
import xyz.jhughes.socialmaps.server.PhotosServer;
import xyz.jhughes.socialmaps.server.Server;

public class NewStoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int REQUEST_PLACE_PICKER = 1000;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static int RESULT_LOAD_IMAGE = 2;

    private ArrayList<String> links = new ArrayList<>();
    private ProgressDialog progressDialog;

    private String username;
    private double latitude;
    private double longitude;

    private EditText titleText;
    private EditText storyText;
    private ImageView mImageView;
    private Spinner spinner;
    private Story.PrivacySetting priv;
    private String coverPhoto;
    private boolean isForCoverPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("xyz.jhughes.socialmaps", MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);
        setContentView(R.layout.activity_location);
        titleText = (EditText) findViewById(R.id.editText_title);
        storyText = (EditText) findViewById(R.id.editText_story);
        mImageView = (ImageView) findViewById(R.id.imagePic);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.permission_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        priv = Story.PrivacySetting.PUBILC;
        coverPhoto = null;
        isForCoverPhoto = false;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        switch(pos) {
            case 0:
                priv = Story.PrivacySetting.PUBILC;
                break;
            case 1:
                priv = Story.PrivacySetting.MUTUAL_FOLLOWERS;
                break;
            case 2:
                priv = Story.PrivacySetting.ONLY_ME;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //this is for the google place picker
        if (requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                this.latitude = place.getLatLng().latitude;
                this.longitude = place.getLatLng().longitude;
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Alert.generalError(getApplicationContext());
            } else if (resultCode == RESULT_CANCELED) { }
        }

        // this is for the taking camera pic
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Uri selectedImage = Uri.parse("file:///sdcard/photo.jpg");
                String s = getPath(selectedImage);
                System.out.println(s);
                progressDialog = new ProgressDialog(NewStoryActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Uploading, please wait...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                if (this.isForCoverPhoto) {
                    new PostCoverPhotoTask().execute(s);
                } else {
                    new PostPhotoTask().execute(s);
                }
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Uri selectedImage = Uri.parse("file:///sdcard/photo.jpg");
                String s = getPath(selectedImage);
                System.out.println(s);
                progressDialog = new ProgressDialog(NewStoryActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Uploading, please wait...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }

        //this is for picture image from the Gallery

        if (requestCode == RESULT_LOAD_IMAGE){
            Uri selectedImage = data.getData();
            String s = getPath(selectedImage);
            System.out.println(s);
            progressDialog = new ProgressDialog(NewStoryActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Uploading, please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            if (this.isForCoverPhoto) {
                new PostCoverPhotoTask().execute(s);
            } else {
                new PostPhotoTask().execute(s);
            }
        }
    }

    public void onClickPost(View view) {
        String title = this.titleText.getText().toString();
        String storyText = this.storyText.getText().toString();
        Date date = new Date();
        User user = new User(getSharedPreferences("xyz.jhughes.socialmaps", MODE_PRIVATE).getString("username", null), username, null, null, null, null, null);
        System.out.println(priv);
        Story story = new Story(null, this.latitude, this.longitude, title, this.username, storyText, date, this.coverPhoto, links, this.priv);
        Command command = new Command(Command.CommandType.POST_STORY, story, user, null, null);
        new AddStoryTask().execute(command);
    }

    public void onClickPic(View view) {
        this.isForCoverPhoto = false;
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
    /*
        For posting cover photo
    */


    public void onClickPostCover(View view) {
        this.isForCoverPhoto = true;
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
        AlertDialog.Builder b = new AlertDialog.Builder(this);
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

    public void onClickLocation(View view) {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), REQUEST_PLACE_PICKER);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(), 0).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            Alert.generalError(getApplicationContext());
        }
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

    private class AddStoryTask extends AsyncTask<Command, Void, Response> {

        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], getApplicationContext());
        }

        protected void onPostExecute(Response response) {
            if (response == null)
                Alert.networkError(getApplicationContext());
            else if(!response.isBool())
                Alert.generalError(getApplicationContext());
            else {
                Intent intent = new Intent(getApplicationContext(), StoryActivity.class);
                intent.putExtra("storyid", response.getStory().getId());
                startActivity(intent);
            }
        }
    }

    private class PostPhotoTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return PhotosServer.pushPhotoToServer(params[0]);
        }

        protected void onPostExecute(String s) {
            progressDialog.cancel();
            if (s == null) {
                Alert.createAlertDialog("Upload Error", "The image upload failed!", NewStoryActivity.this);
                return;
            }
            links.add(s);
            System.out.println("Regular photo added");
            System.out.println(s);
        }
    }

    private class PostCoverPhotoTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return PhotosServer.pushPhotoToServer(params[0]);
        }

        protected void onPostExecute(String s) {
            System.out.println("Cover photo added");
            progressDialog.cancel();
            if (s == null) {
                Alert.createAlertDialog("Upload Error", "The image upload failed!", NewStoryActivity.this);
                return;
            }
            coverPhoto = s;
        }
    }
}
