package br.com.memocellcio.http;

import java.util.Date;

import br.com.memocellcio.db.Conjunto;
import br.com.memocellcio.db.Revisao;
import io.objectbox.annotation.Id;

public class Card {
    public @Id
    long id;
    public String frente;
    public String tras;
    public Date createAt;
    public Conjunto conjunto;
    public Revisao revisao;
}
