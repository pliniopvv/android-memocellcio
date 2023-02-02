package br.com.memocellcio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showFlashActivity(View view) {
        Intent i = new Intent(this, SelecionarConjuntoActivity.class);
        i.putExtra("MENU",0);
        startActivity(i);
    }

    public void showAplicarActivity(View view) {
        Intent i = new Intent(this, SelecionarConjuntoActivity.class);
        i.putExtra("MENU",1);
        startActivity(i);
    }

    public void showColetarActivity(View view) {
        Intent i = new Intent(this, ColetarActivity.class);
        startActivity(i);
    }

}