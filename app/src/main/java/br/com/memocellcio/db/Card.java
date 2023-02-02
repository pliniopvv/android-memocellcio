package br.com.memocellcio.db;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class Card {

    public Card() {}
    public Card(br.com.memocellcio.http.Card card) {
        this.id = card.id;
        this.frente = card.frente;
        this.tras = card.tras;

        if (card.conjunto != null) {
            Conjunto c = new Conjunto();
            c.id = card.conjunto.id;
            c.nome = card.conjunto.nome;
            this.conjunto.setTarget(c);
        }
        if (card.revisao != null) {
            ;
            this.revisao.setTarget(null);
        }
    }

    public @Id long id;
    public String frente;
    public String tras;
    public Date createAt;

    public ToOne<Conjunto> conjunto;
    public ToOne<Revisao> revisao;

}
