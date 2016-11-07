package xyz.jhughes.socialmaps.activities.loginSystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import objects.Command;
import objects.Response;
import objects.User;
import xyz.jhughes.socialmaps.R;
import xyz.jhughes.socialmaps.activities.storySystem.MainActivity;
import xyz.jhughes.socialmaps.helpers.Alert;
import xyz.jhughes.socialmaps.helpers.EmailCheck;
import xyz.jhughes.socialmaps.server.Server;

public class RegisterActivity extends AppCompatActivity {

    private String username;
    private String establishedPassword;

    @Bind(R.id.realName) EditText realNameField;
    @Bind(R.id.confirmPassword) EditText passwordField;
    @Bind(R.id.emailField) EditText emailField;
    @Bind(R.id.checkBox) CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        Bundle extras = getIntent().getExtras();
        this.username = extras.getString("username");
        this.establishedPassword = extras.getString("establishedPassword");

        checkBox.setText(Html.fromHtml(getString(R.string.TOSHTML)));
        checkBox.setMovementMethod(LinkMovementMethod.getInstance());

        Button finishButton = (Button) findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFinish();
            }
        });
    }

    private void onClickFinish() {
        if (!passwordField.getText().toString().equals(establishedPassword)) {
            passwordField.setTextColor(Color.RED);
            emailField.setTextColor(Color.BLACK);
            Alert.createAlertDialog(getString(R.string.PswdVerif), getString(R.string.PswdIncor), RegisterActivity.this);
        } else if (!EmailCheck.emailIsValid(emailField.getText().toString())) {
            passwordField.setTextColor(Color.BLACK);
            emailField.setTextColor(Color.RED);
            Alert.createAlertDialog(getString(R.string.EmailVerif), getString(R.string.EmailIncor), RegisterActivity.this);
        } else if (!checkBox.isChecked()) {
            checkBox.setTextColor(Color.RED);
            Alert.createAlertDialog(getString(R.string.TOS), getString(R.string.TOSAgree), RegisterActivity.this);
        } else {
            passwordField.setTextColor(Color.BLACK);
            emailField.setTextColor(Color.BLACK);
            checkBox.setTextColor(Color.BLACK);
            User user = new User(null, username, realNameField.getText().toString(),
                    emailField.getText().toString(), establishedPassword, null, null);
            Command command = new Command(Command.CommandType.CREATE_ACCOUNT, null, user, null, null);
            new AddUserTask().execute(command);
        }
    }

    public class AddUserTask extends AsyncTask<Command, Void, Response> {

        protected Response doInBackground(Command... commands) {
            return Server.sendCommandToServer(commands[0], getApplicationContext());
        }

        protected void onPostExecute(Response response) {
            if (response == null)
                Alert.networkError(getApplicationContext());
            else if (!response.isBool()) {
                Alert.generalError(getApplicationContext());
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("xyz.jhughes.socialmaps", MODE_PRIVATE);
                sharedPreferences.edit().putString("username", username).apply();
                sharedPreferences.edit().putString("password", establishedPassword).apply();
                sharedPreferences.edit().putBoolean("loggedIn", true);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }
    }
}
