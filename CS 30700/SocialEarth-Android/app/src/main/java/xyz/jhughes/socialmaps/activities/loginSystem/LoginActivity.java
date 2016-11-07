package xyz.jhughes.socialmaps.activities.loginSystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import objects.Command;
import objects.Response;
import objects.User;
import xyz.jhughes.socialmaps.R;
import xyz.jhughes.socialmaps.activities.storySystem.MainActivity;
import xyz.jhughes.socialmaps.helpers.Alert;
import xyz.jhughes.socialmaps.helpers.PasswordCheck;
import xyz.jhughes.socialmaps.helpers.UsernameCheck;
import xyz.jhughes.socialmaps.server.Server;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "M1b5CCpyjRB8A9tcHkHEujmex";
    private static final String TWITTER_SECRET = "YqQUs0a1Mmrp2tSCFH47MQCC21fgxXmJfMW73kx981ucorgiXI";

    private TwitterAuthConfig authConfig;
    private GoogleApiClient mGoogleApiClient;

    @Bind(R.id.username)
    EditText usernameField;
    @Bind(R.id.password)
    EditText passwordField;
    @Bind(R.id.RegisterButton)
    Button registerButton;
    @Bind(R.id.login_button)
    Button loginButton;
    @Bind(R.id.gp_button)
    SignInButton gpButton;
    @Bind(R.id.twitter_login_button)
    TwitterLoginButton twitterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Needed. DO NOT DELETE
        MultiDex.install(this);

        authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setTheme(R.style.LoginTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        ButterKnife.bind(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(null, usernameField.getText().toString(), null, null, passwordField.getText().toString(), null, null);
                Command command = new Command(Command.CommandType.LOGIN, null, user, null, null);
                LoginTask task = new LoginTask(LoginActivity.this.getApplicationContext(), usernameField.getText().toString(), passwordField.getText().toString());
                task.execute(command);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRegisterAccount(v);
            }
        });

        twitterButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                //String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                twitterLogin(session);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId().requestProfile().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        for (int i = 0; i < gpButton.getChildCount(); i++) {
            View v = gpButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText("Log in with Google");
            }
        }

        gpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, 9001);
            }
        });

        if (getSharedPreferences("xyz.jhughes.socialmaps", MODE_PRIVATE).contains("username") &&
                getSharedPreferences("xyz.jhughes.socialmaps", MODE_PRIVATE).contains("username")) {

            startActivity(new Intent(getApplicationContext(), MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void onClickRegisterAccount(View v) {
        if (!PasswordCheck.passwordIsStrong(passwordField.getText().toString())) {
            passwordField.setTextColor(Color.RED);
            usernameField.setTextColor(Color.BLACK);
            Alert.createAlertDialog("Password not Strong", getResources().getString(R.string.password_requirements), this);
        } else if (!UsernameCheck.usernameIsValid(usernameField.getText().toString())) {
            passwordField.setTextColor(Color.BLACK);
            usernameField.setTextColor(Color.RED);
            Alert.createAlertDialog("Username is not valid", getResources().getString(R.string.username_requirements), this);
        } else {
            System.out.println(passwordField.getText().toString());
            String username = usernameField.getText().toString();
            User user = new User(null, username, null, null, null, null, null);
            Command command = new Command(Command.CommandType.ACCOUNT_EXISTS, null, user, null, null);
            new UserExistsTask().execute(command);
        }
    }

    public void usernameAlreadyExists() {
        passwordField.setTextColor(Color.BLACK);
        usernameField.setTextColor(Color.RED);
        Alert.createAlertDialog("Username already exists", "Please specify a more unique username", this);
    }

    private void invalidLogin() {
        passwordField.setTextColor(Color.RED);
        usernameField.setTextColor(Color.RED);
        Alert.createAlertDialog("Error logging in", "Username or Password is incorrect", this);
    }

    private String getName(TwitterSession session) {
        final String[] name = new String[1];
        name[0] = null;

        Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false, new Callback<com.twitter.sdk.android.core.models.User>() {

            @Override
            public void success(Result<com.twitter.sdk.android.core.models.User> userResult) {
                com.twitter.sdk.android.core.models.User user = userResult.data;
                name[0] = user.name;
            }

            @Override
            public void failure(TwitterException e) {

            }

        });
        return name[0];
    }

    private void twitterLogin(TwitterSession session) {
        System.out.println("Login");

        final String[] imageUrl = new String[1];

        Twitter.getApiClient().getAccountService().verifyCredentials(true, false, new Callback<com.twitter.sdk.android.core.models.User>() {
                    @Override
                    public void success(Result<com.twitter.sdk.android.core.models.User> result) {
                        imageUrl[0] = result.data.profileImageUrl;
                        System.out.println(result.data.profileBackgroundImageUrl);
                    }

                    @Override
                    public void failure(TwitterException e) {

                    }
                });

        User user = new User(null, session.getUserName(), getName(session), null, Long.toString(session.getUserId()), imageUrl[0], null);
        Command command = new Command(Command.CommandType.LOGIN, null, user, null, null);

        TwitterAccountLoginTask task = new TwitterAccountLoginTask(session);
        task.execute(command);
    }

    private void twitterCreateAccount(TwitterSession session) {

        final String[] imageUrl = new String[1];

        Twitter.getApiClient().getAccountService().verifyCredentials(true, false, new Callback<com.twitter.sdk.android.core.models.User>() {
            @Override
            public void success(Result<com.twitter.sdk.android.core.models.User> result) {
                imageUrl[0] = result.data.profileImageUrl;
            }

            @Override
            public void failure(TwitterException e) {

            }
        });

        System.out.println("Create");
        User user = new User(null, session.getUserName(), session.getUserName(), null, Long.toString(session.getUserId()), imageUrl[0], null);
        Command command = new Command(Command.CommandType.CREATE_ACCOUNT, null, user, null, null);

        TwitterAccountLoginTask task = new TwitterAccountLoginTask(session);
        task.execute(command);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        if (requestCode == 9001) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            googleSignIn(result);
        } else {
            twitterButton.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void googleSignIn(GoogleSignInResult result) throws NullPointerException {
        System.out.println("Login");

        if (result == null || result.getSignInAccount() == null) {
            return;
        }

        User user = new User(null, result.getSignInAccount().getDisplayName(), result.getSignInAccount().getDisplayName(), null, result.getSignInAccount().getId(), null, null);
        Command command = new Command(Command.CommandType.LOGIN, null, user, null, null);

        GoogleLoginTask googleLoginTask = new GoogleLoginTask(result);
        googleLoginTask.execute(command);
    }

    private void googleCreateAccount(GoogleSignInResult result) throws NullPointerException {
        System.out.println("Create");

        if (result == null || result.getSignInAccount() == null) {
            return;
        }

        User user = new User(null, result.getSignInAccount().getDisplayName(), result.getSignInAccount().getDisplayName(), null, result.getSignInAccount().getId(), null, null);
        Command command = new Command(Command.CommandType.CREATE_ACCOUNT, null, user, null, null);

        GoogleLoginTask googleLoginTask = new GoogleLoginTask(result);
        googleLoginTask.execute(command);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Google Sign In Failed", Toast.LENGTH_LONG).show();
    }

    class LoginTask extends AsyncTask<Command, Void, Response> {

        private Context context;
        private String username;
        private String password;

        public LoginTask(Context c, String user, String password) {
            this.context = c;
            this.username = user;
            this.password = password;
        }

        @Override
        protected Response doInBackground(Command... params) {
            return Server.sendCommandToServer(params[0], context);
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response == null)
                Alert.networkError(context);
            else if (!response.isBool())
                invalidLogin();
            else {
                SharedPreferences sharedPreferences = context.getSharedPreferences("xyz.jhughes.socialmaps", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("username", username).apply();
                sharedPreferences.edit().putString("password", password).apply();
                sharedPreferences.edit().putBoolean("loggedIn", true).apply();
                sharedPreferences.edit().putString("userID", response.getUser().getId()).apply();
                context.startActivity(new Intent(context.getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }
    }

    class UserExistsTask extends AsyncTask<Command, Void, Response> {

        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], getApplicationContext());
        }

        protected void onPostExecute(Response response) {
            if (response == null)
                Alert.networkError(getApplicationContext());
            else if (response.isBool())
                usernameAlreadyExists();
            else {
                passwordField.setTextColor(Color.BLACK);
                usernameField.setTextColor(Color.BLACK);
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                i.putExtra("username", usernameField.getText().toString());
                i.putExtra("establishedPassword", passwordField.getText().toString());
                startActivity(i);
            }
        }
    }

    class TwitterAccountLoginTask extends AsyncTask<Command, Void, Response> {

        private TwitterSession session;

        public TwitterAccountLoginTask(TwitterSession session) {
            this.session = session;
        }

        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], getApplicationContext());
        }

        protected void onPostExecute(Response response) {
            if (response == null)
                twitterCreateAccount(session);
            else if (!response.isBool()) {
                twitterCreateAccount(session);
            }
            else {
                SharedPreferences sharedPreferences = getSharedPreferences("xyz.jhughes.socialmaps", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("username", session.getUserName()).apply();
                sharedPreferences.edit().putString("password", Long.toString(session.getUserId())).apply();
                sharedPreferences.edit().putBoolean("loggedIn", true).apply();
                startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }
    }

    class GoogleLoginTask extends AsyncTask<Command, Void, Response> {

        private GoogleSignInResult result;

        public GoogleLoginTask(GoogleSignInResult result) {
            this.result = result;
        }

        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], getApplicationContext());
        }

        protected void onPostExecute(Response response) {
            if (response == null)
                googleCreateAccount(result);
            if (!response.isBool()) {
                googleCreateAccount(result);
            }
            else {
                SharedPreferences sharedPreferences = getSharedPreferences("xyz.jhughes.socialmaps", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("username", result.getSignInAccount().getDisplayName()).apply();
                sharedPreferences.edit().putString("password", result.getSignInAccount().getId()).apply();
                sharedPreferences.edit().putBoolean("loggedIn", true).apply();
                //sharedPreferences.edit().putString("userID", response.getUser().getId()).apply();
                startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }
    }

}