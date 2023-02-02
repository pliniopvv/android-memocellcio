package br.com.memocellcio.db;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Revisao {
    @Id
    public long id;
    public Date updateAt;
    public Date proximaRevisao;
}
