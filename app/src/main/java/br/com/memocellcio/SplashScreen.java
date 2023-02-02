package br.com.memocellcio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import br.com.memocellcio.db.DBFactory;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        DBFactory.init(this);
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}