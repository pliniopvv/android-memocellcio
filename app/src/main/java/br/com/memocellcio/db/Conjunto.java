package br.com.memocellcio.db;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Conjunto {

    public Conjunto() {}

    public Conjunto(Conjunto cnj) {
        this.id = cnj.id;
        this.nome = cnj.nome;
    }

    public @Id long id;
    public String nome;
}
