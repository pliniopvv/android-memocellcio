package br.com.memocellcio;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import br.com.memocellcio.db.Card;
import br.com.memocellcio.db.Conjunto;
import br.com.memocellcio.db.DBFactory;
import br.com.memocellcio.http.ApiService;
import io.objectbox.Box;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ColetarActivity extends Activity {

    Button btnColetar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coletar);
        btnColetar = findViewById(R.id.btn_coletar);
    }

    public void startColetar(View view) {

        String ip = ((EditText) findViewById(R.id.txt_ip)).getText().toString();
        String port = ((EditText) findViewById(R.id.txt_port)).getText().toString();

        btnColetar.setEnabled(false);
        btnColetar.setText("Coletando ...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://" + ip + ":" + port);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(url)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    ApiService service = retrofit.create(ApiService.class);

                    Call<List<br.com.memocellcio.http.Card>> requestListaCards = service.listCards();
                    Response<List<br.com.memocellcio.http.Card>> responseApi = requestListaCards.execute();
                    List<br.com.memocellcio.http.Card> listaCard = responseApi.body();

                    Box<Card> boxCard = DBFactory.get().boxFor(Card.class);
                    Box<Conjunto> boxConjunto = DBFactory.get().boxFor(Conjunto.class);
                    boxConjunto.removeAll();
                    boxCard.removeAll();
                    HashMap<String, Long> listCnjName = new HashMap<>();

                    for (br.com.memocellcio.http.Card card : listaCard) {
//                        Log.i("CARD", String.valueOf(card.id));
//                        if (card.conjunto != null)
//                            Log.i("|CONJUNTO", String.valueOf(card.conjunto.id));
//                        if (card.revisao != null)
//                            Log.i("|REVISAO", String.valueOf(card.revisao.id));

                        if (card.conjunto != null) {
                            Conjunto cnj = new Conjunto(card.conjunto);
                            cnj.id = 0;
                            if (!listCnjName.containsKey(cnj.nome)) {
                                boxConjunto.put(cnj);
                                card.conjunto.id = cnj.id;
                                listCnjName.put(cnj.nome,cnj.id);
                            } else {
                                card.conjunto.id = listCnjName.get(cnj.nome);
                            }
                        }
                        Card c = new Card(card);
                        c.id = 0;
                        boxCard.put(c);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnColetar.setText("Coleta finalizada!");
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}