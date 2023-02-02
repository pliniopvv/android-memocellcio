package br.com.memocellcio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import br.com.memocellcio.db.Conjunto;
import br.com.memocellcio.db.DBFactory;
import io.objectbox.Box;

public class SelecionarConjuntoActivity extends Activity {

    int selectedOption = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Selecione o tema do estudo:");
        setContentView(R.layout.activity_selecionar_conjunto);

        int option = getIntent().getExtras().getInt("MENU");
        selectedOption = option;
        // 0 - flash
        // 1 - aplicar

        LinearLayout linearLayout = findViewById(R.id.layout_linear);
        Box<Conjunto> conjuntoBox = DBFactory.get().boxFor(Conjunto.class);

        List<Conjunto> listCnj = conjuntoBox.getAll();
        for (Conjunto cnj : listCnj) {
//            Log.i("CNJ>", cnj.id + " - "+ cnj.nome);
            Button b = new Button(this);
            b.setText(cnj.id + " - " + cnj.nome);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i;
                    if (selectedOption == 0) {
                        i = new Intent(SelecionarConjuntoActivity.this, FlashActivity.class);
//                    } else if (selectedOption == 1) {
                    } else {
                        i = new Intent(SelecionarConjuntoActivity.this, AplicarActivity.class);
                    }
                    i.putExtra("ID", cnj.id);
                    i.putExtra("TITULO",cnj.id + " - " + cnj.nome);
                    startActivity(i);
                }
            });
            linearLayout.addView(b);
        }
    }
}