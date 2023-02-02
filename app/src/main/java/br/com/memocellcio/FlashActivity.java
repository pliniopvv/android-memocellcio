package br.com.memocellcio;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import br.com.memocellcio.db.Card;
import br.com.memocellcio.db.Card_;
import br.com.memocellcio.db.DBFactory;
import br.com.memocellcio.db.Revisao;
import io.objectbox.Box;

public class FlashActivity extends Activity {
    ArrayList<Card> listaCards;
    Card cardSelected;
    //
    EditText et_frente;
    EditText et_tras;
    //
    TextView tx_cards;
    TextView tx_tempo;
    int tmp_tempo_total = 40;
    int tmp_total_cartas = 0;
    TimerRunnable timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        et_frente = (EditText) findViewById(R.id.flash_frente);
        et_tras = (EditText) findViewById(R.id.flash_tras);
        tx_cards = (TextView) findViewById(R.id.flash_conter_total_cartas);
        tx_tempo = (TextView) findViewById(R.id.flash_conter_total_tempo);
        timer = new TimerRunnable();

        long id = getIntent().getExtras().getLong("ID");
        String title = getIntent().getExtras().getString("TITULO");

        setTitle(title);

        List<Card> listaCardsBox = DBFactory.get().boxFor(Card.class).query().equal(Card_.conjuntoId, id).build().find();
        listaCards = new ArrayList<>();
        Date hoje = new Date();
        for (Card c : listaCardsBox) {
            Revisao r = c.revisao.getTarget();
            if (r == null) {
                listaCards.add(c);
                continue;
            }
            Date d = r.proximaRevisao;
            if (hoje.after(d)) {
                listaCards.add(c);
            }
        }

        Collections.sort(listaCards, (o1, o2) -> {
            Revisao r1 = o1.revisao.getTarget();
            Revisao r2 = o2.revisao.getTarget();

            if (r1 == null) {
                return -1;
            }
            if (r2 == null) {
                return 1;
            }

            Date d1 = r1.proximaRevisao;
            Date d2 = r2.proximaRevisao;

            if (d1.after(d2)) {
                return 1;
            } else if (d1.before(d2)) {
                return -1;
            } else {
                return 0;
            }
        });

        resetContadores();
        nextCard();
        Thread thread = new Thread(timer);
        thread.start();
    }

    public void nextCard() {
        setTras("");
        if (listaCards.size() <= 0) {
            setFrente("Sem cartões no conjunto.");
            cardSelected = null;
            return;
        }

        cardSelected = listaCards.get(0);
        listaCards.remove(0);
        setFrente(cardSelected.frente);
    }

    public void setFrente(String txt) {
        et_frente.setText(txt);
    }

    public void setTras(String txt) {
        et_tras.setText(txt);
    }

    public void onResposta(View view) {
        onResposta();
    }

    public void onResposta() {
        if (cardSelected == null) {
            return;
        }
        setTras(cardSelected.tras);
    }

    public void onFacil(View view) {
        Revisao r = cardSelected.revisao.getTarget();
        if (r == null) {
            Revisao _r = new Revisao();
            _r.proximaRevisao = new Date();
            long tempo = _r.proximaRevisao.getTime() + 60 * 60 * 24 * 4; // 60 * 60 * 24 * 4 -> 4 dias
            _r.proximaRevisao.setTime(tempo);
            Box<Revisao> boxRevisao = DBFactory.get().boxFor(Revisao.class);
            Box<Card> boxCard = DBFactory.get().boxFor(Card.class);
            boxRevisao.put(_r);
            cardSelected.revisao.setTarget(_r);
            boxCard.put(cardSelected);
        }
        nextCard();
        resetContadores();
    }

    public void onMedio(View view) {
        Revisao r = cardSelected.revisao.getTarget();
        if (r == null) {
            Revisao _r = new Revisao();
            _r.proximaRevisao = new Date();
            long tempo = _r.proximaRevisao.getTime() + 60 * 60 * 24 * 1; // 60 * 60 * 24 * 1 -> 1 dia
            _r.proximaRevisao.setTime(tempo);
            Box<Revisao> boxRevisao = DBFactory.get().boxFor(Revisao.class);
            Box<Card> boxCard = DBFactory.get().boxFor(Card.class);
            boxRevisao.put(_r);
            cardSelected.revisao.setTarget(_r);
            boxCard.put(cardSelected);
        }
        nextCard();
        resetContadores();
    }

    public void onDificil(View view) {
        Revisao r = cardSelected.revisao.getTarget();
        if (r == null) {
            Revisao _r = new Revisao();
            _r.proximaRevisao = new Date();
            long tempo = _r.proximaRevisao.getTime() + 60 * 10; // 60 * 60 * 24 * 1 -> 1 dia
            _r.proximaRevisao.setTime(tempo);
            Box<Revisao> boxRevisao = DBFactory.get().boxFor(Revisao.class);
            Box<Card> boxCard = DBFactory.get().boxFor(Card.class);
            boxRevisao.put(_r);
            cardSelected.revisao.setTarget(_r);
            boxCard.put(cardSelected);
        }
        nextCard();
        resetContadores();
    }

    public void resetContadores() {
        tmp_total_cartas = listaCards.size();
        tx_cards.setText(String.valueOf(tmp_total_cartas));
        tmp_tempo_total = 40;
        tx_tempo.setText(String.valueOf(tmp_tempo_total));
    }

    public void tick() {
        if (tmp_total_cartas == 0) {
            return;
        }
        if (tmp_tempo_total == 0) {
            resetContadores();
            nextCard();
            tx_cards.setText(String.valueOf(tmp_total_cartas));
            return;
        }
        if (tmp_tempo_total == 20) {
            EditText resposta = (EditText) findViewById(R.id.flash_tras);
            resposta.setText(cardSelected.tras);
        }
        tmp_tempo_total--;
        tx_tempo.setText(String.valueOf(tmp_tempo_total));
    }

    private class TimerRunnable implements Runnable {

        boolean isOn = true;
//        TextView _tx_cards = (TextView) findViewById(R.id.flash_conter_total_cartas);
//        TextView _tx_tempo = (TextView) findViewById(R.id.flash_conter_total_tempo);


        @Override
        public void run() {
            try {
                while (isOn) {
//                    tick(_tx_tempo, _tx_cards);
//                    tick();
                    if (tmp_total_cartas == 0) {
                        continue;
                    }
                    if (tmp_tempo_total == 0) {
                        runOnUiThread(() -> {
                            resetContadores();
                            nextCard();
                            tx_cards.setText(String.valueOf(tmp_total_cartas));
                        });
                        continue;
                    }
                    if (tmp_tempo_total == 20) {
                        runOnUiThread(() -> {
                            EditText resposta = (EditText) findViewById(R.id.flash_tras);
                            resposta.setText(cardSelected.tras);
                        });
                    }
                    tmp_tempo_total--;
                    tx_tempo.setText(String.valueOf(tmp_tempo_total));
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void shutdown() {
            isOn = false;
        }
    }

    @Override
    protected void onDestroy() {
        timer.shutdown();
        super.onDestroy();
    }
}