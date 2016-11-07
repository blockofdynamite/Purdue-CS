package com.example.myapplication2.app;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.*;
import android.view.View;
import android.os.*;
import android.widget.*;

public class Listener implements OnClickListener {

    public void onClick(View arg) {

        Button testButton = (Button) arg;
        MainActivity.logIt(testButton.getText().toString());
    }

}
